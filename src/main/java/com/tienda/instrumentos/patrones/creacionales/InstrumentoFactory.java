package com.tienda.instrumentos.patrones.creacionales;

import com.tienda.instrumentos.dominio.enums.TipoInstrumento;
import com.tienda.instrumentos.dominio.modelo.GuitarraAcustica;
import com.tienda.instrumentos.dominio.modelo.GuitarraClasica;
import com.tienda.instrumentos.dominio.modelo.GuitarraElectrica;
import com.tienda.instrumentos.dominio.modelo.Instrumento;

/**
 * =====================================================================
 * PATRON CREACIONAL: FACTORY (Fabrica)
 * =====================================================================
 *
 * PROBLEMA QUE RESUELVE:
 *   Cuando el dueno de la tienda quiere registrar un nuevo instrumento,
 *   no deberia preocuparse por cual subclase instanciar (new GuitarraAcustica,
 *   new GuitarraElectrica, etc.). La Factory centraliza esa decision.
 *
 * COMO FUNCIONA:
 *   El cliente llama a crearInstrumento(tipo, ...) y la fabrica
 *   decide QUE objeto concreto crear segun el tipo.
 *   El codigo que llama a la fabrica NO conoce las subclases concretas.
 *
 * BENEFICIO:
 *   Si mañana agregamos "Violin", solo modificamos la Factory,
 *   no todos los lugares donde se crean instrumentos.
 */
public class InstrumentoFactory {

    /**
     * Metodo principal de la fabrica.
     * Recibe el tipo y devuelve el instrumento correcto.
     *
     * Usamos valores por defecto sensatos para simplificar la creacion.
     * Para personalizar mas, usar el Builder (ver ReservaBuilder).
     */
    public static Instrumento crearInstrumento(TipoInstrumento tipo,
                                                String marca,
                                                String modelo,
                                                String numeroDeSerie,
                                                double precioPorDia) {
        // La fabrica decide que subclase instanciar segun el tipo
        return switch (tipo) {
            case GUITARRA_ACUSTICA -> new GuitarraAcustica(
                    marca, modelo, numeroDeSerie, precioPorDia,
                    6,          // cuerdas por defecto
                    "Madera"    // material por defecto
            );
            case GUITARRA_ELECTRICA -> new GuitarraElectrica(
                    marca, modelo, numeroDeSerie, precioPorDia,
                    "Humbucker", // tipo pastilla por defecto
                    false        // sin amplificador por defecto
            );
            case GUITARRA_CLASICA -> new GuitarraClasica(
                    marca, modelo, numeroDeSerie, precioPorDia,
                    "Alta tension", // tipo nylon por defecto
                    false           // sin estuche por defecto
            );
            default -> throw new IllegalArgumentException(
                    "Tipo de instrumento no soportado: " + tipo);
        };
    }

    /**
     * Version extendida: crea una guitarra electrica especificando
     * si incluye amplificador.
     * Muestra como la fabrica puede tener metodos especializados.
     */
    public static GuitarraElectrica crearGuitarraElectrica(String marca, String modelo,
                                                            String numeroDeSerie,
                                                            double precioPorDia,
                                                            String tipoPastilla,
                                                            boolean conAmplificador) {
        return new GuitarraElectrica(marca, modelo, numeroDeSerie,
                precioPorDia, tipoPastilla, conAmplificador);
    }

    /**
     * Version extendida: crea una guitarra acustica con detalle de cuerdas.
     */
    public static GuitarraAcustica crearGuitarraAcustica(String marca, String modelo,
                                                          String numeroDeSerie,
                                                          double precioPorDia,
                                                          int numeroCuerdas,
                                                          String materialCuerpo) {
        return new GuitarraAcustica(marca, modelo, numeroDeSerie,
                precioPorDia, numeroCuerdas, materialCuerpo);
    }
}
