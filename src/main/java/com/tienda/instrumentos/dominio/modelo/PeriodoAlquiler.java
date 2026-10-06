package com.tienda.instrumentos.dominio.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * =====================================================================
 * PATRON: VALUE OBJECT (Objeto de Valor)
 * =====================================================================
 *
 * CONCEPTO:
 *   Un Value Object es un objeto que NO tiene identidad propia.
 *   No tiene @Id. Dos PeriodoAlquiler son iguales si sus fechas son iguales.
 *   Se diferencia de una ENTIDAD en que la entidad se identifica por su ID
 *   (un Cliente es unico por su ID aunque cambie su nombre),
 *   mientras que un Value Object se identifica por su CONTENIDO.
 *
 * POR QUE EXISTE:
 *   Sin PeriodoAlquiler, las fechas de inicio y devolucion vivirían sueltas
 *   en Reserva como dos atributos separados. La logica de validar fechas
 *   y calcular dias estaria dispersa.
 *   Con PeriodoAlquiler, ese comportamiento queda encapsulado en un solo lugar.
 *
 * @Embeddable: le dice a JPA que este objeto NO tiene tabla propia.
 *   Sus columnas se incrustan directamente en la tabla de la entidad
 *   que lo contenga (en este caso, la tabla "reservas").
 *   Eso es el ORM en accion: un objeto Java se mapea a columnas,
 *   no a una tabla separada.
 */
@Embeddable
public class PeriodoAlquiler {

    @Column(nullable = false)
    private LocalDate fechaInicio;

    @Column(nullable = false)
    private LocalDate fechaDevolucion;

    // Constructor vacio requerido por JPA/Hibernate
    protected PeriodoAlquiler() {}

    public PeriodoAlquiler(LocalDate fechaInicio, LocalDate fechaDevolucion) {
        if (fechaInicio == null || fechaDevolucion == null) {
            throw new IllegalArgumentException("Las fechas del periodo no pueden ser nulas");
        }
        if (!fechaDevolucion.isAfter(fechaInicio)) {
            throw new IllegalArgumentException(
                "La fecha de devolucion debe ser posterior a la fecha de inicio");
        }
        this.fechaInicio     = fechaInicio;
        this.fechaDevolucion = fechaDevolucion;
    }

    /**
     * Calcula cuantos dias dura el alquiler.
     * La logica esta encapsulada aqui: nadie mas necesita saber como se calcula.
     */
    public int calcularDias() {
        return (int) ChronoUnit.DAYS.between(fechaInicio, fechaDevolucion);
    }

    /**
     * Verifica si la devolucion esta vencida respecto a hoy.
     */
    public boolean estaVencido() {
        return LocalDate.now().isAfter(fechaDevolucion);
    }

    public LocalDate getFechaInicio()     { return fechaInicio; }
    public LocalDate getFechaDevolucion() { return fechaDevolucion; }

    @Override
    public String toString() {
        return fechaInicio + " -> " + fechaDevolucion + " (" + calcularDias() + " dias)";
    }

    // Dos PeriodoAlquiler son iguales si sus fechas son iguales (identidad por valor)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PeriodoAlquiler that)) return false;
        return fechaInicio.equals(that.fechaInicio) &&
               fechaDevolucion.equals(that.fechaDevolucion);
    }

    @Override
    public int hashCode() {
        return 31 * fechaInicio.hashCode() + fechaDevolucion.hashCode();
    }
}
