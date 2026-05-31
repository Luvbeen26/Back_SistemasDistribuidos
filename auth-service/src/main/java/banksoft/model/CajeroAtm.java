package banksoft.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
@Table(name = "cajero_atm")
public class CajeroAtm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cajero")
    private Integer idCajero;

    @Column(name = "id_sucursal")
    private Integer idSucursal;

    @Column(name = "id_ubicacion")
    private Integer idUbicacion;

    @Column(name = "coordenada_latitud", precision = 9, scale = 6)
    private BigDecimal coordenadaLatitud;

    @Column(name = "coordenada_longitud", precision = 9, scale = 6)
    private BigDecimal coordenadaLongitud;

    @Column(name = "descripcion_ubicacion")
    private String descripcionUbicacion;

    @Column(name = "fecha_instalacion")
    private LocalDate fechaInstalacion;

    @Column(name = "fecha_ultimo_mantenimiento")
    private LocalDate fechaUltimoMantenimiento;

    @Column(name = "estatus", length = 1) //A = Activo, I = Inactivo, M = Mantenimiento
    private String estatus;

    @Column(name = "nombre", length = 45)
    private String nombre;


    public CajeroAtm() {}

    public CajeroAtm(Integer idSucursal, Integer idUbicacion, String nombre) {
        this.idSucursal = idSucursal;
        this.idUbicacion = idUbicacion;
        this.nombre = nombre;
        this.fechaInstalacion = LocalDate.now();
    }

    // Getters y Setters
    public Integer getIdCajero() { return idCajero; }
    public void setIdCajero(Integer idCajero) { this.idCajero = idCajero; }

    public Integer getIdSucursal() { return idSucursal; }
    public void setIdSucursal(Integer idSucursal) { this.idSucursal = idSucursal; }

    public Integer getIdUbicacion() { return idUbicacion; }
    public void setIdUbicacion(Integer idUbicacion) { this.idUbicacion = idUbicacion; }

    public BigDecimal getCoordenadaLatitud() { return coordenadaLatitud; }
    public void setCoordenadaLatitud(BigDecimal coordenadaLatitud) { this.coordenadaLatitud = coordenadaLatitud; }

    public BigDecimal getCoordenadaLongitud() { return coordenadaLongitud; }
    public void setCoordenadaLongitud(BigDecimal coordenadaLongitud) { this.coordenadaLongitud = coordenadaLongitud; }

    public String getDescripcionUbicacion() { return descripcionUbicacion; }
    public void setDescripcionUbicacion(String descripcionUbicacion) { this.descripcionUbicacion = descripcionUbicacion; }

    public LocalDate getFechaInstalacion() { return fechaInstalacion; }
    public void setFechaInstalacion(LocalDate fechaInstalacion) { this.fechaInstalacion = fechaInstalacion; }

    public LocalDate getFechaUltimoMantenimiento() { return fechaUltimoMantenimiento; }
    public void setFechaUltimoMantenimiento(LocalDate fechaUltimoMantenimiento) { this.fechaUltimoMantenimiento = fechaUltimoMantenimiento; }

    public String getEstatus() { return estatus; }
    public void setEstatus(String estatus) { this.estatus = estatus; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }


}
