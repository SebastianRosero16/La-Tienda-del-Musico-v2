package com.tienda.instrumentos.patrones.comportamentales;

/**
 * =====================================================================
 * PATRON COMPORTAMENTAL: STRATEGY - Parte 2/4
 * =====================================================================
 * Estrategia: sin descuento (precio normal, para clientes regulares).
 */
public class DescuentoSinDescuento implements EstrategiaDescuento {

    @Override
    public double calcularDescuento(double precioOriginal) {
        // Sin descuento, devolvemos el precio tal cual
        return precioOriginal;
    }

    @Override
    public String obtenerNombreEstrategia() {
        return "Precio normal (sin descuento)";
    }
}
