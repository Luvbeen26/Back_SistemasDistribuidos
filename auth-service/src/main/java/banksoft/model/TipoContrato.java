package banksoft.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tipo_contrato")
public class TipoContrato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contrato")
    private Integer idContrato;

    @Column(name = "nombre_contrato", nullable = false, length = 50)
    private String nombreContrato;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "estatus", length = 1)
    private String estatus;
 

    public TipoContrato() {}

    public TipoContrato(String nombreContrato) {
        this.nombreContrato = nombreContrato;
    }

    public Integer getIdContrato() { return idContrato; }
    public void setIdContrato(Integer idContrato) { this.idContrato = idContrato; }

    public String getNombreContrato() { return nombreContrato; }
    public void setNombreContrato(String nombreContrato) { this.nombreContrato = nombreContrato; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstatus() { return estatus; }
    public void setEstatus(String estatus) { this.estatus = estatus; }
 
}
