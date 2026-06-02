package banksoft.servlet;

import com.google.gson.Gson;
import banksoft.model.Movimiento;
import banksoft.dao.MovimientoDAO;
import banksoft.util.MqttPublisher; // ← AGREGAR
import banksoft.util.MqttTopics; // ← AGREGAR
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant; // ← AGREGAR
import java.util.List;

@WebServlet(urlPatterns = "/api/movimientos/*", loadOnStartup = 3)
public class MovimientoServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(MovimientoServlet.class);
    private final MovimientoDAO movimientoDAO = new MovimientoDAO();
    private final Gson gson = new Gson();
    private MqttPublisher mqtt; // ← AGREGAR

    @Override
    public void init() throws ServletException {
        this.mqtt = MqttPublisher.getInstance(); // ← AGREGAR
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String pathInfo = request.getPathInfo();
        String cuentaIdParam = request.getParameter("cuentaId");

        try {
            if (cuentaIdParam != null) {
                Integer cuentaId = Integer.parseInt(cuentaIdParam);
                List<Movimiento> movimientos = movimientoDAO.obtenerPorCuenta(cuentaId);
                response.getWriter().write(gson.toJson(movimientos));
            } else if (pathInfo == null || pathInfo.equals("/")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Use cuentaId query parameter\"}");
            } else {
                Integer id = Integer.parseInt(pathInfo.substring(1));
                Movimiento movimiento = movimientoDAO.obtenerPorId(id);
                if (movimiento != null) {
                    response.getWriter().write(gson.toJson(movimiento));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"error\": \"Movimiento no encontrado\"}");
                }
            }
        } catch (Exception e) {
            logger.error("Error en GET /movimientos", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error al obtener movimientos\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            Movimiento movimiento = gson.fromJson(request.getReader(), Movimiento.class);
            movimientoDAO.guardar(movimiento);
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write(gson.toJson(movimiento));
            logger.info("Movimiento registrado: " + movimiento.getIdMovimiento());

            // ← MQTT: publicar evento de movimiento
            boolean esTransferencia = movimiento.getClabeInterbancaria() != null
                    && !movimiento.getClabeInterbancaria().isBlank();

            if (esTransferencia) {
                mqtt.publish(MqttTopics.TRANSFERENCIA_COMPLETADA, String.format(
                        "{\"movimientoId\":%d, \"cuentaId\":%d, \"clabeInterbancaria\":\"%s\", \"monto\":%s, \"timestamp\":\"%s\"}",
                        movimiento.getIdMovimiento(),
                        movimiento.getIdCuenta(),
                        movimiento.getClabeInterbancaria(),
                        movimiento.getImporte(),
                        Instant.now()));
                logger.info("MQTT: transferencia completada, movimiento " + movimiento.getIdMovimiento());
            }

        } catch (Exception e) {
            logger.error("Error en POST /movimientos", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error al crear movimiento\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            Movimiento movimiento = gson.fromJson(request.getReader(), Movimiento.class);
            movimientoDAO.actualizar(movimiento);
            response.getWriter().write(gson.toJson(movimiento));
            logger.info("Movimiento actualizado: " + movimiento.getIdMovimiento());
        } catch (Exception e) {
            logger.error("Error en PUT /movimientos", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error al actualizar movimiento\"}");
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
            movimientoDAO.eliminar(id);
            response.getWriter().write("{\"mensaje\": \"Movimiento eliminado\"}");
            logger.info("Movimiento eliminado: " + id);
        } catch (Exception e) {
            logger.error("Error en DELETE /movimientos", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error al eliminar movimiento\"}");
        }
    }
}