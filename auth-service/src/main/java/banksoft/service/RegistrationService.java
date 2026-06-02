package banksoft.service;

import banksoft.dto.RegisterRequest;
import banksoft.dto.ClienteRequest;
import banksoft.model.Usuario;
import banksoft.model.Cliente;
import banksoft.model.CuentaBancaria;
import banksoft.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;

public class RegistrationService {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationService.class);
    private final SecureRandom rng = new SecureRandom();

    /** Registra usuario, cliente y cuenta en una sola transacción.     */
    public Usuario register(RegisterRequest request) throws Exception {
        if (request == null || request.getUsuario() == null || request.getCliente() == null) {
            throw new IllegalArgumentException("Request de registro incompleto");
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombreUsuario(request.getUsuario().getNombreUsuario());
        //la contraseña debe venir ya en texto plano;
        nuevoUsuario.setContrasena(request.getUsuario().getContrasena());
        nuevoUsuario.setEstatus("A");

        ClienteRequest creq = request.getCliente();

        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            // Verificar nombre de usuario único
            Usuario existe = session.createQuery("FROM Usuario WHERE nombreUsuario = :n", Usuario.class)
                    .setParameter("n", nuevoUsuario.getNombreUsuario())
                    .uniqueResult();
            if (existe != null) {
                throw new IllegalStateException("Usuario ya existe");
            }

            // Encriptar contraseña aquí (usar BCrypt fuera si prefieres)
            String hashed = at.favre.lib.crypto.bcrypt.BCrypt.withDefaults()
                    .hashToString(12, nuevoUsuario.getContrasena().toCharArray());
            nuevoUsuario.setContrasena(hashed);

            // Persistir usuario
            session.persist(nuevoUsuario);
            session.flush();

            // Crear cliente usando los campos disponibles
            Cliente cliente = new Cliente();
            cliente.setIdUsuario(nuevoUsuario.getIdUsuario());
            if (creq.getNombre() != null) cliente.setNombre(creq.getNombre());
            if (creq.getApellido1() != null) cliente.setApellido1(creq.getApellido1());
            if (creq.getApellido2() != null) cliente.setApellido2(creq.getApellido2());
            if (creq.getRfc() != null) cliente.setRfc(creq.getRfc());
            if (creq.getFechaNacimiento() != null) {
                try { cliente.setFechaNacimiento(LocalDate.parse(creq.getFechaNacimiento())); } catch (Exception e) { /* ignore */ }
            }
            if (creq.getCurp() != null) cliente.setCurp(creq.getCurp());
            if (creq.getTelefono() != null) cliente.setTelefono(creq.getTelefono());
            if (creq.getCp() != null) {
                try {
                    Integer cpInt = Integer.parseInt(creq.getCp());
                    banksoft.dao.CodigoPostalDAO cpDao = new banksoft.dao.CodigoPostalDAO();
                    banksoft.model.CodigoPostal cpEntity = cpDao.obtenerPorCp(cpInt);
                    if (cpEntity == null) {
                        throw new IllegalArgumentException("Código postal no encontrado: " + creq.getCp());
                    }
                    cliente.setIdCodigoPostal(cpEntity.getIdCodigoPostal());
                } catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("Código postal inválido: " + creq.getCp());
                }
            }
            if (creq.getCalle() != null) cliente.setCalle(creq.getCalle());
            if (creq.getNumero() != null) cliente.setNumeroExterior(creq.getNumero());
            if (creq.getCorreo() != null) cliente.setEmail(creq.getCorreo());
            // default sucursal
            cliente.setIdSucursal(1);

            session.persist(cliente);
            session.flush();

            // guardar el id del cliente también en el usuario para dejar la relación duplicada lista
            nuevoUsuario.setIdCliente(cliente.getIdCliente());
            session.merge(nuevoUsuario);
            session.flush();

            // Crear cuenta con valores por defecto
            CuentaBancaria cuenta = new CuentaBancaria();
            cuenta.setIdCliente(cliente.getIdCliente());
            cuenta.setIdTipoCuenta(1);
            cuenta.setIdSucursal(cliente.getIdSucursal() != null ? cliente.getIdSucursal() : 1);
            cuenta.setSaldo(BigDecimal.ZERO);
            cuenta.setLimite(new BigDecimal("100000"));
            cuenta.setFechaApertura(LocalDate.now());
            cuenta.setBloqHasta(null);
            cuenta.setEsMostrada(1);

            // Generar numeroCuenta único
            String numeroCuenta;
            do {
                StringBuilder sb = new StringBuilder(16);
                for (int i = 0; i < 16; i++) sb.append(rng.nextInt(10));
                numeroCuenta = sb.toString();
            } while (session.createQuery("FROM CuentaBancaria WHERE numeroCuenta = :n", CuentaBancaria.class)
                    .setParameter("n", numeroCuenta)
                    .uniqueResult() != null);
            cuenta.setNumeroCuenta(numeroCuenta);

            // Generar CLABE con prefijo 008 y dígito verificador
            String base17;
            String clabe;
            do {
                StringBuilder rest = new StringBuilder(14);
                for (int i = 0; i < 14; i++) rest.append(rng.nextInt(10));
                base17 = String.format("%03d", 8) + rest.toString();
                int[] weights = {3,7,1};
                int sum = 0;
                for (int i = 0; i < base17.length(); i++) {
                    int digit = Character.getNumericValue(base17.charAt(i));
                    sum += digit * weights[i % 3];
                }
                int control = (10 - (sum % 10)) % 10;
                clabe = base17 + control;
            } while (session.createQuery("FROM CuentaBancaria WHERE clabeInterbancaria = :c", CuentaBancaria.class)
                    .setParameter("c", clabe)
                    .uniqueResult() != null);
            cuenta.setClabeInterbancaria(clabe);

            session.persist(cuenta);
            session.flush();

            tx.commit();

            return nuevoUsuario;
        } catch (Exception e) {
            if (tx != null) try { tx.rollback(); } catch (Exception ex) { logger.error("Rollback error", ex); }
            logger.error("Error registrando usuario", e);
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }
}
