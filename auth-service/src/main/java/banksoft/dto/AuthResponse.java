package banksoft.dto;

/**
 * DTO para respuestas de autenticación
 */
public class AuthResponse {
    private String token;
    private String nombreUsuario;
    private Integer idUsuario;
    private Integer idCliente;
    private String mensaje;
    private boolean exito;

    public AuthResponse() {}

    public AuthResponse(String token, String nombreUsuario, Integer idUsuario) {
        this.token = token;
        this.nombreUsuario = nombreUsuario;
        this.idUsuario = idUsuario;
        this.exito = true;
    }

    public AuthResponse(String token, String nombreUsuario, Integer idUsuario, Integer idCliente) {
        this.token = token;
        this.nombreUsuario = nombreUsuario;
        this.idUsuario = idUsuario;
        this.idCliente = idCliente;
        this.exito = true;
    }

    public AuthResponse(String mensaje, boolean exito) {
        this.mensaje = mensaje;
        this.exito = exito;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public boolean isExito() { return exito; }
    public void setExito(boolean exito) { this.exito = exito; }
}
