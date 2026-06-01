package banksoft.dao;

import banksoft.model.Movimiento;
import banksoft.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MovimientoDAO {
    private static final Logger logger = LoggerFactory.getLogger(MovimientoDAO.class);

    public void guardar(Movimiento movimiento) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(movimiento);
            transaction.commit();
            logger.info("Movimiento guardado: " + movimiento.getIdMovimiento());
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            logger.error("Error al guardar movimiento", e);
        }
    }

    public Movimiento obtenerPorId(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Movimiento.class, id);
        } catch (Exception e) {
            logger.error("Error al obtener movimiento por ID", e);
            return null;
        }
    }

    public List<Movimiento> obtenerPorCuenta(Integer cuentaId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session
                    .createQuery("FROM Movimiento WHERE idCuenta = :cuentaId ORDER BY fechaHora DESC", Movimiento.class)
                    .setParameter("cuentaId", cuentaId)
                    .list();
        } catch (Exception e) {
            logger.error("Error al obtener movimientos de la cuenta", e);
            return null;
        }
    }

    public void actualizar(Movimiento movimiento) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(movimiento);
            transaction.commit();
            logger.info("Movimiento actualizado: " + movimiento.getIdMovimiento());
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            logger.error("Error al actualizar movimiento", e);
        }
    }

    public void eliminar(Integer id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Movimiento movimiento = session.get(Movimiento.class, id);
            if (movimiento != null) {
                session.remove(movimiento);
                transaction.commit();
                logger.info("Movimiento eliminado: " + id);
            }
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            logger.error("Error al eliminar movimiento", e);
        }
    }
}
