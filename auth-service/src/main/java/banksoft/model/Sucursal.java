package banksoft.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "sucursal")
public class Sucursal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sucursal")
    private Integer idSucursal;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "telefono", nullable = false, length = 10)
    private String telefono;

    @Column(name = "propia_rentada", length = 1)
    private String propiaRentada;

    @Column(name = "fecha_construccion")
    private LocalDate fechaConstruccion;

    @Column(name = "metros_cuadrados", precision = 10, scale = 2)
    private BigDecimal metrosCuadrados;

    @Column(name = "clave_catastral", length = 20)
    private String claveCatastral;

    @Column(name = "calle", length = 45)
    private String calle;

    @Column(name = "numero", length = 45)
    private String numero;

    @Column(name = "id_codigo_postal")
    private Integer idCodigoPostal;

    @Column(name = "hora_apertura")
    private LocalTime horaApertura;

    @Column(name = "hora_cierre")
    private LocalTime horaCierre;

    @Column(name = "estatus", nullable = false, length = 1)
    private String estatus;

    @Column(name = "coordenada_latitud", length = 45)
    private String coordenadaLatitud;

    @Column(name = "coordenada_longitud", length = 45)
    private String coordenadaLongitud;

    @Column(name = "descripcion_ubicacion", length = 45)
    private String descripcionUbicacion;
 

    public Sucursal() {}

    public Sucursal(String nombre, String email, String telefono) {
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.estatus = "A";
    }

    public Integer getIdSucursal() { return idSucursal; }
    public void setIdSucursal(Integer idSucursal) { this.idSucursal = idSucursal; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getPropiaRentada() { return propiaRentada; }
    public void setPropiaRentada(String propiaRentada) { this.propiaRentada = propiaRentada; }

    public LocalDate getFechaConstruccion() { return fechaConstruccion; }
    public void setFechaConstruccion(LocalDate fechaConstruccion) { this.fechaConstruccion = fechaConstruccion; }

    public BigDecimal getMetrosCuadrados() { return metrosCuadrados; }
    public void setMetrosCuadrados(BigDecimal metrosCuadrados) { this.metrosCuadrados = metrosCuadrados; }

    public String getClaveCatastral() { return claveCatastral; }
    public void setClaveCatastral(String claveCatastral) { this.claveCatastral = claveCatastral; }

    public String getCalle() { return calle; }
    public void setCalle(String calle) { this.calle = calle; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public Integer getIdCodigoPostal() { return idCodigoPostal; }
    public void setIdCodigoPostal(Integer idCodigoPostal) { this.idCodigoPostal = idCodigoPostal; }

    public LocalTime getHoraApertura() { return horaApertura; }
    public void setHoraApertura(LocalTime horaApertura) { this.horaApertura = horaApertura; }

    public LocalTime getHoraCierre() { return horaCierre; }
    public void setHoraCierre(LocalTime horaCierre) { this.horaCierre = horaCierre; }

    public String getEstatus() { return estatus; }
    public void setEstatus(String estatus) { this.estatus = estatus; }

    public String getCoordenadaLatitud() { return coordenadaLatitud; }
    public void setCoordenadaLatitud(String coordenadaLatitud) { this.coordenadaLatitud = coordenadaLatitud; }

    public String getCoordenadaLongitud() { return coordenadaLongitud; }
    public void setCoordenadaLongitud(String coordenadaLongitud) { this.coordenadaLongitud = coordenadaLongitud; }

    public String getDescripcionUbicacion() { return descripcionUbicacion; }
    public void setDescripcionUbicacion(String descripcionUbicacion) { this.descripcionUbicacion = descripcionUbicacion; }
 
}
