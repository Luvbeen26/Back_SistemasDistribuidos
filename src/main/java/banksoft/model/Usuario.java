package banksoft.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario {
    private Integer idUsuario;

    private Integer idCliente;

    private String nombreUsuario;

    private String contrasena;

    private String estatus;
 
    private String correo;

    public Usuario() {}

    public Usuario(String nombreUsuario, String contrasena) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.estatus = "A";
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    @Column(name = "id_cliente")
    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }

    @Column(name = "nombre_usuario", length = 30)
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    @Column(name = "contrasena", length = 200)
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    @Column(name = "estatus", length = 1)
    public String getEstatus() { return estatus; }
    public void setEstatus(String estatus) { this.estatus = estatus; }
 
   /*  @Column(name = "correo", length = 100)
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    */
}
