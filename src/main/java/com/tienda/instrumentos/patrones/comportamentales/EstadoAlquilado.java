package com.tienda.instrumentos.patrones.comportamentales;

import com.tienda.instrumentos.dominio.enums.DisponibilidadInstrumento;
import com.tienda.instrumentos.dominio.modelo.Instrumento;

/**
 * =====================================================================
 * PATRON COMPORTAMENTAL: STATE - Parte 4/5
 * =====================================================================
 * Estado: ALQUILADO
 * El instrumento esta en manos del cliente. Solo puede ser devuelto.
 */
public class EstadoAlquilado implements EstadoDisponibilidad {

    @Override
    public String getNombreEstado() {
        return "ALQUILADO";
    }

    @Override
    public void reservar(Instrumento instrumento) {
        throw new IllegalStateException(
            "No se puede reservar: el instrumento ya esta alquilado por otro cliente.");
    }

    @Override
    public void alquilar(Instrumento instrumento) {
        throw new IllegalStateException(
            "No se puede alquilar: el instrumento ya esta alquilado.");
    }

    @Override
    public void devolver(Instrumento instrumento) {
        // PERMITIDO: el cliente devuelve el instrumento
        instrumento.setDisponibilidad(DisponibilidadInstrumento.DISPONIBLE);
        System.out.println("Instrumento " + instrumento.getModelo()
            + " devuelto. Disponible nuevamente.");
    }

    @Override
    public void enviarAReparacion(Instrumento instrumento) {
        throw new IllegalStateException(
            "No se puede enviar a reparacion: esta alquilado actualmente.");
    }

    @Override
    public void terminarReparacion(Instrumento instrumento) {
        throw new IllegalStateException("No esta en reparacion actualmente.");
    }
}
