package com.tienda.instrumentos.patrones.estructurales;

/**
 * =====================================================================
 * PATRON ESTRUCTURAL: ADAPTER - Parte 2/3
 * =====================================================================
 *
 * Esta clase simula un servicio de SMS externo (de terceros) que
 * ya existe y tiene su propia interfaz incompatible con la nuestra.
 *
 * NO PODEMOS MODIFICAR esta clase (imaginemos que es una libreria
 * externa o un sistema legado del proveedor de SMS).
 * Su metodo se llama "mandarSms" en lugar de "enviarNotificacion".
 */
public class ServicioSmsLegado {

    /**
     * Metodo del sistema legado con firma DISTINTA a nuestra interfaz.
     * Recibe el numero y el texto por separado.
     */
    public void mandarSms(String numeroCelular, String texto) {
        // En un sistema real aqui se conectaria con la API del proveedor de SMS
        System.out.println("=== SMS ENVIADO ===");
        System.out.println("Para: " + numeroCelular);
        System.out.println("Texto: " + texto);
        System.out.println("==================");
    }
}
