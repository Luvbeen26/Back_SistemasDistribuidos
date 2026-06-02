package banksoft.dao;

import banksoft.model.CatalogoBancos;
import banksoft.util.HibernateUtil;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CatalogoBancosDAO {
    private static final Logger logger = LoggerFactory.getLogger(CatalogoBancosDAO.class);

    public CatalogoBancos obtenerPrimeroODefault() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM CatalogoBancos ORDER BY clave", CatalogoBancos.class)
                    .setMaxResults(1)
                    .uniqueResult();
        } catch (Exception e) {
            logger.error("Error al obtener banco del catalogo", e);
            return null;
        }
    }
}
