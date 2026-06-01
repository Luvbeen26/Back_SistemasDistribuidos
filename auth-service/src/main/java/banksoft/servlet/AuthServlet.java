package banksoft.servlet;

import com.google.gson.Gson;
import banksoft.model.Usuario;
import banksoft.model.Cliente;
import banksoft.dao.ClienteDAO;
import banksoft.dao.UsuarioDAO;
import banksoft.dto.AuthResponse;
import banksoft.dto.LoginRequest;
import banksoft.dto.RegisterRequest;
import banksoft.dto.UsuarioRequest;
import banksoft.dto.ClienteRequest;
import banksoft.util.JwtUtil;
import banksoft.util.MqttPublisher;   // ← AGREGAR
import banksoft.util.MqttTopics;      // ← AGREGAR
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;

import at.favre.lib.crypto.bcrypt.BCrypt;

@WebServlet(urlPatterns = "/api/auth/*", loadOnStartup = 1)
public class AuthServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(AuthServlet.class);
    private final Gson gson = new Gson();
    private UsuarioDAO usuarioDAO;
    private ClienteDAO clienteDAO;
    private banksoft.service.RegistrationService registrationService;
    private MqttPublisher mqtt;   // ← AGREGAR

    @Override
    public void init() throws ServletException {
        this.usuarioDAO = new UsuarioDAO();
        this.clienteDAO = new ClienteDAO();
        this.registrationService = new banksoft.service.RegistrationService();
        this.mqtt = MqttPublisher.getInstance();   // ← AGREGAR
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String pathInfo = request.getPathInfo();
        try {
            if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/login")) {
                login(request, response);
            } else if (pathInfo.equals("/register")) {
                register(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(gson.toJson(new AuthResponse("Endpoint no encontrado", false)));
            }
        } catch (Exception e) {
            logger.error("Error en AuthServlet", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(new AuthResponse("Error en autenticación", false)));
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            LoginRequest loginRequest = null;
            try {
                loginRequest = gson.fromJson(request.getReader(), LoginRequest.class);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson(new AuthResponse("JSON inválido en el request", false)));
                return;
            }

            if (loginRequest == null || loginRequest.getNombreUsuario() == null || loginRequest.getContrasena() == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson(new AuthResponse("Usuario y contraseña son requeridos", false)));
                return;
            }

            Usuario usuario = usuarioDAO.obtenerPorNombreUsuario(loginRequest.getNombreUsuario());

            if (usuario == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(gson.toJson(new AuthResponse("Usuario o contraseña incorrectos", false)));
                logger.warn("Intento de login fallido para usuario: " + loginRequest.getNombreUsuario());

                // ← MQTT: login sospechoso — usuario no existe
                mqtt.publish(MqttTopics.AUTH_LOGIN_SOSPECHOSO, String.format(
                    "{\"usuario\":\"%s\", \"motivo\":\"usuario_no_existe\", \"timestamp\":\"%s\"}",
                    loginRequest.getNombreUsuario(), Instant.now()
                ));
                return;
            }

            if (!BCrypt.verifyer().verify(loginRequest.getContrasena().toCharArray(), usuario.getContrasena()).verified) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(gson.toJson(new AuthResponse("Usuario o contraseña incorrectos", false)));
                logger.warn("Contraseña incorrecta para usuario: " + loginRequest.getNombreUsuario());

                // ← MQTT: login sospechoso — contraseña incorrecta
                mqtt.publish(MqttTopics.AUTH_LOGIN_SOSPECHOSO, String.format(
                    "{\"usuario\":\"%s\", \"motivo\":\"contrasena_incorrecta\", \"timestamp\":\"%s\"}",
                    loginRequest.getNombreUsuario(), Instant.now()
                ));
                return;
            }

            Cliente cliente = clienteDAO.obtenerPorUsuarioId(usuario.getIdUsuario());
            Integer idCliente = cliente != null ? cliente.getIdCliente() : null;
            String token = JwtUtil.generarToken(usuario.getIdUsuario(), usuario.getNombreUsuario(), idCliente);
            AuthResponse authResponse = new AuthResponse(token, usuario.getNombreUsuario(), usuario.getIdUsuario(), idCliente);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson(authResponse));
            logger.info("Login exitoso para usuario: " + usuario.getNombreUsuario());

            // ← MQTT: login exitoso
            mqtt.publish(MqttTopics.AUTH_LOGIN_EXITOSO, String.format(
                "{\"usuarioId\":%d, \"usuario\":\"%s\", \"clienteId\":%s, \"timestamp\":\"%s\"}",
                usuario.getIdUsuario(),
                usuario.getNombreUsuario(),
                idCliente != null ? idCliente.toString() : "null",
                Instant.now()
            ));

        } catch (Exception e) {
            logger.error("Error en login", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(new AuthResponse("Error interno del servidor: " + e.getMessage(), false)));
        }
    }

    private void register(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            RegisterRequest registerRequest = null;
            try {
                registerRequest = gson.fromJson(request.getReader(), RegisterRequest.class);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson(new AuthResponse("JSON inválido en el request", false)));
                return;
            }

            UsuarioRequest usuarioRequest = registerRequest != null ? registerRequest.getUsuario() : null;
            ClienteRequest clienteRequest = registerRequest != null ? registerRequest.getCliente() : null;

            if (usuarioRequest == null || clienteRequest == null
                    || usuarioRequest.getNombreUsuario() == null || usuarioRequest.getContrasena() == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson(new AuthResponse("Todos los campos son requeridos", false)));
                return;
            }

            Usuario usuarioExistente = usuarioDAO.obtenerPorNombreUsuario(usuarioRequest.getNombreUsuario());
            if (usuarioExistente != null) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getWriter().write(gson.toJson(new AuthResponse("El usuario ya existe", false)));
                return;
            }

            try {
                Usuario creado = registrationService.register(registerRequest);
                response.setStatus(HttpServletResponse.SC_CREATED);
                Cliente clienteCreado = clienteDAO.obtenerPorUsuarioId(creado.getIdUsuario());
                Integer idClienteCreado = clienteCreado != null ? clienteCreado.getIdCliente() : null;
                String token = JwtUtil.generarToken(creado.getIdUsuario(), creado.getNombreUsuario(), idClienteCreado);
                AuthResponse resp = new AuthResponse(token, creado.getNombreUsuario(), creado.getIdUsuario(), idClienteCreado);
                response.getWriter().write(gson.toJson(resp));

                // ← MQTT: registro exitoso (opcional, útil para auditoría)
                mqtt.publish(MqttTopics.AUTH_LOGIN_EXITOSO, String.format(
                    "{\"usuarioId\":%d, \"usuario\":\"%s\", \"accion\":\"REGISTRO\", \"timestamp\":\"%s\"}",
                    creado.getIdUsuario(), creado.getNombreUsuario(), Instant.now()
                ));

            } catch (IllegalStateException ise) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getWriter().write(gson.toJson(new AuthResponse(ise.getMessage(), false)));
            } catch (IllegalArgumentException iae) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson(new AuthResponse(iae.getMessage(), false)));
            }
        } catch (Exception e) {
            logger.error("Error en registro", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(new AuthResponse("Error interno del servidor: " + e.getMessage(), false)));
        }
    }
}