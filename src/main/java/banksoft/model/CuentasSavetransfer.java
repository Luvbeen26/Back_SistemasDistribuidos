package banksoft.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cuentas_savetransfer")
public class CuentasSavetransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_save")
    private Integer idSave;

    @Column(name = "id_cuenta", nullable = false)
    private Integer idCuenta;

    @Column(name = "id_destino", nullable = false)
    private Integer idDestino;

    @Column(name = "tipo", columnDefinition = "bpchar")
    private String tipo;
 

    public CuentasSavetransfer() {}

    public CuentasSavetransfer(Integer idCuenta, Integer idDestino) {
        this.idCuenta = idCuenta;
        this.idDestino = idDestino;
    }

    public Integer getIdSave() { return idSave; }
    public void setIdSave(Integer idSave) { this.idSave = idSave; }

    public Integer getIdCuenta() { return idCuenta; }
    public void setIdCuenta(Integer idCuenta) { this.idCuenta = idCuenta; }

    public Integer getIdDestino() { return idDestino; }
    public void setIdDestino(Integer idDestino) { this.idDestino = idDestino; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
 
}
