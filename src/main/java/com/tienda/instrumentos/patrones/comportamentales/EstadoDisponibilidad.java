package com.tienda.instrumentos.patrones.comportamentales;

import com.tienda.instrumentos.dominio.modelo.Instrumento;

/**
 * =====================================================================
 * PATRON COMPORTAMENTAL: STATE - Parte 1/5 (Interfaz del Estado)
 * =====================================================================
 *
 * PROBLEMA QUE RESUELVE:
 *   Un instrumento puede estar en 4 estados: DISPONIBLE, RESERVADO,
 *   ALQUILADO, EN_REPARACION. Segun el estado, las acciones permitidas
 *   cambian. Sin State, el servicio tendria ifs para cada combinacion.
 *
 * COMO FUNCIONA:
 *   Cada estado es una clase que sabe que acciones PUEDE y NO PUEDE hacer.
 *   El Instrumento delega al estado actual la decision.
 *
 * EN EL NEGOCIO:
 *   Si el instrumento esta ALQUILADO no se puede reservar.
 *   Si esta EN_REPARACION no se puede alquilar.
 *   Cada estado controla su propia logica.
 */
public interface EstadoDisponibilidad {

    // Nombre del estado para mostrar
    String getNombreEstado();

    // Acciones que puede o no hacer un instrumento segun su estado

    /**
     * Transicion: DISPONIBLE -> RESERVADO
     * Solo valida si el estado actual lo permite.
     */
    void reservar(Instrumento instrumento);

    /**
     * Transicion: RESERVADO -> ALQUILADO
     */
    void alquilar(Instrumento instrumento);

    /**
     * Transicion: ALQUILADO -> DISPONIBLE
     */
    void devolver(Instrumento instrumento);

    /**
     * Transicion: cualquier estado -> EN_REPARACION
     */
    void enviarAReparacion(Instrumento instrumento);

    /**
     * Transicion: EN_REPARACION -> DISPONIBLE
     */
    void terminarReparacion(Instrumento instrumento);
}
