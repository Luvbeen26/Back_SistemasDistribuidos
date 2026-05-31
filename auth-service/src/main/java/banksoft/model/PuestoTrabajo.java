package banksoft.model;

import jakarta.persistence.*;

@Entity
@Table(name = "puesto_trabajo")
public class PuestoTrabajo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_puesto")
    private Integer idPuesto;

    @Column(name = "id_departamento")
    private Integer idDepartamento;

    @Column(name = "nombre_puesto", length = 50)
    private String nombrePuesto;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "estatus", length = 1)
    private String estatus;
 

    public PuestoTrabajo() {}

    public PuestoTrabajo(String nombrePuesto, Integer idDepartamento) {
        this.nombrePuesto = nombrePuesto;
        this.idDepartamento = idDepartamento;
    }

    public Integer getIdPuesto() { return idPuesto; }
    public void setIdPuesto(Integer idPuesto) { this.idPuesto = idPuesto; }

    public Integer getIdDepartamento() { return idDepartamento; }
    public void setIdDepartamento(Integer idDepartamento) { this.idDepartamento = idDepartamento; }

    public String getNombrePuesto() { return nombrePuesto; }
    public void setNombrePuesto(String nombrePuesto) { this.nombrePuesto = nombrePuesto; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstatus() { return estatus; }
    public void setEstatus(String estatus) { this.estatus = estatus; }
 
}
