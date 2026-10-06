package com.tienda.instrumentos.patrones.comportamentales;

/**
 * =====================================================================
 * PATRON COMPORTAMENTAL: STRATEGY - Parte 1/4 (Interfaz)
 * =====================================================================
 *
 * PROBLEMA QUE RESUELVE:
 *   Hay diferentes formas de calcular el descuento de una reserva:
 *   para clientes nuevos, para clientes VIP, en temporada de descuentos, etc.
 *   Sin Strategy, tendriamos un if/else gigante que crece con cada nuevo tipo.
 *
 * COMO FUNCIONA:
 *   Definimos una INTERFAZ con el contrato "calcular descuento".
 *   Cada algoritmo de descuento es una clase separada que implementa esa interfaz.
 *   En tiempo de ejecucion se elige cual usar SIN cambiar el codigo del servicio.
 *
 * EN EL NEGOCIO:
 *   El dueno puede cambiar la estrategia de descuento segun la temporada
 *   o el tipo de cliente, sin tocar la logica de reservas.
 */
public interface EstrategiaDescuento {

    /**
     * Calcula y retorna el precio final aplicando el descuento correspondiente.
     *
     * @param precioOriginal el precio base antes del descuento
     * @return precio final con el descuento aplicado
     */
    double calcularDescuento(double precioOriginal);

    /**
     * Retorna una descripcion de la estrategia aplicada
     * (para mostrarsela al cliente en la factura).
     */
    String obtenerNombreEstrategia();
}
