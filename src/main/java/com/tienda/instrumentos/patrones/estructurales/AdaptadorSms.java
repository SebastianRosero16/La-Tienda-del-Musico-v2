package com.tienda.instrumentos.patrones.estructurales;

/**
 * =====================================================================
 * PATRON ESTRUCTURAL: ADAPTER - Parte 3/3 (el Adaptador en si)
 * =====================================================================
 *
 * PROBLEMA QUE RESUELVE:
 *   Nuestro sistema quiere enviar notificaciones usando la interfaz
 *   NotificadorExterno (metodo enviarNotificacion).
 *   Pero el servicio de SMS que tenemos disponible es ServicioSmsLegado
 *   y su metodo se llama mandarSms con parametros distintos.
 *   Son INCOMPATIBLES.
 *
 * COMO FUNCIONA:
 *   El Adapter actua como un "enchufe adaptador":
 *   - IMPLEMENTA la interfaz que nuestro sistema espera (NotificadorExterno)
 *   - INTERNAMENTE usa el servicio legado (ServicioSmsLegado)
 *   - TRADUCE la llamada de un formato al otro
 *
 * ANALOGIA:
 *   Es como un adaptador de corriente al viajar: el enchufe del pais
 *   tiene otra forma, pero el adaptador hace que funcione igual.
 *
 * EN EL NEGOCIO:
 *   Cuando se hace una reserva, el sistema notifica al cliente por SMS
 *   sin que el servicio de reservas sepa que usa un sistema legado.
 */
public class AdaptadorSms implements NotificadorExterno {

    // Tenemos una referencia al sistema que queremos adaptar
    private final ServicioSmsLegado servicioSmsLegado;

    public AdaptadorSms() {
        // Creamos el servicio legado internamente
        this.servicioSmsLegado = new ServicioSmsLegado();
    }

    /**
     * Nuestro sistema llama este metodo con la firma que conoce.
     * El Adapter lo "traduce" al metodo del servicio legado.
     */
    @Override
    public void enviarNotificacion(String destinatario, String asunto, String mensaje) {
        // TRADUCCION: combinamos asunto y mensaje para el SMS de texto plano
        String textoSms = "[" + asunto + "] " + mensaje;

        // Llamamos al metodo del sistema legado con su firma propia
        servicioSmsLegado.mandarSms(destinatario, textoSms);
    }
}
