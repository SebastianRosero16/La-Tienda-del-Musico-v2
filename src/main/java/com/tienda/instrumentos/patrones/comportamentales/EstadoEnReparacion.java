package com.tienda.instrumentos.patrones.comportamentales;

import com.tienda.instrumentos.dominio.enums.DisponibilidadInstrumento;
import com.tienda.instrumentos.dominio.modelo.Instrumento;

/**
 * =====================================================================
 * PATRON COMPORTAMENTAL: STATE - Parte 5/5
 * =====================================================================
 * Estado: EN_REPARACION
 * El instrumento esta en el taller. Solo puede terminar la reparacion.
 */
public class EstadoEnReparacion implements EstadoDisponibilidad {

    @Override
    public String getNombreEstado() {
        return "EN REPARACION";
    }

    @Override
    public void reservar(Instrumento instrumento) {
        throw new IllegalStateException(
            "No se puede reservar: el instrumento esta en reparacion.");
    }

    @Override
    public void alquilar(Instrumento instrumento) {
        throw new IllegalStateException(
            "No se puede alquilar: el instrumento esta en reparacion.");
    }

    @Override
    public void devolver(Instrumento instrumento) {
        throw new IllegalStateException(
            "No se puede devolver: el instrumento no esta alquilado, esta en reparacion.");
    }

    @Override
    public void enviarAReparacion(Instrumento instrumento) {
        throw new IllegalStateException("El instrumento ya esta en reparacion.");
    }

    @Override
    public void terminarReparacion(Instrumento instrumento) {
        // PERMITIDO: la reparacion termino, vuelve a estar disponible
        instrumento.setDisponibilidad(DisponibilidadInstrumento.DISPONIBLE);
        System.out.println("Reparacion finalizada. Instrumento " + instrumento.getModelo()
            + " disponible nuevamente.");
    }
}
