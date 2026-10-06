package com.tienda.instrumentos.patrones.creacionales;

import com.tienda.instrumentos.dominio.modelo.Cliente;
import com.tienda.instrumentos.dominio.modelo.Instrumento;
import com.tienda.instrumentos.dominio.modelo.Reserva;

import java.time.LocalDate;

/**
 * =====================================================================
 * PATRON CREACIONAL: BUILDER (Constructor paso a paso)
 * =====================================================================
 *
 * PROBLEMA QUE RESUELVE:
 *   Crear una Reserva requiere varios datos: cliente, instrumento,
 *   fecha inicio, fecha devolucion. Si el constructor tuviera 6 o 7
 *   parametros seria muy facil equivocarse de orden.
 *
 *   CON BUILDER: se va paso a paso, de forma legible y clara.
 *
 * COMO SE USA (ejemplo):
 *   Reserva r = new ReservaBuilder()
 *       .paraCliente(cliente)
 *       .delInstrumento(instrumento)
 *       .desdeFecha(LocalDate.of(2024, 1, 10))
 *       .hastaFecha(LocalDate.of(2024, 1, 17))
 *       .build();
 *
 * BENEFICIO:
 *   El codigo que crea la reserva es legible como una oracion.
 *   Si falta algun dato obligatorio, el build() lanza error claro.
 */
public class ReservaBuilder {

    // Datos que vamos acumulando paso a paso
    private Cliente cliente;
    private Instrumento instrumento;
    private LocalDate fechaInicio;
    private LocalDate fechaDevolucion;

    // Constructor del Builder (vacio, los datos se setean con los metodos)
    public ReservaBuilder() {}

    // Metodos encadenables (retornan "this" para poder seguir encadenando)

    public ReservaBuilder paraCliente(Cliente cliente) {
        this.cliente = cliente;
        return this; // retorna el mismo builder para encadenar
    }

    public ReservaBuilder delInstrumento(Instrumento instrumento) {
        this.instrumento = instrumento;
        return this;
    }

    public ReservaBuilder desdeFecha(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
        return this;
    }

    public ReservaBuilder hastaFecha(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
        return this;
    }

    /**
     * Metodo final: valida que todo este completo y construye la Reserva.
     * Si falta algun dato obligatorio, lanza una excepcion clara.
     */
    public Reserva build() {
        // Validaciones antes de construir
        if (cliente == null) {
            throw new IllegalStateException("El Builder necesita un cliente (usa paraCliente())");
        }
        if (instrumento == null) {
            throw new IllegalStateException("El Builder necesita un instrumento (usa delInstrumento())");
        }
        if (fechaInicio == null) {
            throw new IllegalStateException("El Builder necesita fecha de inicio (usa desdeFecha())");
        }
        if (fechaDevolucion == null) {
            throw new IllegalStateException("El Builder necesita fecha de devolucion (usa hastaFecha())");
        }

        // Verificar disponibilidad antes de crear la reserva
        if (!instrumento.estaDisponible()) {
            throw new IllegalStateException(
                "El instrumento " + instrumento.getMarca() + " " + instrumento.getModelo()
                + " no esta disponible. Estado actual: " + instrumento.getDisponibilidad().getDescripcion());
        }

        // Verificar que el cliente puede hacer mas reservas
        if (!cliente.puedeHacerNuevaReserva()) {
            throw new IllegalStateException(
                "El cliente " + cliente.getNombre() + " ya tiene el maximo de reservas activas permitidas");
        }

        // Todo bien: creamos y devolvemos la Reserva
        return new Reserva(cliente, instrumento, fechaInicio, fechaDevolucion);
    }
}
