package banksoft.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_sueldos")
public class HistorialSueldos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_empleado")
    private Integer idEmpleado;

    @Column(name = "sueldo_anterior", precision = 10, scale = 2)
    private BigDecimal sueldoAnterior;

    @Column(name = "sueldo_nuevo", precision = 10, scale = 2)
    private BigDecimal sueldoNuevo;

    @Column(name = "fecha_cambio")
    private LocalDateTime fechaCambio;
 

    public HistorialSueldos() {}

    public HistorialSueldos(Integer idEmpleado, BigDecimal sueldoAnterior, BigDecimal sueldoNuevo) {
        this.idEmpleado = idEmpleado;
        this.sueldoAnterior = sueldoAnterior;
        this.sueldoNuevo = sueldoNuevo;
        this.fechaCambio = LocalDateTime.now();
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(Integer idEmpleado) { this.idEmpleado = idEmpleado; }

    public BigDecimal getSueldoAnterior() { return sueldoAnterior; }
    public void setSueldoAnterior(BigDecimal sueldoAnterior) { this.sueldoAnterior = sueldoAnterior; }

    public BigDecimal getSueldoNuevo() { return sueldoNuevo; }
    public void setSueldoNuevo(BigDecimal sueldoNuevo) { this.sueldoNuevo = sueldoNuevo; }

    public LocalDateTime getFechaCambio() { return fechaCambio; }
    public void setFechaCambio(LocalDateTime fechaCambio) { this.fechaCambio = fechaCambio; }
 
}
