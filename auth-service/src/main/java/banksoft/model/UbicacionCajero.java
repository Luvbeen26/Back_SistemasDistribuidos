package banksoft.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ubicacion_cajero")
public class UbicacionCajero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ubicacion")
    private Integer idUbicacion;

    @Column(name = "id_codigo_postal")
    private Integer idCodigoPostal;

    @Column(name = "calle", length = 50)
    private String calle;

    @Column(name = "numero_exterior", length = 10)
    private String numeroExterior;

    @Column(name = "descripcion", length = 45)
    private String descripcion;
 

    public UbicacionCajero() {}

    public UbicacionCajero(String calle, String numeroExterior) {
        this.calle = calle;
        this.numeroExterior = numeroExterior;
    }

    public Integer getIdUbicacion() { return idUbicacion; }
    public void setIdUbicacion(Integer idUbicacion) { this.idUbicacion = idUbicacion; }

    public Integer getIdCodigoPostal() { return idCodigoPostal; }
    public void setIdCodigoPostal(Integer idCodigoPostal) { this.idCodigoPostal = idCodigoPostal; }

    public String getCalle() { return calle; }
    public void setCalle(String calle) { this.calle = calle; }

    public String getNumeroExterior() { return numeroExterior; }
    public void setNumeroExterior(String numeroExterior) { this.numeroExterior = numeroExterior; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
 
}
