package banksoft.dto;

/**
 * DTO para solicitudes de registro
 */
public class RegisterRequest {
    private UsuarioRequest usuario;
    private ClienteRequest cliente;

    public RegisterRequest() {}
    
    public UsuarioRequest getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioRequest usuario) {
        this.usuario = usuario;
    }

    public ClienteRequest getCliente() {
        return cliente;
    }

    public void setCliente(ClienteRequest cliente) {
        this.cliente = cliente;
    }
}
