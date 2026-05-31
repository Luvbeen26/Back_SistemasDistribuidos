package banksoft.dao;

import banksoft.model.CodigoPostal;
import banksoft.util.HibernateUtil;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CodigoPostalDAO {
    private static final Logger logger = LoggerFactory.getLogger(CodigoPostalDAO.class);

    public CodigoPostal obtenerPorCp(Integer cp) {
        if (cp == null) return null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM CodigoPostal WHERE cp = :cp", CodigoPostal.class)
                    .setParameter("cp", cp)
                    .uniqueResult();
        } catch (Exception e) {
            logger.error("Error al buscar codigo postal", e);
            return null;
        }
    }
}
