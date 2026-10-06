package com.tienda.instrumentos.patrones.comportamentales;

import com.tienda.instrumentos.dominio.modelo.Reserva;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * =====================================================================
 * PATRON COMPORTAMENTAL: OBSERVER - Parte 4/4 (el "Publicador")
 * =====================================================================
 *
 * GestorEventosReserva es el SUJETO (o publicador) del patron Observer.
 * Mantiene la lista de observadores y los notifica cuando ocurre un evento.
 *
 * Los observadores se registran aqui (suscripcion).
 * Cuando el servicio llama a "notificarNuevaReserva()", todos los
 * observadores registrados reciben la notificacion automaticamente.
 *
 * Nota: @Component hace que Spring lo maneje como un bean (singleton de Spring).
 */
@Component
public class GestorEventosReserva {

    // Lista de todos los observadores suscritos
    private final List<ObservadorReserva> observadores = new ArrayList<>();

    // Al crearse, registramos los observadores por defecto
    public GestorEventosReserva() {
        // Registramos los dos observadores: SMS y Log
        registrarObservador(new ObservadorNotificacionSms());
        registrarObservador(new ObservadorRegistroLog());
    }

    /**
     * Registra un nuevo observador (suscripcion).
     * Si mañana queremos agregar notificacion por email, solo
     * creamos ObservadorNotificacionEmail y lo registramos aqui.
     */
    public void registrarObservador(ObservadorReserva observador) {
        observadores.add(observador);
    }

    /**
     * Elimina un observador (desuscripcion).
     */
    public void eliminarObservador(ObservadorReserva observador) {
        observadores.remove(observador);
    }

    // Metodos de notificacion: llaman a todos los observadores registrados

    public void notificarNuevaReserva(Reserva reserva) {
        System.out.println("\n--- Notificando nueva reserva a " + observadores.size() + " observadores ---");
        for (ObservadorReserva obs : observadores) {
            obs.alCrearReserva(reserva);
        }
    }

    public void notificarCancelacionReserva(Reserva reserva) {
        System.out.println("\n--- Notificando cancelacion a " + observadores.size() + " observadores ---");
        for (ObservadorReserva obs : observadores) {
            obs.alCancelarReserva(reserva);
        }
    }

    public void notificarDevolucion(Reserva reserva) {
        System.out.println("\n--- Notificando devolucion a " + observadores.size() + " observadores ---");
        for (ObservadorReserva obs : observadores) {
            obs.alDevolverInstrumento(reserva);
        }
    }
}
