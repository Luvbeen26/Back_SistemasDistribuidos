package banksoft.dao;

import banksoft.model.Cliente;
import banksoft.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ClienteDAO {
    private static final Logger logger = LoggerFactory.getLogger(ClienteDAO.class);

    public Cliente guardar(Cliente cliente) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(cliente);
            session.flush();
            transaction.commit();
            logger.info("Cliente guardado: " + cliente.getCurp());
            return cliente;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error al guardar cliente", e);
            return null;
        }
    }

    public Cliente obtenerPorId(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Cliente.class, id);
        } catch (Exception e) {
            logger.error("Error al obtener cliente por ID", e);
            return null;
        }
    }

    public Cliente obtenerPorCurp(String curp) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Cliente WHERE curp = :curp", Cliente.class)
                    .setParameter("curp", curp)
                    .uniqueResult();
        } catch (Exception e) {
            logger.error("Error al obtener cliente por CURP", e);
            return null;
        }
    }

    public List<Cliente> obtenerTodos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Cliente", Cliente.class).list();
        } catch (Exception e) {
            logger.error("Error al obtener todos los clientes", e);
            return null;
        }
    }

    public Cliente obtenerPorUsuarioId(Integer idUsuario) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Cliente WHERE idUsuario = :uid", Cliente.class)
                    .setParameter("uid", idUsuario)
                    .uniqueResult();
        } catch (Exception e) {
            logger.error("Error al obtener cliente por idUsuario", e);
            return null;
        }
    }

    public void actualizar(Cliente cliente) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(cliente);
            transaction.commit();
            logger.info("Cliente actualizado: " + cliente.getIdCliente());
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error al actualizar cliente", e);
        }
    }

    public void eliminar(Integer id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Cliente cliente = session.get(Cliente.class, id);
            if (cliente != null) {
                session.remove(cliente);
                transaction.commit();
                logger.info("Cliente eliminado: " + id);
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error al eliminar cliente", e);
        }
    }
}
