package banksoft.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Integer idCliente;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "apellido_1", length = 20)
    private String apellido1;

    @Column(name = "apellido_2", length = 20)
    private String apellido2;

    @Column(name = "rfc", nullable = false, length = 13, unique = true)
    private String rfc;

    @Column(name = "fecha_alta", nullable = false)
    private LocalDate fechaAlta;

    @Column(name = "id_sucursal", nullable = false)
    private Integer idSucursal;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "curp", length = 18, unique = true)
    private String curp;

    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "telefono", nullable = false, length = 15)
    private String telefono;

    @Column(name = "id_codigo_postal")
    private Integer idCodigoPostal;

    @Column(name = "numero_exterior", length = 45)
    private String numeroExterior;

    @Column(name = "numero_interior", length = 45)
    private String numeroInterior;

    @Column(name = "calle", nullable = false, length = 45)
    private String calle;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "estatus", nullable = false, length = 1)
    private String estatus = "A";



    // Constructores
    public Cliente() {
        this.fechaAlta = LocalDate.now();
        this.estatus = "A";
    }

    public Cliente(String nombre, String rfc, Integer idSucursal, String email, String telefono, String calle) {
        this.nombre = nombre;
        this.rfc = rfc;
        this.idSucursal = idSucursal;
        this.email = email;
        this.telefono = telefono;
        this.calle = calle;
        this.fechaAlta = LocalDate.now();
        this.estatus = "A";
    }

    // Getters y Setters
    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido1() { return apellido1; }
    public void setApellido1(String apellido1) { this.apellido1 = apellido1; }

    public String getApellido2() { return apellido2; }
    public void setApellido2(String apellido2) { this.apellido2 = apellido2; }

    public String getRfc() { return rfc; }
    public void setRfc(String rfc) { this.rfc = rfc; }

    public LocalDate getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(LocalDate fechaAlta) { this.fechaAlta = fechaAlta; }

    public Integer getIdSucursal() { return idSucursal; }
    public void setIdSucursal(Integer idSucursal) { this.idSucursal = idSucursal; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getCurp() { return curp; }
    public void setCurp(String curp) { this.curp = curp; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public Integer getIdCodigoPostal() { return idCodigoPostal; }
    public void setIdCodigoPostal(Integer idCodigoPostal) { this.idCodigoPostal = idCodigoPostal; }

    public String getNumeroExterior() { return numeroExterior; }
    public void setNumeroExterior(String numeroExterior) { this.numeroExterior = numeroExterior; }

    public String getNumeroInterior() { return numeroInterior; }
    public void setNumeroInterior(String numeroInterior) { this.numeroInterior = numeroInterior; }

    public String getCalle() { return calle; }
    public void setCalle(String calle) { this.calle = calle; }

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public String getEstatus() { return estatus; }
    public void setEstatus(String estatus) { this.estatus = estatus; }


}
