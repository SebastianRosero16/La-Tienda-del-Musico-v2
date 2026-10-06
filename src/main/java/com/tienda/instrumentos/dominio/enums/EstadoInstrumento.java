package com.tienda.instrumentos.dominio.enums;

/**
 * TEMA: Fundamentos del Modelado de Dominio + Encapsulamiento de Reglas de Negocio
 *
 * Este enum representa los posibles estados fisicos de un instrumento.
 * Al usar un enum, garantizamos que nadie pueda inventarse un estado
 * que no exista en el sistema (por ejemplo "Rotisserie" no tendria sentido).
 */
public enum EstadoInstrumento {
    NUEVO("Nuevo - Sin uso previo"),
    USADO("Usado - Con desgaste normal"),
    RAYADO("Rayado - Tiene marcas visibles"),
    DANADO("Dañado - Requiere revision");

    // Descripcion legible para mostrar al usuario
    private final String descripcion;

    EstadoInstrumento(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
