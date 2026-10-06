package com.tienda.instrumentos.patrones.estructurales;

public interface NotificadorExterno {
    void enviarNotificacion(String destinatario, String asunto, String mensaje);
}
