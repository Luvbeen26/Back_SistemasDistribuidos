package banksoft.filter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filtro JWT para validar tokens en peticiones a los endpoints protegidos
 * Dev 3 entrega esto
 */
@WebFilter(urlPatterns = "/api/*")
public class JwtFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    private static final String SECRET_KEY = "tu-clave-secreta-muy-larga-y-segura-aqui-2024";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("JwtFilter inicializado");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestPath = httpRequest.getRequestURI();

        // Excepciones: login y register no requieren token
        if (requestPath.contains("/api/auth/login") || requestPath.contains("/api/auth/register")) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = httpRequest.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            logger.warn("Solicitud sin token JWT");
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("{\"error\": \"Token no proporcionado\"}");
            return;
        }

        String token = authHeader.substring(BEARER_PREFIX.length());

        try {
            SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String usuario = claims.getSubject();
            logger.info("Token válido para usuario: " + usuario);

            // Agregar usuario al request
            request.setAttribute("usuario", usuario);
            chain.doFilter(request, response);

        } catch (JwtException e) {
            logger.error("Token JWT inválido", e);
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("{\"error\": \"Token inválido o expirado\"}");
        } catch (Exception e) {
            logger.error("Error al procesar token JWT", e);
            httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            httpResponse.getWriter().write("{\"error\": \"Error al procesar token\"}");
        }
    }

    @Override
    public void destroy() {
        logger.info("JwtFilter destruido");
    }
}
