package banksoft.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tipo_movimiento")
public class TipoMovimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo")
    private Integer idTipo;

    @Column(name = "descripcion", nullable = false, length = 50)
    private String descripcion;

    @Column(name = "action_sum_rest", length = 1)
    private String actionSumRest;
 

    public TipoMovimiento() {}

    public TipoMovimiento(String descripcion, String actionSumRest) {
        this.descripcion = descripcion;
        this.actionSumRest = actionSumRest;
    }

    public Integer getIdTipo() { return idTipo; }
    public void setIdTipo(Integer idTipo) { this.idTipo = idTipo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getActionSumRest() { return actionSumRest; }
    public void setActionSumRest(String actionSumRest) { this.actionSumRest = actionSumRest; }
 
}
