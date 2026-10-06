package com.tienda.instrumentos.patrones.estructurales;

import com.tienda.instrumentos.dominio.modelo.Instrumento;

/**
 * =====================================================================
 * PATRON ESTRUCTURAL: DECORATOR - Parte 2/3
 * =====================================================================
 *
 * InstrumentoConSeguro es un DECORADOR CONCRETO.
 * Agrega la funcionalidad de seguro al instrumento sin modificar
 * las clases originales de instrumento.
 *
 * EJEMPLO DE USO:
 *   Instrumento guitarra = new GuitarraAcustica(...);
 *   Instrumento guitarraAsegurada = new InstrumentoConSeguro(guitarra, 5000);
 *
 *   // El precio ahora incluye el seguro automaticamente
 *   guitarraAsegurada.calcularPrecioAlquiler(7); // precio + seguro * 7 dias
 *   guitarraAsegurada.obtenerDescripcion(); // descripcion + info del seguro
 */
public class InstrumentoConSeguro extends InstrumentoDecorador {

    private final double costoDiarioSeguro; // costo extra del seguro por dia

    public InstrumentoConSeguro(Instrumento instrumento, double costoDiarioSeguro) {
        super(instrumento);
        this.costoDiarioSeguro = costoDiarioSeguro;
    }

    /**
     * DECORATOR en accion: llama al precio original y LE SUMA el seguro.
     * El instrumento decorado no sabe nada de este calculo extra.
     */
    @Override
    public double calcularPrecioAlquiler(int dias) {
        double precioOriginal = instrumentoDecorado.calcularPrecioAlquiler(dias);
        double costoSeguro    = costoDiarioSeguro * dias;
        return precioOriginal + costoSeguro;
    }

    /**
     * DECORATOR en accion: llama a la descripcion original y LE AGREGA
     * la informacion del seguro.
     */
    @Override
    public String obtenerDescripcion() {
        return instrumentoDecorado.obtenerDescripcion()
                + String.format(" | SEGURO incluido (+$%.0f/dia)", costoDiarioSeguro);
    }

    public double getCostoDiarioSeguro() {
        return costoDiarioSeguro;
    }
}
