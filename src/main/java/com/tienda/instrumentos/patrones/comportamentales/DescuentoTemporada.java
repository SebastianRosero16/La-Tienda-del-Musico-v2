package com.tienda.instrumentos.patrones.comportamentales;

/**
 * =====================================================================
 * PATRON COMPORTAMENTAL: STRATEGY - Parte 4/4
 * =====================================================================
 * Estrategia: descuento de temporada configurable.
 * El dueno puede ajustar el porcentaje segun la epoca del año.
 *
 * ESTO DEMUESTRA LA POTENCIA DEL STRATEGY:
 *   Para agregar esta nueva regla de negocio NO tocamos el servicio
 *   de reservas. Solo creamos una nueva clase e inyectamos la estrategia.
 */
public class DescuentoTemporada implements EstrategiaDescuento {

    private final double porcentajeDescuento;
    private final String nombreTemporada;

    public DescuentoTemporada(double porcentajeDescuento, String nombreTemporada) {
        this.porcentajeDescuento = porcentajeDescuento;
        this.nombreTemporada     = nombreTemporada;
    }

    @Override
    public double calcularDescuento(double precioOriginal) {
        return precioOriginal * (1 - porcentajeDescuento / 100);
    }

    @Override
    public String obtenerNombreEstrategia() {
        return String.format("Descuento %s (%.0f%% off)", nombreTemporada, porcentajeDescuento);
    }
}
