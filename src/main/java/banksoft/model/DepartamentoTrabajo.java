package banksoft.model;

import jakarta.persistence.*;

@Entity
@Table(name = "departamento_trabajo")
public class DepartamentoTrabajo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_departamento")
    private Integer idDepartamento;

    @Column(name = "nombre_departamento", length = 20)
    private String nombreDepartamento;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "estatus", length = 1)
    private String estatus;
    
 

    public DepartamentoTrabajo() {}

    public DepartamentoTrabajo(String nombreDepartamento) {
        this.nombreDepartamento = nombreDepartamento;
    }

    public Integer getIdDepartamento() { return idDepartamento; }
    public void setIdDepartamento(Integer idDepartamento) { this.idDepartamento = idDepartamento; }

    public String getNombreDepartamento() { return nombreDepartamento; }
    public void setNombreDepartamento(String nombreDepartamento) { this.nombreDepartamento = nombreDepartamento; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstatus() { return estatus; }
    public void setEstatus(String estatus) { this.estatus = estatus; }
 
}
