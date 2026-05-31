package banksoft.model;

import jakarta.persistence.*;

@Entity
@Table(name = "municipio")
public class Municipio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_municipio")
    private Integer idMunicipio;

    @Column(name = "id_estado", nullable = false)
    private Integer idEstado;

    @Column(name = "nombre_municipio")
    private String nombreMunicipio;

    @Column(name = "pob_total")
    private Integer pobTotal;

    @Column(name = "pob_masculina")
    private Integer pobMasculina;

    @Column(name = "pob_femenina")
    private Integer pobFemenina;
 

    public Municipio() {}

    public Municipio(Integer idEstado, String nombreMunicipio) {
        this.idEstado = idEstado;
        this.nombreMunicipio = nombreMunicipio;
    }

    public Integer getIdMunicipio() { return idMunicipio; }
    public void setIdMunicipio(Integer idMunicipio) { this.idMunicipio = idMunicipio; }

    public Integer getIdEstado() { return idEstado; }
    public void setIdEstado(Integer idEstado) { this.idEstado = idEstado; }

    public String getNombreMunicipio() { return nombreMunicipio; }
    public void setNombreMunicipio(String nombreMunicipio) { this.nombreMunicipio = nombreMunicipio; }

    public Integer getPobTotal() { return pobTotal; }
    public void setPobTotal(Integer pobTotal) { this.pobTotal = pobTotal; }

    public Integer getPobMasculina() { return pobMasculina; }
    public void setPobMasculina(Integer pobMasculina) { this.pobMasculina = pobMasculina; }

    public Integer getPobFemenina() { return pobFemenina; }
    public void setPobFemenina(Integer pobFemenina) { this.pobFemenina = pobFemenina; }
 
}
