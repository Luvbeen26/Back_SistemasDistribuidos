package banksoft.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cuenta_bancaria")
public class CuentaBancaria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cuenta")
    private Integer idCuenta;

    @Column(name = "id_cliente")
    private Integer idCliente;

    @Column(name = "numero_cuenta", length = 16, unique = true)
    private String numeroCuenta;

    @Column(name = "id_tipo_cuenta", nullable = false)
    private Integer idTipoCuenta;

    @Column(name = "id_sucursal", nullable = false)
    private Integer idSucursal;

    @Column(name = "saldo", nullable = false, precision = 10, scale = 2)
    private BigDecimal saldo = BigDecimal.ZERO;

    @Column(name = "limite", precision = 10, scale = 2)
    private BigDecimal limite;

    @Column(name = "fecha_apertura", nullable = false)
    private LocalDate fechaApertura;

    @Column(name = "estatus", nullable = false, length = 1)
    private String estatus = "A";

    @Column(name = "clabe_interbancaria", length = 45)
    private String clabeInterbancaria;

    @Column(name = "es_mostrada")
    private Integer esMostrada;

    @Column(name = "bloq_hasta")
    private LocalDateTime bloqHasta;


    // Constructores
    public CuentaBancaria() {
        this.fechaApertura = LocalDate.now();
        this.saldo = BigDecimal.ZERO;
        this.estatus = "A";
    }

    public CuentaBancaria(Integer idCliente, String numeroCuenta, Integer idTipoCuenta, Integer idSucursal) {
        this.idCliente = idCliente;
        this.numeroCuenta = numeroCuenta;
        this.idTipoCuenta = idTipoCuenta;
        this.idSucursal = idSucursal;
        this.fechaApertura = LocalDate.now();
        this.saldo = BigDecimal.ZERO;
        this.estatus = "A";
    }

    // Getters y Setters
    public Integer getIdCuenta() { return idCuenta; }
    public void setIdCuenta(Integer idCuenta) { this.idCuenta = idCuenta; }

    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }

    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }

    public Integer getIdTipoCuenta() { return idTipoCuenta; }
    public void setIdTipoCuenta(Integer idTipoCuenta) { this.idTipoCuenta = idTipoCuenta; }

    public Integer getIdSucursal() { return idSucursal; }
    public void setIdSucursal(Integer idSucursal) { this.idSucursal = idSucursal; }

    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }

    public BigDecimal getLimite() { return limite; }
    public void setLimite(BigDecimal limite) { this.limite = limite; }

    public LocalDate getFechaApertura() { return fechaApertura; }
    public void setFechaApertura(LocalDate fechaApertura) { this.fechaApertura = fechaApertura; }

    public String getEstatus() { return estatus; }
    public void setEstatus(String estatus) { this.estatus = estatus; }

    public String getClabeInterbancaria() { return clabeInterbancaria; }
    public void setClabeInterbancaria(String clabeInterbancaria) { this.clabeInterbancaria = clabeInterbancaria; }

    public Integer getEsMostrada() { return esMostrada; }
    public void setEsMostrada(Integer esMostrada) { this.esMostrada = esMostrada; }

    public LocalDateTime getBloqHasta() { return bloqHasta; }
    public void setBloqHasta(LocalDateTime bloqHasta) { this.bloqHasta = bloqHasta; }

    }
