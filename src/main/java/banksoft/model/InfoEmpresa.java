package banksoft.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "info_empresa")
public class InfoEmpresa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "razon_social", length = 100)
    private String razonSocial;

    @Column(name = "rfc", length = 12)
    private String rfc;

    @Column(name = "id_estado")
    private Integer idEstado;

    @Column(name = "id_municipio")
    private Integer idMunicipio;

    @Column(name = "id_codigo_postal")
    private Integer idCodigoPostal;

    @Column(name = "ciudad", length = 30)
    private String ciudad;

    @Column(name = "colonia", length = 30)
    private String colonia;

    @Column(name = "calle", length = 50)
    private String calle;

    @Column(name = "numero_exterior", length = 10)
    private String numeroExterior;

    @Column(name = "pais", length = 30)
    private String pais;

    @Column(name = "telefono", length = 10)
    private String telefono;

    @Column(name = "email_contacto", length = 100)
    private String emailContacto;

    @Column(name = "sitio_web", length = 100)
    private String sitioWeb;

    @Column(name = "fecha_fundacion")
    private LocalDate fechaFundacion;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "logo", length = 100)
    private String logo;

    @Column(name = "registro_patronal", length = 45)
    private String registroPatronal;

    @Column(name = "estatus", length = 1)
    private String estatus;

    @Column(name = "salario_minimo", precision = 10, scale = 2)
    private BigDecimal salarioMinimo;

    @Column(name = "clave_autorizacion", length = 45)
    private String claveAutorizacion;

    @Column(name = "slogan", length = 45)
    private String slogan;

    @Column(name = "tipo_cambio_compra", precision = 10, scale = 2)
    private BigDecimal tipoCambioCompra;

    @Column(name = "tipo_cambio_venta", precision = 10, scale = 2)
    private BigDecimal tipoCambioVenta;
 

    public InfoEmpresa() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }

    public String getRfc() { return rfc; }
    public void setRfc(String rfc) { this.rfc = rfc; }

    public Integer getIdEstado() { return idEstado; }
    public void setIdEstado(Integer idEstado) { this.idEstado = idEstado; }

    public Integer getIdMunicipio() { return idMunicipio; }
    public void setIdMunicipio(Integer idMunicipio) { this.idMunicipio = idMunicipio; }

    public Integer getIdCodigoPostal() { return idCodigoPostal; }
    public void setIdCodigoPostal(Integer idCodigoPostal) { this.idCodigoPostal = idCodigoPostal; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getColonia() { return colonia; }
    public void setColonia(String colonia) { this.colonia = colonia; }

    public String getCalle() { return calle; }
    public void setCalle(String calle) { this.calle = calle; }

    public String getNumeroExterior() { return numeroExterior; }
    public void setNumeroExterior(String numeroExterior) { this.numeroExterior = numeroExterior; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmailContacto() { return emailContacto; }
    public void setEmailContacto(String emailContacto) { this.emailContacto = emailContacto; }

    public String getSitioWeb() { return sitioWeb; }
    public void setSitioWeb(String sitioWeb) { this.sitioWeb = sitioWeb; }

    public LocalDate getFechaFundacion() { return fechaFundacion; }
    public void setFechaFundacion(LocalDate fechaFundacion) { this.fechaFundacion = fechaFundacion; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }

    public String getRegistroPatronal() { return registroPatronal; }
    public void setRegistroPatronal(String registroPatronal) { this.registroPatronal = registroPatronal; }

    public String getEstatus() { return estatus; }
    public void setEstatus(String estatus) { this.estatus = estatus; }

    public BigDecimal getSalarioMinimo() { return salarioMinimo; }
    public void setSalarioMinimo(BigDecimal salarioMinimo) { this.salarioMinimo = salarioMinimo; }

    public String getClaveAutorizacion() { return claveAutorizacion; }
    public void setClaveAutorizacion(String claveAutorizacion) { this.claveAutorizacion = claveAutorizacion; }

    public String getSlogan() { return slogan; }
    public void setSlogan(String slogan) { this.slogan = slogan; }

    public BigDecimal getTipoCambioCompra() { return tipoCambioCompra; }
    public void setTipoCambioCompra(BigDecimal tipoCambioCompra) { this.tipoCambioCompra = tipoCambioCompra; }

    public BigDecimal getTipoCambioVenta() { return tipoCambioVenta; }
    public void setTipoCambioVenta(BigDecimal tipoCambioVenta) { this.tipoCambioVenta = tipoCambioVenta; }
 
}
