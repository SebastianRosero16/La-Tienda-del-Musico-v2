package com.tienda.instrumentos.dominio.modelo;

import com.tienda.instrumentos.dominio.enums.TipoInstrumento;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * =====================================================================
 * TEMAS CUBIERTOS:
 *   - Herencia: GuitarraElectrica HEREDA de Instrumento
 *   - Polimorfismo: su calculo de precio ES DIFERENTE al de la acustica.
 *     Si incluye amplificador, el precio aumenta un 30%.
 *     Esto demuestra que el mismo metodo (calcularPrecioAlquiler) se
 *     comporta diferente segun el objeto concreto.
 * =====================================================================
 */
@Entity
@DiscriminatorValue("GUITARRA_ELECTRICA")
public class GuitarraElectrica extends Instrumento {

    @Column
    private String tipoPastilla; // Single coil, Humbucker, etc.

    @Column
    private boolean incluyeAmplificador; // si viene con amplificador, el precio sube

    public GuitarraElectrica() {}

    public GuitarraElectrica(String marca, String modelo, String numeroDeSerie,
                              double precioPorDia, String tipoPastilla, boolean incluyeAmplificador) {
        super(marca, modelo, numeroDeSerie, TipoInstrumento.GUITARRA_ELECTRICA, precioPorDia);
        this.tipoPastilla = tipoPastilla;
        this.incluyeAmplificador = incluyeAmplificador;
    }

    // =========================================================
    // POLIMORFISMO: mismo metodo, comportamiento DISTINTO
    // La electrica cobra 30% mas si incluye amplificador
    // =========================================================
    @Override
    public double calcularPrecioAlquiler(int dias) {
        double precioBase = getPrecioPorDia() * dias;
        // Regla de negocio: con amplificador se aplica recargo del 30%
        if (incluyeAmplificador) {
            return precioBase * 1.30;
        }
        return precioBase;
    }

    @Override
    public String obtenerDescripcion() {
        String amp = incluyeAmplificador ? "Con amplificador (+30%)" : "Sin amplificador";
        return String.format("Guitarra Electrica %s %s | Pastilla: %s | %s | $%.0f/dia",
                getMarca(), getModelo(), tipoPastilla, amp, getPrecioPorDia());
    }

    public String getTipoPastilla() { return tipoPastilla; }
    public void setTipoPastilla(String tipoPastilla) { this.tipoPastilla = tipoPastilla; }

    public boolean isIncluyeAmplificador() { return incluyeAmplificador; }
    public void setIncluyeAmplificador(boolean incluyeAmplificador) {
        this.incluyeAmplificador = incluyeAmplificador;
    }
}
