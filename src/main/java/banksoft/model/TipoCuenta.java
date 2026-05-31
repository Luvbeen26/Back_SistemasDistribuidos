package banksoft.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tipo_cuenta")
public class TipoCuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_cuenta")
    private Integer idTipoCuenta;

    @Column(name = "descripcion", nullable = false, length = 50)
    private String descripcion;

    @Column(name = "estatus", nullable = false, length = 1)
    private String estatus;

    @Column(name = "tasa_interes", precision = 5, scale = 2)
    private BigDecimal tasaInteres;
 

    public TipoCuenta() {}

    public TipoCuenta(String descripcion, String estatus) {
        this.descripcion = descripcion;
        this.estatus = estatus;
    }

    public Integer getIdTipoCuenta() { return idTipoCuenta; }
    public void setIdTipoCuenta(Integer idTipoCuenta) { this.idTipoCuenta = idTipoCuenta; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstatus() { return estatus; }
    public void setEstatus(String estatus) { this.estatus = estatus; }

    public BigDecimal getTasaInteres() { return tasaInteres; }
    public void setTasaInteres(BigDecimal tasaInteres) { this.tasaInteres = tasaInteres; }
 
}
