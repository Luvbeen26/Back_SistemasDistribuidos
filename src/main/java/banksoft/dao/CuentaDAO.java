package banksoft.dao;

import banksoft.model.CuentaBancaria;
import banksoft.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.security.SecureRandom;
import banksoft.dao.CatalogoBancosDAO;
import banksoft.model.CatalogoBancos;

public class CuentaDAO {
    private static final Logger logger = LoggerFactory.getLogger(CuentaDAO.class);

    public void guardar(CuentaBancaria cuenta) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(cuenta);
            transaction.commit();
            logger.info("Cuenta guardada: " + cuenta.getNumeroCuenta());
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error al guardar cuenta", e);
        }
    }

    private static final SecureRandom RNG = new SecureRandom();

    public String generarNumeroCuentaUnico() {
        String numero;
        do {
            StringBuilder sb = new StringBuilder(16);
            for (int i = 0; i < 16; i++) sb.append(RNG.nextInt(10));
            numero = sb.toString();
        } while (obtenerPorNumero(numero) != null);
        return numero;
    }

    public String generarClabeFromAccount(String numeroCuenta, Integer idSucursal) {
        CatalogoBancosDAO catalogoDao = new CatalogoBancosDAO();
        CatalogoBancos banco = catalogoDao.obtenerPrimeroODefault();
        Integer claveBanco = banco != null ? banco.getClave() : 8;

        String bank = String.format("%03d", claveBanco != null ? claveBanco : 0);
        // According to request: only first 3 digits (bank) must be controlled;
        // the remaining 14 digits can be random.
        StringBuilder rest = new StringBuilder(14);
        for (int i = 0; i < 14; i++) rest.append(RNG.nextInt(10));
        String base17 = bank + rest.toString();
        int[] weights = {3, 7, 1};
        int sum = 0;
        for (int i = 0; i < base17.length(); i++) {
            int digit = Character.getNumericValue(base17.charAt(i));
            sum += digit * weights[i % 3];
        }
        int control = (10 - (sum % 10)) % 10;
        return base17 + control;
    }

    public String generarClabeUnico(String numeroCuenta, Integer idSucursal) {
        String clabe;
        do {
            clabe = generarClabeFromAccount(numeroCuenta, idSucursal);
        } while (obtenerPorClabe(clabe) != null);
        return clabe;
    }

    public CuentaBancaria prepararYGuardarCuenta(CuentaBancaria cuenta) {
        if (cuenta.getNumeroCuenta() == null) {
            cuenta.setNumeroCuenta(generarNumeroCuentaUnico());
        }
        if (cuenta.getClabeInterbancaria() == null) {
            cuenta.setClabeInterbancaria(generarClabeUnico(cuenta.getNumeroCuenta(), cuenta.getIdSucursal()));
        }
        guardar(cuenta);
        return cuenta;
    }

    public CuentaBancaria obtenerPorId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(CuentaBancaria.class, id);
        } catch (Exception e) {
            logger.error("Error al obtener cuenta por ID", e);
            return null;
        }
    }

    public CuentaBancaria obtenerPorNumero(String numeroCuenta) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM CuentaBancaria WHERE numeroCuenta = :numero", CuentaBancaria.class)
                    .setParameter("numero", numeroCuenta)
                    .uniqueResult();
        } catch (Exception e) {
            logger.error("Error al obtener cuenta por número", e);
            return null;
        }
    }

    public CuentaBancaria obtenerPorClabe(String clabe) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM CuentaBancaria WHERE clabeInterbancaria = :clabe", CuentaBancaria.class)
                    .setParameter("clabe", clabe)
                    .uniqueResult();
        } catch (Exception e) {
            logger.error("Error al obtener cuenta por CLABE", e);
            return null;
        }
    }

    public List<CuentaBancaria> obtenerPorCliente(Long clienteId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM CuentaBancaria WHERE cliente.id = :clienteId", CuentaBancaria.class)
                    .setParameter("clienteId", clienteId)
                    .list();
        } catch (Exception e) {
            logger.error("Error al obtener cuentas del cliente", e);
            return null;
        }
    }

    public void actualizar(CuentaBancaria cuenta) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(cuenta);
            transaction.commit();
            logger.info("Cuenta actualizada: " + cuenta.getIdCuenta());
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error al actualizar cuenta", e);
        }
    }

    public void eliminar(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CuentaBancaria cuenta = session.get(CuentaBancaria.class, id);
            if (cuenta != null) {
                session.remove(cuenta);
                transaction.commit();
                logger.info("Cuenta eliminada: " + id);
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error al eliminar cuenta", e);
        }
    }
}
