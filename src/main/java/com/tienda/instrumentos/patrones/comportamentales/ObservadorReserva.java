package com.tienda.instrumentos.patrones.comportamentales;

import com.tienda.instrumentos.dominio.modelo.Reserva;

/**
 * =====================================================================
 * PATRON COMPORTAMENTAL: OBSERVER - Parte 1/4 (Interfaz del Observador)
 * =====================================================================
 *
 * PROBLEMA QUE RESUELVE:
 *   Cuando se crea o cancela una reserva, VARIAS cosas deben pasar:
 *   - Notificar al cliente por SMS
 *   - Registrar en el historial/log
 *   - Actualizar el inventario
 *   Sin Observer, el servicio de reservas tendria que llamar todo eso
 *   directamente, creando un acoplamiento fuerte y dificulta agregar nuevas acciones.
 *
 * COMO FUNCIONA:
 *   - Los "observadores" se registran para escuchar eventos
 *   - Cuando ocurre el evento (nueva reserva), se notifica a todos
 *   - Cada observador reacciona a su manera
 *
 * EN EL NEGOCIO:
 *   Si mañana queremos enviar un correo ademas del SMS, solo agregamos
 *   un nuevo observador. El servicio de reservas no se modifica.
 */
public interface ObservadorReserva {

    /**
     * Se llama cuando se crea una nueva reserva exitosamente.
     */
    void alCrearReserva(Reserva reserva);

    /**
     * Se llama cuando una reserva es cancelada.
     */
    void alCancelarReserva(Reserva reserva);

    /**
     * Se llama cuando el cliente devuelve el instrumento.
     */
    void alDevolverInstrumento(Reserva reserva);
}
