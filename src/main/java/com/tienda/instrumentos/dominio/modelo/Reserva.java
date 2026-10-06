package com.tienda.instrumentos.dominio.modelo;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

/**
 * =====================================================================
 * TEMAS CUBIERTOS:
 *   - Fundamentos del Modelado de Dominio: Reserva conecta Cliente
 *     con Instrumento en el tiempo.
 *   - Encapsulamiento: atributos privados, logica interna.
 *   - UUID: identificador tecnico unico e independiente de la BD.
 *   - Value Object: PeriodoAlquiler agrupa y valida las fechas.
 *     Las columnas fecha_inicio y fecha_devolucion son de Reserva
 *     pero su logica vive encapsulada en PeriodoAlquiler.
 *   - ORM: @Embedded le dice a Hibernate que incruste las columnas
 *     de PeriodoAlquiler directamente en la tabla "reservas".
 * =====================================================================
 */
@Entity
@Table(name = "reservas")
public class Reserva {

    // UUID como clave tecnica: no depende del autoincremento de la BD
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    // Relacion con el cliente que hace la reserva
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // Relacion con el instrumento reservado
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrumento_id", nullable = false)
    private Instrumento instrumento;

    // VALUE OBJECT: PeriodoAlquiler agrupa fechaInicio y fechaDevolucion.
    // @Embedded le dice a JPA/Hibernate que sus columnas se guardan
    // directamente en la tabla "reservas", sin crear una tabla separada.
    // Esto es el ORM decidiendo como mapear un objeto Java a columnas SQL.
    @Embedded
    private PeriodoAlquiler periodo;

    @Column(nullable = false)
    private double precioTotal;

    // Estado de la reserva
    @Column(nullable = false)
    private boolean activa;

    // Estado del instrumento al momento de entregarlo
    @Column
    private String estadoAlMomentoDeEntrega;

    // Estado del instrumento cuando es devuelto
    @Column
    private String estadoAlMomentoDeDevolucion;

    // Constructor vacio requerido por JPA
    public Reserva() {}

    public Reserva(Cliente cliente, Instrumento instrumento,
                   LocalDate fechaInicio, LocalDate fechaDevolucion) {
        this.cliente     = cliente;
        this.instrumento = instrumento;

        // El Value Object valida las fechas internamente
        this.periodo = new PeriodoAlquiler(fechaInicio, fechaDevolucion);

        this.activa = true;

        // Calculamos el precio total usando los dias del Value Object
        int dias = this.periodo.calcularDias();
        this.precioTotal = instrumento.calcularPrecioAlquiler(dias);

        // Guardamos el estado actual del instrumento al momento de entregarlo
        this.estadoAlMomentoDeEntrega = instrumento.getEstado().getDescripcion();
    }

    // =========================================================
    // ENCAPSULAMIENTO DE REGLAS DE NEGOCIO
    // =========================================================

    /**
     * Delega el calculo de dias al Value Object PeriodoAlquiler.
     * Quien use Reserva no necesita conocer como se calculan los dias.
     */
    public int calcularDias() {
        return periodo.calcularDias();
    }

    public boolean estaActiva() {
        return this.activa;
    }

    /**
     * Verifica si el instrumento fue devuelto tarde.
     * Delega la logica de fechas al Value Object.
     */
    public boolean estaVencida() {
        return activa && periodo.estaVencido();
    }

    /**
     * Finaliza la reserva cuando el cliente devuelve el instrumento.
     */
    public void finalizarReserva(String estadoAlDevolver) {
        this.activa = false;
        this.estadoAlMomentoDeDevolucion = estadoAlDevolver;
    }

    // GETTERS
    public UUID getId()                            { return id; }
    public Cliente getCliente()                    { return cliente; }
    public Instrumento getInstrumento()            { return instrumento; }
    public PeriodoAlquiler getPeriodo()            { return periodo; }
    public LocalDate getFechaInicio()              { return periodo.getFechaInicio(); }
    public LocalDate getFechaDevolucion()          { return periodo.getFechaDevolucion(); }
    public double getPrecioTotal()                 { return precioTotal; }
    public String getEstadoAlMomentoDeEntrega()    { return estadoAlMomentoDeEntrega; }
    public String getEstadoAlMomentoDeDevolucion() { return estadoAlMomentoDeDevolucion; }

    public void setActiva(boolean activa) { this.activa = activa; }

    @Override
    public String toString() {
        return String.format("Reserva #%s | %s | %s | %d dias | Total: $%.0f",
                id, cliente.getNombre(), periodo,
                calcularDias(), precioTotal);
    }
}
