package banksoft.servlet;

import com.google.gson.Gson;
import banksoft.model.CuentaBancaria;
import banksoft.dao.CuentaDAO;
import banksoft.util.MqttPublisher;  // ← AGREGAR
import banksoft.util.MqttTopics;     // ← AGREGAR
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;  // ← AGREGAR
import java.util.List;

@WebServlet(urlPatterns = "/api/cuentas/*", loadOnStartup = 2)
public class CuentaServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(CuentaServlet.class);
    private final CuentaDAO cuentaDAO = new CuentaDAO();
    private final Gson gson = new Gson();
    private MqttPublisher mqtt;  // ← AGREGAR

    @Override
    public void init() throws ServletException {
        this.mqtt = MqttPublisher.getInstance();  // ← AGREGAR
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String pathInfo = request.getPathInfo();
        String clienteIdParam = request.getParameter("clienteId");

        try {
            if (clienteIdParam != null) {
                Long clienteId = Long.parseLong(clienteIdParam);
                List<CuentaBancaria> cuentas = cuentaDAO.obtenerPorCliente(clienteId);
                response.getWriter().write(gson.toJson(cuentas));
            } else if (pathInfo == null || pathInfo.equals("/")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Use clienteId query parameter\"}");
            } else {
                Long id = Long.parseLong(pathInfo.substring(1));
                CuentaBancaria cuenta = cuentaDAO.obtenerPorId(id);
                if (cuenta != null) {
                    response.getWriter().write(gson.toJson(cuenta));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"error\": \"Cuenta no encontrada\"}");
                }
            }
        } catch (Exception e) {
            logger.error("Error en GET /cuentas", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error al obtener cuentas\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            CuentaBancaria cuenta = gson.fromJson(request.getReader(), CuentaBancaria.class);
            cuentaDAO.guardar(cuenta);
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write(gson.toJson(cuenta));
            logger.info("Cuenta creada: " + cuenta.getNumeroCuenta());
        } catch (Exception e) {
            logger.error("Error en POST /cuentas", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error al crear cuenta\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Guardar estado anterior para comparar
            CuentaBancaria cuentaAnterior = null;
            CuentaBancaria cuenta = gson.fromJson(request.getReader(), CuentaBancaria.class);

            if (cuenta.getIdCuenta() != null) {
                cuentaAnterior = cuentaDAO.obtenerPorId(cuenta.getIdCuenta());
            }

            cuentaDAO.actualizar(cuenta);
            response.getWriter().write(gson.toJson(cuenta));
            logger.info("Cuenta actualizada: " + cuenta.getIdCuenta());

            // ← MQTT: detectar qué cambió y publicar evento correspondiente
            if (cuentaAnterior != null) {
                boolean estabaActiva   = cuentaAnterior.isActiva();
                boolean ahoraActiva    = cuenta.isActiva();
                boolean cambioLimite   = cuentaAnterior.getLimiteTransferencia() != null
                        && !cuentaAnterior.getLimiteTransferencia().equals(cuenta.getLimiteTransferencia());

                // Cuenta bloqueada
                if (estabaActiva && !ahoraActiva) {
                    mqtt.publish(MqttTopics.CUENTA_BLOQUEADA, String.format(
                        "{\"cuentaId\":%d, \"numeroCuenta\":\"%s\", \"clienteId\":%d, \"accion\":\"BLOQUEADA\", \"timestamp\":\"%s\"}",
                        cuenta.getIdCuenta(),
                        cuenta.getNumeroCuenta(),
                        cuenta.getCliente() != null ? cuenta.getCliente().getIdCliente() : 0,
                        Instant.now()
                    ));
                    logger.info("MQTT: cuenta bloqueada " + cuenta.getNumeroCuenta());
                }

                // Cuenta desbloqueada
                if (!estabaActiva && ahoraActiva) {
                    mqtt.publish(MqttTopics.CUENTA_DESBLOQUEADA, String.format(
                        "{\"cuentaId\":%d, \"numeroCuenta\":\"%s\", \"clienteId\":%d, \"accion\":\"DESBLOQUEADA\", \"timestamp\":\"%s\"}",
                        cuenta.getIdCuenta(),
                        cuenta.getNumeroCuenta(),
                        cuenta.getCliente() != null ? cuenta.getCliente().getIdCliente() : 0,
                        Instant.now()
                    ));
                    logger.info("MQTT: cuenta desbloqueada " + cuenta.getNumeroCuenta());
                }

                // Límite de transferencia cambiado
                if (cambioLimite) {
                    mqtt.publish(MqttTopics.CUENTA_LIMITE_CAMBIO, String.format(
                        "{\"cuentaId\":%d, \"numeroCuenta\":\"%s\", \"clienteId\":%d, \"limiteAnterior\":%s, \"limiteNuevo\":%s, \"timestamp\":\"%s\"}",
                        cuenta.getIdCuenta(),
                        cuenta.getNumeroCuenta(),
                        cuenta.getCliente() != null ? cuenta.getCliente().getIdCliente() : 0,
                        cuentaAnterior.getLimiteTransferencia(),
                        cuenta.getLimiteTransferencia(),
                        Instant.now()
                    ));
                    logger.info("MQTT: límite cambiado en cuenta " + cuenta.getNumeroCuenta());
                }
            }

        } catch (Exception e) {
            logger.error("Error en PUT /cuentas", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error al actualizar cuenta\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String pathInfo = request.getPathInfo();
            Long id = Long.parseLong(pathInfo.substring(1));
            cuentaDAO.eliminar(id);
            response.getWriter().write("{\"mensaje\": \"Cuenta eliminada\"}");
            logger.info("Cuenta eliminada: " + id);
        } catch (Exception e) {
            logger.error("Error en DELETE /cuentas", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error al eliminar cuenta\"}");
        }
    }
}