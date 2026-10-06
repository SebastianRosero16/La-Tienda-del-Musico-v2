package com.tienda.instrumentos.patrones.estructurales;

import com.tienda.instrumentos.dominio.enums.DisponibilidadInstrumento;
import com.tienda.instrumentos.dominio.enums.EstadoInstrumento;
import com.tienda.instrumentos.dominio.enums.TipoInstrumento;
import com.tienda.instrumentos.dominio.modelo.Instrumento;

/**
 * =====================================================================
 * PATRON ESTRUCTURAL: DECORATOR - Parte 1/3 (el Decorador base)
 * =====================================================================
 *
 * PROBLEMA QUE RESUELVE:
 *   Queremos agregar comportamientos EXTRA a un instrumento sin modificar
 *   sus clases originales (GuitarraAcustica, GuitarraElectrica, etc.)
 *   y sin crear una explosion de subclases.
 *
 * COMO FUNCIONA:
 *   - InstrumentoDecorador HEREDA de Instrumento (para ser compatible)
 *   - Internamente ENVUELVE a otro Instrumento (el "decorado")
 *   - Cada metodo delega al instrumento envuelto
 *   - Las subclases del decorador agregan comportamiento EXTRA
 *
 * ANALOGIA:
 *   Como envolver un regalo: el regalo sigue siendo el mismo,
 *   pero con el papel le agregamos presentacion extra.
 *
 * EN EL NEGOCIO:
 *   Podemos "decorar" un instrumento para que incluya seguro,
 *   o para que muestre un aviso cuando esta proximo a vencer.
 */
public abstract class InstrumentoDecorador extends Instrumento {

    // El instrumento "envuelto" al que le vamos a agregar comportamiento
    protected final Instrumento instrumentoDecorado;

    protected InstrumentoDecorador(Instrumento instrumentoDecorado) {
        this.instrumentoDecorado = instrumentoDecorado;
    }

    // Delegamos todos los metodos al instrumento interno
    @Override
    public double calcularPrecioAlquiler(int dias) {
        return instrumentoDecorado.calcularPrecioAlquiler(dias);
    }

    @Override
    public String obtenerDescripcion() {
        return instrumentoDecorado.obtenerDescripcion();
    }

    @Override
    public String getMarca() { return instrumentoDecorado.getMarca(); }

    @Override
    public String getModelo() { return instrumentoDecorado.getModelo(); }

    @Override
    public String getNumeroDeSerie() { return instrumentoDecorado.getNumeroDeSerie(); }

    @Override
    public TipoInstrumento getTipo() { return instrumentoDecorado.getTipo(); }

    @Override
    public EstadoInstrumento getEstado() { return instrumentoDecorado.getEstado(); }

    @Override
    public DisponibilidadInstrumento getDisponibilidad() {
        return instrumentoDecorado.getDisponibilidad();
    }

    @Override
    public double getPrecioPorDia() { return instrumentoDecorado.getPrecioPorDia(); }
}
