package banksoft.servlet;

import com.google.gson.Gson;
import banksoft.model.Cliente;
import banksoft.dao.ClienteDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet para gestionar operaciones CRUD de Clientes
 */
@WebServlet(urlPatterns = "/api/clientes", loadOnStartup = 1)
public class ClienteServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ClienteServlet.class);
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // GET /api/clientes - Obtener todos
                List<Cliente> clientes = clienteDAO.obtenerTodos();
                response.getWriter().write(gson.toJson(clientes));
            } else {
                // GET /api/clientes/{id}
                Integer id = Integer.parseInt(pathInfo.substring(1));
                Cliente cliente = clienteDAO.obtenerPorId(id);
                if (cliente != null) {
                    response.getWriter().write(gson.toJson(cliente));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"error\": \"Cliente no encontrado\"}");
                }
            }
        } catch (Exception e) {
            logger.error("Error en GET /clientes", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error al obtener clientes\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            Cliente cliente = gson.fromJson(request.getReader(), Cliente.class);
            clienteDAO.guardar(cliente);
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write(gson.toJson(cliente));
            logger.info("Cliente creado: " + cliente.getCurp());
        } catch (Exception e) {
            logger.error("Error en POST /clientes", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error al crear cliente\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            Cliente cliente = gson.fromJson(request.getReader(), Cliente.class);
            clienteDAO.actualizar(cliente);
            response.getWriter().write(gson.toJson(cliente));
            logger.info("Cliente actualizado: " + cliente.getIdCliente());
        } catch (Exception e) {
            logger.error("Error en PUT /clientes", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error al actualizar cliente\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String pathInfo = request.getPathInfo();
            Integer id = Integer.parseInt(pathInfo.substring(1));
            clienteDAO.eliminar(id);
            response.getWriter().write("{\"mensaje\": \"Cliente eliminado\"}");
            logger.info("Cliente eliminado: " + id);
        } catch (Exception e) {
            logger.error("Error en DELETE /clientes", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error al eliminar cliente\"}");
        }
    }
}
