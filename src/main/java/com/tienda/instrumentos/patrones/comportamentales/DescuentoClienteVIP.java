package com.tienda.instrumentos.patrones.comportamentales;

/**
 * =====================================================================
 * PATRON COMPORTAMENTAL: STRATEGY - Parte 3/4
 * =====================================================================
 * Estrategia: descuento del 20% para clientes VIP (mas de 5 reservas).
 */
public class DescuentoClienteVIP implements EstrategiaDescuento {

    private static final double PORCENTAJE_VIP = 0.20; // 20% de descuento

    @Override
    public double calcularDescuento(double precioOriginal) {
        // Aplicamos el 20% de descuento al precio original
        return precioOriginal * (1 - PORCENTAJE_VIP);
    }

    @Override
    public String obtenerNombreEstrategia() {
        return "Descuento Cliente VIP (20% off)";
    }
}
