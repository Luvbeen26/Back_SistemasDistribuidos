package banksoft.model;

import jakarta.persistence.*;

@Entity
@Table(name = "estado")
public class Estado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    private Integer idEstado;

    @Column(name = "nombre_estado", length = 50)
    private String nombreEstado;

    @Column(name = "nom_abr", length = 10)
    private String nomAbr;

    @Column(name = "pob_total")
    private Integer pobTotal;

    @Column(name = "pob_masculina")
    private Integer pobMasculina;

    @Column(name = "pob_femenina")
    private Integer pobFemenina;

    @Column(name = "total_viviendad_habitadas")
    private Integer totalViviendasHabitadas;
 

    public Estado() {}

    public Estado(String nombreEstado, String nomAbr) {
        this.nombreEstado = nombreEstado;
        this.nomAbr = nomAbr;
    }

    public Integer getIdEstado() { return idEstado; }
    public void setIdEstado(Integer idEstado) { this.idEstado = idEstado; }

    public String getNombreEstado() { return nombreEstado; }
    public void setNombreEstado(String nombreEstado) { this.nombreEstado = nombreEstado; }

    public String getNomAbr() { return nomAbr; }
    public void setNomAbr(String nomAbr) { this.nomAbr = nomAbr; }

    public Integer getPobTotal() { return pobTotal; }
    public void setPobTotal(Integer pobTotal) { this.pobTotal = pobTotal; }

    public Integer getPobMasculina() { return pobMasculina; }
    public void setPobMasculina(Integer pobMasculina) { this.pobMasculina = pobMasculina; }

    public Integer getPobFemenina() { return pobFemenina; }
    public void setPobFemenina(Integer pobFemenina) { this.pobFemenina = pobFemenina; }

    public Integer getTotalViviendasHabitadas() { return totalViviendasHabitadas; }
    public void setTotalViviendasHabitadas(Integer totalViviendasHabitadas) { this.totalViviendasHabitadas = totalViviendasHabitadas; }
 
}
