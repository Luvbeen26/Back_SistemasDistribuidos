package banksoft.dao;

import banksoft.model.CatalogoBancos;
import banksoft.util.HibernateUtil;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CatalogoBancosDAO {
    private static final Logger logger = LoggerFactory.getLogger(CatalogoBancosDAO.class);

    public CatalogoBancos obtenerPrimero() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM CatalogoBancos", CatalogoBancos.class)
                    .setMaxResults(1)
                    .uniqueResult();
        } catch (Exception e) {
            logger.error("Error al obtener catalogo bancos", e);
            return null;
        }
    }

    public CatalogoBancos obtenerPrimeroODefault() {
        CatalogoBancos cb = obtenerPrimero();
        if (cb == null) {
            cb = new CatalogoBancos();
            cb.setClave(8); // usamos 008 como clave de nuestro banco por consistencia
        }
        return cb;
    }
}
