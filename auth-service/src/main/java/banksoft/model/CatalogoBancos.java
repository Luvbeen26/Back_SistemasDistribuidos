package banksoft.model;

import jakarta.persistence.*;

@Entity
@Table(name = "catalogo_bancos")
public class CatalogoBancos {
    @Id
    @Column(name = "clave")
    private Integer clave;

    @Column(name = "nombre_corto", length = 20)
    private String nombreCorto;

    @Column(name = "razon_social", columnDefinition = "text")
    private String razonSocial;

    public CatalogoBancos() {}

    public Integer getClave() { return clave; }
    public void setClave(Integer clave) { this.clave = clave; }

    public String getNombreCorto() { return nombreCorto; }
    public void setNombreCorto(String nombreCorto) { this.nombreCorto = nombreCorto; }

    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }
}
