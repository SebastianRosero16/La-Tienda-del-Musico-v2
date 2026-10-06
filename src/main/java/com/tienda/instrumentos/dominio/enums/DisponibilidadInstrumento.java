package com.tienda.instrumentos.dominio.enums;

/**
 * TEMA: Fundamentos del Modelado de Dominio + Patron State
 *
 * Este enum representa los estados de disponibilidad del instrumento
 * dentro del ciclo de vida del negocio.
 * Es la base del Patron State que implementaremos mas adelante.
 */
public enum DisponibilidadInstrumento {
    DISPONIBLE("Disponible para alquiler"),
    RESERVADO("Reservado por un cliente"),
    ALQUILADO("Actualmente alquilado"),
    EN_REPARACION("En reparacion, no disponible");

    private final String descripcion;

    DisponibilidadInstrumento(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
