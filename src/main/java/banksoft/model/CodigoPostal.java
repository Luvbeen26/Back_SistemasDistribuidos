package banksoft.model;

import jakarta.persistence.*;

@Entity
@Table(name = "codigo_postal")
public class CodigoPostal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_codigo_postal")
    private Integer idCodigoPostal;

    @Column(name = "id_estado", nullable = false)
    private Integer idEstado;

    @Column(name = "id_municipio", nullable = false)
    private Integer idMunicipio;

    @Column(name = "ciudad", length = 255)
    private String ciudad;

    @Column(name = "zona", nullable = false, length = 50)
    private String zona;

    @Column(name = "cp", nullable = false)
    private Integer cp;

    @Column(name = "colonia", nullable = false, length = 255)
    private String colonia;

    @Column(name = "tipo", nullable = false, length = 255)
    private String tipo;



    public CodigoPostal() {}

    public CodigoPostal(Integer idEstado, Integer idMunicipio, Integer cp, String colonia, String tipo) {
        this.idEstado = idEstado;
        this.idMunicipio = idMunicipio;
        this.cp = cp;
        this.colonia = colonia;
        this.tipo = tipo;
    }

    public Integer getIdCodigoPostal() { return idCodigoPostal; }
    public void setIdCodigoPostal(Integer idCodigoPostal) { this.idCodigoPostal = idCodigoPostal; }

    public Integer getIdEstado() { return idEstado; }
    public void setIdEstado(Integer idEstado) { this.idEstado = idEstado; }

    public Integer getIdMunicipio() { return idMunicipio; }
    public void setIdMunicipio(Integer idMunicipio) { this.idMunicipio = idMunicipio; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getZona() { return zona; }
    public void setZona(String zona) { this.zona = zona; }

    public Integer getCp() { return cp; }
    public void setCp(Integer cp) { this.cp = cp; }

    public String getColonia() { return colonia; }
    public void setColonia(String colonia) { this.colonia = colonia; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }


}
