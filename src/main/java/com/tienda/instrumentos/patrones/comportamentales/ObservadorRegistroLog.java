package com.tienda.instrumentos.patrones.comportamentales;

import com.tienda.instrumentos.dominio.modelo.Reserva;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * =====================================================================
 * PATRON COMPORTAMENTAL: OBSERVER - Parte 3/4
 * =====================================================================
 * Observador concreto: registra en consola cada evento importante.
 *
 * En un sistema real esto escribiria a una base de datos de auditoria
 * o a un sistema de logs como Logback/Log4j.
 * Aqui lo mostramos en consola para que sea visible en la exposicion.
 */
public class ObservadorRegistroLog implements ObservadorReserva {

    private static final DateTimeFormatter FORMATO =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Override
    public void alCrearReserva(Reserva reserva) {
        System.out.println(String.format(
            "[LOG %s] NUEVA RESERVA | Reserva #%s | Cliente: %s | " +
            "Instrumento: %s %s | Dias: %d | Total: $%.0f",
            ahora(), reserva.getId(),
            reserva.getCliente().getNombre(),
            reserva.getInstrumento().getMarca(),
            reserva.getInstrumento().getModelo(),
            reserva.calcularDias(),
            reserva.getPrecioTotal()
        ));
    }

    @Override
    public void alCancelarReserva(Reserva reserva) {
        System.out.println(String.format(
            "[LOG %s] RESERVA CANCELADA | Reserva #%s | Cliente: %s",
            ahora(), reserva.getId(), reserva.getCliente().getNombre()
        ));
    }

    @Override
    public void alDevolverInstrumento(Reserva reserva) {
        System.out.println(String.format(
            "[LOG %s] DEVOLUCION | Reserva #%s | Instrumento: %s %s | " +
            "Estado devolucion: %s",
            ahora(), reserva.getId(),
            reserva.getInstrumento().getMarca(),
            reserva.getInstrumento().getModelo(),
            reserva.getEstadoAlMomentoDeDevolucion()
        ));
    }

    private String ahora() {
        return LocalDateTime.now().format(FORMATO);
    }
}
