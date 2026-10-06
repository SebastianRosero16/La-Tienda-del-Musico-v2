package com.tienda.instrumentos.dominio.enums;

/**
 * TEMA: Fundamentos del Modelado de Dominio
 *
 * Clasifica el tipo de instrumento musical.
 * Aunque el enfoque es en guitarras, el sistema es extensible
 * a otros tipos de instrumentos (principio de diseño abierto/cerrado).
 */
public enum TipoInstrumento {
    GUITARRA_ACUSTICA("Guitarra Acustica"),
    GUITARRA_ELECTRICA("Guitarra Electrica"),
    GUITARRA_CLASICA("Guitarra Clasica"),
    BAJO("Bajo Electrico"),
    UKULELE("Ukulele");

    private final String nombre;

    TipoInstrumento(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
