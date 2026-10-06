package com.tienda.instrumentos.patrones.comportamentales;

import com.tienda.instrumentos.dominio.enums.DisponibilidadInstrumento;
import com.tienda.instrumentos.dominio.modelo.Instrumento;

/**
 * =====================================================================
 * PATRON COMPORTAMENTAL: STATE - Parte 3/5
 * =====================================================================
 * Estado: RESERVADO
 * El instrumento tiene una reserva activa: solo puede pasar a ALQUILADO
 * cuando el cliente llega a recogerlo.
 */
public class EstadoReservado implements EstadoDisponibilidad {

    @Override
    public String getNombreEstado() {
        return "RESERVADO";
    }

    @Override
    public void reservar(Instrumento instrumento) {
        // NO PERMITIDO: ya tiene una reserva activa
        throw new IllegalStateException(
            "El instrumento ya esta reservado. No se puede hacer otra reserva.");
    }

    @Override
    public void alquilar(Instrumento instrumento) {
        // PERMITIDO: el cliente llego a recoger su reserva
        instrumento.setDisponibilidad(DisponibilidadInstrumento.ALQUILADO);
        System.out.println("Instrumento " + instrumento.getModelo()
            + " entregado al cliente (pasa de reservado a alquilado).");
    }

    @Override
    public void devolver(Instrumento instrumento) {
        // CASO ESPECIAL: el cliente cancela la reserva antes de recogerlo
        instrumento.setDisponibilidad(DisponibilidadInstrumento.DISPONIBLE);
        System.out.println("Reserva cancelada. Instrumento " + instrumento.getModelo()
            + " disponible nuevamente.");
    }

    @Override
    public void enviarAReparacion(Instrumento instrumento) {
        throw new IllegalStateException(
            "No se puede enviar a reparacion: tiene una reserva activa. Cancele la reserva primero.");
    }

    @Override
    public void terminarReparacion(Instrumento instrumento) {
        throw new IllegalStateException("No esta en reparacion actualmente.");
    }
}
