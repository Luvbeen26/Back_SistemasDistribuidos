package banksoft.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tarjetas_plastico")
public class TarjetasPlastico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarjeta")
    private Integer idTarjeta;

    @Column(name = "id_cuenta", nullable = false)
    private Integer idCuenta;

    @Column(name = "numero_tarjeta", nullable = false, length = 16)
    private String numeroTarjeta;

    @Column(name = "mes_expira", nullable = false)
    private Integer mesExpira;

    @Column(name = "anio_expira", nullable = false)
    private Integer anioExpira;

    @Column(name = "cvc", nullable = false, length = 3)
    private String cvc;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    @Column(name = "estatus", nullable = false, length = 20)
    private String estatus = "activa";

    @Column(name = "nip", length = 4)
    private String nip;
 

    public TarjetasPlastico() {}

    public TarjetasPlastico(Integer idCuenta, String numeroTarjeta, Integer mesExpira, Integer anioExpira, String cvc) {
        this.idCuenta = idCuenta;
        this.numeroTarjeta = numeroTarjeta;
        this.mesExpira = mesExpira;
        this.anioExpira = anioExpira;
        this.cvc = cvc;
        this.fechaEmision = LocalDate.now();
        this.estatus = "activa";
    }

    public Integer getIdTarjeta() { return idTarjeta; }
    public void setIdTarjeta(Integer idTarjeta) { this.idTarjeta = idTarjeta; }

    public Integer getIdCuenta() { return idCuenta; }
    public void setIdCuenta(Integer idCuenta) { this.idCuenta = idCuenta; }

    public String getNumeroTarjeta() { return numeroTarjeta; }
    public void setNumeroTarjeta(String numeroTarjeta) { this.numeroTarjeta = numeroTarjeta; }

    public Integer getMesExpira() { return mesExpira; }
    public void setMesExpira(Integer mesExpira) { this.mesExpira = mesExpira; }

    public Integer getAnioExpira() { return anioExpira; }
    public void setAnioExpira(Integer anioExpira) { this.anioExpira = anioExpira; }

    public String getCvc() { return cvc; }
    public void setCvc(String cvc) { this.cvc = cvc; }

    public LocalDate getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDate fechaEmision) { this.fechaEmision = fechaEmision; }

    public String getEstatus() { return estatus; }
    public void setEstatus(String estatus) { this.estatus = estatus; }

    public String getNip() { return nip; }
    public void setNip(String nip) { this.nip = nip; }
 
}
