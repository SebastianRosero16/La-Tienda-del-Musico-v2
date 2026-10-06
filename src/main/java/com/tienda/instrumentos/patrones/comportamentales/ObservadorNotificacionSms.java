package com.tienda.instrumentos.patrones.comportamentales;

import com.tienda.instrumentos.dominio.modelo.Reserva;
import com.tienda.instrumentos.patrones.estructurales.AdaptadorSms;
import com.tienda.instrumentos.patrones.estructurales.NotificadorExterno;

/**
 * =====================================================================
 * PATRON COMPORTAMENTAL: OBSERVER - Parte 2/4
 * =====================================================================
 * Observador concreto: envia SMS al cliente cuando ocurre un evento.
 *
 * AQUI SE UNEN DOS PATRONES:
 *   - Observer: este observador reacciona a los eventos de reserva
 *   - Adapter: usa el AdaptadorSms para enviar el SMS a traves
 *              del sistema legado (ServicioSmsLegado)
 */
public class ObservadorNotificacionSms implements ObservadorReserva {

    // Usamos la interfaz, no la implementacion concreta (buena practica)
    private final NotificadorExterno notificador;

    public ObservadorNotificacionSms() {
        // El adaptador conecta con el sistema legado de SMS
        this.notificador = new AdaptadorSms();
    }

    @Override
    public void alCrearReserva(Reserva reserva) {
        String celular = reserva.getCliente().getCelular();
        String asunto  = "Reserva confirmada - La Tienda del Musico";
        String mensaje = String.format(
            "Hola %s! Tu reserva #%s para el instrumento %s %s fue confirmada. " +
            "Fecha de entrega: %s. Fecha de devolucion: %s. Total: $%.0f",
            reserva.getCliente().getNombre(),
            reserva.getId(),
            reserva.getInstrumento().getMarca(),
            reserva.getInstrumento().getModelo(),
            reserva.getFechaInicio(),
            reserva.getFechaDevolucion(),
            reserva.getPrecioTotal()
        );
        notificador.enviarNotificacion(celular, asunto, mensaje);
    }

    @Override
    public void alCancelarReserva(Reserva reserva) {
        String celular = reserva.getCliente().getCelular();
        String asunto  = "Reserva cancelada - La Tienda del Musico";
        String mensaje = String.format(
            "Hola %s! Tu reserva #%s para %s %s ha sido cancelada.",
            reserva.getCliente().getNombre(),
            reserva.getId(),
            reserva.getInstrumento().getMarca(),
            reserva.getInstrumento().getModelo()
        );
        notificador.enviarNotificacion(celular, asunto, mensaje);
    }

    @Override
    public void alDevolverInstrumento(Reserva reserva) {
        String celular = reserva.getCliente().getCelular();
        String asunto  = "Devolucion registrada - La Tienda del Musico";
        String mensaje = String.format(
            "Hola %s! Hemos registrado la devolucion de %s %s. Gracias!",
            reserva.getCliente().getNombre(),
            reserva.getInstrumento().getMarca(),
            reserva.getInstrumento().getModelo()
        );
        notificador.enviarNotificacion(celular, asunto, mensaje);
    }
}
