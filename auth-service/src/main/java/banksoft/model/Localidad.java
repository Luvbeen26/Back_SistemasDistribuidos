package banksoft.model;

import jakarta.persistence.*;

@Entity
@Table(name = "localidad")
public class Localidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_estado", nullable = false)
    private Integer idEstado;

    @Column(name = "id_municipio", nullable = false)
    private Integer idMunicipio;

    @Column(name = "id_localidad", nullable = false)
    private Integer idLocalidad;

    @Column(name = "nombre", length = 255)
    private String nombre;

    @Column(name = "poblacion total")
    private Integer poblacionTotal;
 

    public Localidad() {}

    public Localidad(Integer idEstado, Integer idMunicipio, Integer idLocalidad, String nombre) {
        this.idEstado = idEstado;
        this.idMunicipio = idMunicipio;
        this.idLocalidad = idLocalidad;
        this.nombre = nombre;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getIdEstado() { return idEstado; }
    public void setIdEstado(Integer idEstado) { this.idEstado = idEstado; }

    public Integer getIdMunicipio() { return idMunicipio; }
    public void setIdMunicipio(Integer idMunicipio) { this.idMunicipio = idMunicipio; }

    public Integer getIdLocalidad() { return idLocalidad; }
    public void setIdLocalidad(Integer idLocalidad) { this.idLocalidad = idLocalidad; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getPoblacionTotal() { return poblacionTotal; }
    public void setPoblacionTotal(Integer poblacionTotal) { this.poblacionTotal = poblacionTotal; }
 
}
