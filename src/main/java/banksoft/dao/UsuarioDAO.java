package banksoft.dao;

import banksoft.model.Usuario;
import banksoft.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * DAO para operaciones CRUD con la tabla de Usuarios
 */
public class UsuarioDAO {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioDAO.class);

    public Usuario guardar(Usuario usuario) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.persist(usuario);
            session.flush();
            transaction.commit();
            logger.info("Usuario guardado: " + usuario.getNombreUsuario());
            return usuario;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                try { transaction.rollback(); } catch (Exception ex) { logger.error("Error en rollback", ex); }
            }
            logger.error("Error al guardar usuario", e);
            throw e; // re-lanzar para que el servlet lo maneje
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public Usuario obtenerPorId(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Usuario.class, id);
        } catch (Exception e) {
            logger.error("Error al obtener usuario por ID", e);
            return null;
        }
    }

    public Usuario obtenerPorNombreUsuario(String nombreUsuario) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Usuario WHERE nombreUsuario = :nombreUsuario", Usuario.class)
                    .setParameter("nombreUsuario", nombreUsuario)
                    .uniqueResult();
        } catch (Exception e) {
            logger.error("Error al obtener usuario por nombre de usuario", e);
            return null;
        }
    }

    public List<Usuario> obtenerTodos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Usuario", Usuario.class).list();
        } catch (Exception e) {
            logger.error("Error al obtener todos los usuarios", e);
            return null;
        }
    }

    public void actualizar(Usuario usuario) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(usuario);
            transaction.commit();
            logger.info("Usuario actualizado: " + usuario.getNombreUsuario());
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error al actualizar usuario", e);
        }
    }

    public void eliminar(Integer id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Usuario usuario = session.get(Usuario.class, id);
            if (usuario != null) {
                session.remove(usuario);
                transaction.commit();
                logger.info("Usuario eliminado: " + id);
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error al eliminar usuario", e);
        }
    }
}
