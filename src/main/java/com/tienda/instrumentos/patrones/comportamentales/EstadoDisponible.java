package com.tienda.instrumentos.patrones.comportamentales;

import com.tienda.instrumentos.dominio.enums.DisponibilidadInstrumento;
import com.tienda.instrumentos.dominio.modelo.Instrumento;

/**
 * =====================================================================
 * PATRON COMPORTAMENTAL: STATE - Parte 2/5
 * =====================================================================
 * Estado: DISPONIBLE
 * El instrumento esta libre para reservar o alquilar directamente.
 */
public class EstadoDisponible implements EstadoDisponibilidad {

    @Override
    public String getNombreEstado() {
        return "DISPONIBLE";
    }

    @Override
    public void reservar(Instrumento instrumento) {
        // PERMITIDO: disponible -> reservado
        instrumento.setDisponibilidad(DisponibilidadInstrumento.RESERVADO);
        System.out.println("Instrumento " + instrumento.getModelo() + " reservado exitosamente.");
    }

    @Override
    public void alquilar(Instrumento instrumento) {
        // PERMITIDO: disponible -> alquilado (alquiler directo sin reserva previa)
        instrumento.setDisponibilidad(DisponibilidadInstrumento.ALQUILADO);
        System.out.println("Instrumento " + instrumento.getModelo() + " alquilado directamente.");
    }

    @Override
    public void devolver(Instrumento instrumento) {
        // NO TIENE SENTIDO: ya esta disponible
        throw new IllegalStateException("No se puede devolver: el instrumento ya esta disponible.");
    }

    @Override
    public void enviarAReparacion(Instrumento instrumento) {
        // PERMITIDO: el dueno puede mandarlo a reparar en cualquier momento
        instrumento.setDisponibilidad(DisponibilidadInstrumento.EN_REPARACION);
        System.out.println("Instrumento " + instrumento.getModelo() + " enviado a reparacion.");
    }

    @Override
    public void terminarReparacion(Instrumento instrumento) {
        throw new IllegalStateException("No esta en reparacion actualmente.");
    }
}
