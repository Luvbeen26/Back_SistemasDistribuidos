package banksoft.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Utilidad para generar y validar tokens JWT
 */
public class JwtUtil {
    private static final String SECRET_KEY = "tuClaveSuperSecretaDeAlMenos32CaracteresParaHS256";
    private static final long EXPIRATION_TIME = 86400000; // 24 horas en milisegundos
    private static final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    /**
     * Genera un token JWT para un usuario
     * @param usuarioId ID del usuario
     * @param nombreUsuario Nombre de usuario
     * @return Token JWT
     */
    public static String generarToken(Integer usuarioId, String nombreUsuario) {
        return Jwts.builder()
                .subject(String.valueOf(usuarioId))
                .claim("nombreUsuario", nombreUsuario)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    /**
     * Valida un token JWT y retorna sus claims
     * @param token Token JWT
     * @return Claims si es válido, null si no
     */
    public static Claims validarToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Extrae el ID del usuario del token
     * @param token Token JWT
     * @return ID del usuario
     */
    public static Integer extraerIdUsuario(String token) {
        Claims claims = validarToken(token);
        if (claims != null) {
            return Integer.parseInt(claims.getSubject());
        }
        return null;
    }

    /**
     * Verifica si el token está expirado
     * @param token Token JWT
     * @return true si está expirado
     */
    public static boolean estaExpirado(String token) {
        Claims claims = validarToken(token);
        if (claims != null) {
            return claims.getExpiration().before(new Date());
        }
        return true;
    }
}
