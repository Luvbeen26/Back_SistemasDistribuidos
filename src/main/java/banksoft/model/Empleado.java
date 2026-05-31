package banksoft.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "empleado")
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empleado")
    private Integer idEmpleado;

    @Column(name = "nombre", length = 30)
    private String nombre;

    @Column(name = "apellido_1", length = 30)
    private String apellido1;

    @Column(name = "apellido_2", length = 30)
    private String apellido2;

    @Column(name = "id_sucursal")
    private Integer idSucursal;

    @Column(name = "rfc", length = 13)
    private String rfc;

    @Column(name = "curp", length = 18)
    private String curp;

    @Column(name = "nss", length = 11)
    private String nss;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "id_puesto")
    private Integer idPuesto;

    @Column(name = "id_contrato")
    private Integer idContrato;

    @Column(name = "fecha_ingreso_laboral")
    private LocalDate fechaIngresoLaboral;

    @Column(name = "sueldo_diario", precision = 10, scale = 2)
    private BigDecimal sueldoDiario;

    @Column(name = "telefono", length = 10)
    private String telefono;

    @Column(name = "correo", length = 50)
    private String correo;

    @Column(name = "id_codigo_postal")
    private Integer idCodigoPostal;

    @Column(name = "calle", length = 50)
    private String calle;

    @Column(name = "numero_exterior", length = 10)
    private String numeroExterior;

    @Column(name = "numero_interior", length = 10)
    private String numeroInterior;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "estatus", length = 1)
    private String estatus;

    @Column(name = "id_cuenta_bancaria")
    private Integer idCuentaBancaria;
 

    public Empleado() {}

    public Empleado(String nombre, Integer idSucursal, Integer idPuesto) {
        this.nombre = nombre;
        this.idSucursal = idSucursal;
        this.idPuesto = idPuesto;
    }

    public Integer getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(Integer idEmpleado) { this.idEmpleado = idEmpleado; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido1() { return apellido1; }
    public void setApellido1(String apellido1) { this.apellido1 = apellido1; }

    public String getApellido2() { return apellido2; }
    public void setApellido2(String apellido2) { this.apellido2 = apellido2; }

    public Integer getIdSucursal() { return idSucursal; }
    public void setIdSucursal(Integer idSucursal) { this.idSucursal = idSucursal; }

    public String getRfc() { return rfc; }
    public void setRfc(String rfc) { this.rfc = rfc; }

    public String getCurp() { return curp; }
    public void setCurp(String curp) { this.curp = curp; }

    public String getNss() { return nss; }
    public void setNss(String nss) { this.nss = nss; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public Integer getIdPuesto() { return idPuesto; }
    public void setIdPuesto(Integer idPuesto) { this.idPuesto = idPuesto; }

    public Integer getIdContrato() { return idContrato; }
    public void setIdContrato(Integer idContrato) { this.idContrato = idContrato; }

    public LocalDate getFechaIngresoLaboral() { return fechaIngresoLaboral; }
    public void setFechaIngresoLaboral(LocalDate fechaIngresoLaboral) { this.fechaIngresoLaboral = fechaIngresoLaboral; }

    public BigDecimal getSueldoDiario() { return sueldoDiario; }
    public void setSueldoDiario(BigDecimal sueldoDiario) { this.sueldoDiario = sueldoDiario; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public Integer getIdCodigoPostal() { return idCodigoPostal; }
    public void setIdCodigoPostal(Integer idCodigoPostal) { this.idCodigoPostal = idCodigoPostal; }

    public String getCalle() { return calle; }
    public void setCalle(String calle) { this.calle = calle; }

    public String getNumeroExterior() { return numeroExterior; }
    public void setNumeroExterior(String numeroExterior) { this.numeroExterior = numeroExterior; }

    public String getNumeroInterior() { return numeroInterior; }
    public void setNumeroInterior(String numeroInterior) { this.numeroInterior = numeroInterior; }

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public String getEstatus() { return estatus; }
    public void setEstatus(String estatus) { this.estatus = estatus; }

    public Integer getIdCuentaBancaria() { return idCuentaBancaria; }
    public void setIdCuentaBancaria(Integer idCuentaBancaria) { this.idCuentaBancaria = idCuentaBancaria; }
 
}
