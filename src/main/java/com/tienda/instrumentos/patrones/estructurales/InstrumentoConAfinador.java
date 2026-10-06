package com.tienda.instrumentos.patrones.estructurales;

import com.tienda.instrumentos.dominio.modelo.Instrumento;

/**
 * =====================================================================
 * PATRON ESTRUCTURAL: DECORATOR - Parte 3/3
 * =====================================================================
 *
 * Segundo decorador concreto: agrega un afinador digital al instrumento.
 * Esto demuestra que los decoradores se pueden APILAR:
 *
 *   Instrumento guitarra = new GuitarraAcustica(...);
 *   Instrumento conSeguro   = new InstrumentoConSeguro(guitarra, 5000);
 *   Instrumento conAfinador = new InstrumentoConAfinador(conSeguro, 2000);
 *   // Ahora tiene precio base + seguro + afinador
 *
 * Esto es mucho mejor que crear:
 *   GuitarraAcusticaConSeguro, GuitarraAcusticaConAfinador,
 *   GuitarraAcusticaConSeguroYAfinador... (explosion de clases)
 */
public class InstrumentoConAfinador extends InstrumentoDecorador {

    private final double costoDiarioAfinador;

    public InstrumentoConAfinador(Instrumento instrumento, double costoDiarioAfinador) {
        super(instrumento);
        this.costoDiarioAfinador = costoDiarioAfinador;
    }

    @Override
    public double calcularPrecioAlquiler(int dias) {
        // Precio del instrumento (que ya puede tener otros decoradores) + afinador
        return instrumentoDecorado.calcularPrecioAlquiler(dias) + (costoDiarioAfinador * dias);
    }

    @Override
    public String obtenerDescripcion() {
        return instrumentoDecorado.obtenerDescripcion()
                + String.format(" | AFINADOR DIGITAL incluido (+$%.0f/dia)", costoDiarioAfinador);
    }

    public double getCostoDiarioAfinador() {
        return costoDiarioAfinador;
    }
}
