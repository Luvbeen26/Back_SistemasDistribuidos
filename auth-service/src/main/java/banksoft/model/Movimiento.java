package banksoft.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimiento")
public class Movimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento")
    private Integer idMovimiento;

    @Column(name = "id_cuenta", nullable = false)
    private Integer idCuenta;

    @Column(name = "id_tipo_movimiento", nullable = false)
    private Integer idTipoMovimiento;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "id_sucursal")
    private Integer idSucursal;

    @Column(name = "importe", nullable = false, precision = 12, scale = 2)
    private BigDecimal importe;

    @Column(name = "concepto", length = 100)
    private String concepto;

    @Column(name = "numero_autorizacion", nullable = false, length = 6)
    private String numeroAutorizacion;

    @Column(name = "referencia_numerica", nullable = false, length = 45)
    private String referenciaNumerica;

    @Column(name = "referencia_alfanumerica", nullable = false, length = 45)
    private String referenciaAlfanumerica;

    @Column(name = "id_empleado")
    private Integer idEmpleado;

    @Column(name = "id_cajero_atm")
    private Integer idCajeroAtm;

    @Column(name = "clabe_interbancaria", length = 18)
    private String clabeInterbancaria;

    @Column(name = "estatus", nullable = false, length = 20)
    private String estatus;

    public Movimiento() {
        this.fechaHora = LocalDateTime.now();
        this.estatus = "completado";
    }

    public Integer getIdMovimiento() { return idMovimiento; }
    public void setIdMovimiento(Integer idMovimiento) { this.idMovimiento = idMovimiento; }

    public Integer getIdCuenta() { return idCuenta; }
    public void setIdCuenta(Integer idCuenta) { this.idCuenta = idCuenta; }

    public Integer getIdTipoMovimiento() { return idTipoMovimiento; }
    public void setIdTipoMovimiento(Integer idTipoMovimiento) { this.idTipoMovimiento = idTipoMovimiento; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public Integer getIdSucursal() { return idSucursal; }
    public void setIdSucursal(Integer idSucursal) { this.idSucursal = idSucursal; }

    public BigDecimal getImporte() { return importe; }
    public void setImporte(BigDecimal importe) { this.importe = importe; }

    public String getConcepto() { return concepto; }
    public void setConcepto(String concepto) { this.concepto = concepto; }

    public String getNumeroAutorizacion() { return numeroAutorizacion; }
    public void setNumeroAutorizacion(String numeroAutorizacion) { this.numeroAutorizacion = numeroAutorizacion; }

    public String getReferenciaNumerica() { return referenciaNumerica; }
    public void setReferenciaNumerica(String referenciaNumerica) { this.referenciaNumerica = referenciaNumerica; }

    public String getReferenciaAlfanumerica() { return referenciaAlfanumerica; }
    public void setReferenciaAlfanumerica(String referenciaAlfanumerica) { this.referenciaAlfanumerica = referenciaAlfanumerica; }

    public Integer getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(Integer idEmpleado) { this.idEmpleado = idEmpleado; }

    public Integer getIdCajeroAtm() { return idCajeroAtm; }
    public void setIdCajeroAtm(Integer idCajeroAtm) { this.idCajeroAtm = idCajeroAtm; }

    public String getClabeInterbancaria() { return clabeInterbancaria; }
    public void setClabeInterbancaria(String clabeInterbancaria) { this.clabeInterbancaria = clabeInterbancaria; }

    public String getEstatus() { return estatus; }
    public void setEstatus(String estatus) { this.estatus = estatus; }
}
