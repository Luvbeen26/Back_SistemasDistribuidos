package banksoft.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtil {
    private static final String SECRET_KEY = AppProperties.getRequired("jwt.secret");
    private static final long EXPIRATION_TIME = 86400000; // 24 horas en milisegundos
    private static final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    private static final MacAlgorithm ALG = Jwts.SIG.HS256;

    public static String generarToken(Integer usuarioId, String nombreUsuario, Integer idCliente) {
        io.jsonwebtoken.JwtBuilder builder = Jwts.builder()
                .subject(String.valueOf(usuarioId))
                .claim("nombreUsuario", nombreUsuario)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME));

        if (idCliente != null) {
            builder.claim("id_cliente", idCliente);
        }

        return builder.signWith(key, ALG).compact();
    }

    
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

}
