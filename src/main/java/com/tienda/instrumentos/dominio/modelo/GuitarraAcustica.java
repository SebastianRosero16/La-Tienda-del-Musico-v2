package com.tienda.instrumentos.dominio.modelo;

import com.tienda.instrumentos.dominio.enums.TipoInstrumento;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * =====================================================================
 * TEMAS CUBIERTOS:
 *   - Herencia: GuitarraAcustica HEREDA de Instrumento
 *   - Polimorfismo: sobreescribe calcularPrecioAlquiler() y
 *     obtenerDescripcion() con comportamiento PROPIO
 *   - Encapsulamiento: atributos propios son privados
 * =====================================================================
 *
 * GuitarraAcustica tiene caracteristicas propias que no comparte
 * con otros instrumentos, como el numero de cuerdas y el material del cuerpo.
 */
@Entity
@DiscriminatorValue("GUITARRA_ACUSTICA")
public class GuitarraAcustica extends Instrumento {

    // Atributos propios de la guitarra acustica (no existen en la clase padre)
    @Column
    private int numeroCuerdas;

    @Column
    private String materialCuerpo; // madera, carbono, etc.

    // Constructor vacio requerido por JPA
    public GuitarraAcustica() {}

    public GuitarraAcustica(String marca, String modelo, String numeroDeSerie,
                             double precioPorDia, int numeroCuerdas, String materialCuerpo) {
        // Llamamos al constructor del padre (super) con los datos comunes
        super(marca, modelo, numeroDeSerie, TipoInstrumento.GUITARRA_ACUSTICA, precioPorDia);
        this.numeroCuerdas = numeroCuerdas;
        this.materialCuerpo = materialCuerpo;
    }

    // =========================================================
    // POLIMORFISMO: implementacion PROPIA del metodo abstracto
    // La guitarra acustica tiene un precio simple: dias x precio base
    // =========================================================
    @Override
    public double calcularPrecioAlquiler(int dias) {
        return getPrecioPorDia() * dias;
    }

    @Override
    public String obtenerDescripcion() {
        return String.format("Guitarra Acustica %s %s | %d cuerdas | Cuerpo: %s | $%.0f/dia",
                getMarca(), getModelo(), numeroCuerdas, materialCuerpo, getPrecioPorDia());
    }

    // Getters y setters propios
    public int getNumeroCuerdas() { return numeroCuerdas; }
    public void setNumeroCuerdas(int numeroCuerdas) { this.numeroCuerdas = numeroCuerdas; }

    public String getMaterialCuerpo() { return materialCuerpo; }
    public void setMaterialCuerpo(String materialCuerpo) { this.materialCuerpo = materialCuerpo; }
}
