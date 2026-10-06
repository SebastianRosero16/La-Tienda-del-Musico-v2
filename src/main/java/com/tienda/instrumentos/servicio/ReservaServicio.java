package com.tienda.instrumentos.servicio;

import com.tienda.instrumentos.dominio.enums.DisponibilidadInstrumento;
import com.tienda.instrumentos.dominio.enums.EstadoInstrumento;
import com.tienda.instrumentos.dominio.modelo.Cliente;
import com.tienda.instrumentos.dominio.modelo.Instrumento;
import com.tienda.instrumentos.dominio.modelo.Reserva;
import com.tienda.instrumentos.patrones.comportamentales.*;
import com.tienda.instrumentos.patrones.creacionales.ReservaBuilder;
import com.tienda.instrumentos.repositorio.ReservaRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * =====================================================================
 * TEMA: CAPA DE SERVICIOS + ENCAPSULAMIENTO DE REGLAS DE NEGOCIO
 * =====================================================================
 *
 * ReservaServicio es el corazon del sistema.
 * Aqui se unen en una sola operacion:
 *   - BUILDER: para construir la Reserva paso a paso
 *   - STATE: para cambiar disponibilidad del instrumento
 *   - OBSERVER: para notificar a todos los interesados
 *   - STRATEGY: para aplicar el descuento correcto
 *   - REPOSITORY: para persistir todo en la base de datos
 */
@Service
@Transactional
public class ReservaServicio {

    private final ReservaRepositorio     reservaRepositorio;
    private final ClienteServicio        clienteServicio;
    private final InstrumentoServicio    instrumentoServicio;
    private final GestorEventosReserva   gestorEventos;      // Observer

    public ReservaServicio(ReservaRepositorio reservaRepositorio,
                           ClienteServicio clienteServicio,
                           InstrumentoServicio instrumentoServicio,
                           GestorEventosReserva gestorEventos) {
        this.reservaRepositorio  = reservaRepositorio;
        this.clienteServicio     = clienteServicio;
        this.instrumentoServicio = instrumentoServicio;
        this.gestorEventos       = gestorEventos;
    }

    // =========================================================
    // CREAR RESERVA: aqui se unen BUILDER + STATE + OBSERVER + STRATEGY
    // =========================================================

    /**
     * Crea una nueva reserva para un cliente e instrumento dado.
     *
     * Flujo completo:
     * 1. Busca cliente e instrumento en el Repository
     * 2. Usa el BUILDER para construir la Reserva (con validaciones)
     * 3. Usa el STATE para cambiar la disponibilidad del instrumento
     * 4. Aplica la STRATEGY de descuento segun el tipo de cliente
     * 5. Persiste en base de datos via Repository
     * 6. Notifica a los OBSERVERS (SMS + Log)
     */
    public Reserva crearReserva(UUID clienteId, UUID instrumentoId,
                                LocalDate fechaInicio, LocalDate fechaDevolucion,
                                String tipoDescuento) {

        // 1. Obtener entidades del Repository
        Cliente    cliente    = clienteServicio.buscarPorId(clienteId);
        Instrumento instrumento = instrumentoServicio.buscarPorId(instrumentoId);

        // 2. Usar el BUILDER para construir la reserva paso a paso
        //    El Builder valida disponibilidad y limite de reservas del cliente
        Reserva reserva = new ReservaBuilder()
                .paraCliente(cliente)
                .delInstrumento(instrumento)
                .desdeFecha(fechaInicio)
                .hastaFecha(fechaDevolucion)
                .build(); // aqui se valida todo y se lanza error si algo falla

        // 3. Usar el STATE para marcar el instrumento como RESERVADO
        instrumento.setDisponibilidad(DisponibilidadInstrumento.RESERVADO);
        instrumentoServicio.guardarInstrumento(instrumento);

        // 4. Aplicar la STRATEGY de descuento segun el tipo indicado
        EstrategiaDescuento estrategia = seleccionarEstrategia(tipoDescuento);
        // El precio ya fue calculado en el Builder; aqui aplicamos el descuento
        // Nota: en un sistema real la Reserva tendria el precio con descuento aplicado
        double precioConDescuento = estrategia.calcularDescuento(reserva.getPrecioTotal());
        System.out.println("Estrategia aplicada: " + estrategia.obtenerNombreEstrategia());
        System.out.println("Precio original: $" + reserva.getPrecioTotal()
            + " | Precio final: $" + precioConDescuento);

        // 5. Persistir la reserva
        reservaRepositorio.save(reserva);

        // 6. Notificar a todos los OBSERVERS (SMS al cliente + Log de auditoria)
        gestorEventos.notificarNuevaReserva(reserva);

        return reserva;
    }

    // =========================================================
    // DEVOLUCION DEL INSTRUMENTO
    // =========================================================

    /**
     * Registra la devolucion de un instrumento.
     * Actualiza el estado fisico del instrumento segun como lo devolvio el cliente.
     */
    public Reserva registrarDevolucion(UUID reservaId, String estadoDevolucion) {
        Reserva reserva = buscarPorId(reservaId);

        if (!reserva.estaActiva()) {
            throw new IllegalStateException("Esta reserva ya fue finalizada anteriormente.");
        }

        // Convertir el string a enum de estado fisico
        EstadoInstrumento nuevoEstado;
        try {
            nuevoEstado = EstadoInstrumento.valueOf(estadoDevolucion.toUpperCase());
        } catch (IllegalArgumentException e) {
            nuevoEstado = EstadoInstrumento.USADO; // estado por defecto si no se especifica
        }

        // Finalizar la reserva (guarda el estado de devolucion)
        reserva.finalizarReserva(nuevoEstado.getDescripcion());

        // Cambiar la disponibilidad del instrumento a DISPONIBLE usando STATE
        instrumentoServicio.devolverInstrumento(reserva.getInstrumento().getId(), nuevoEstado);

        Reserva reservaFinalizada = reservaRepositorio.save(reserva);

        // Notificar a los OBSERVERS
        gestorEventos.notificarDevolucion(reservaFinalizada);

        return reservaFinalizada;
    }

    // =========================================================
    // CANCELAR RESERVA
    // =========================================================

    public Reserva cancelarReserva(UUID reservaId) {
        Reserva reserva = buscarPorId(reservaId);

        if (!reserva.estaActiva()) {
            throw new IllegalStateException("No se puede cancelar: la reserva ya esta finalizada.");
        }

        reserva.setActiva(false);

        // El instrumento vuelve a estar disponible
        reserva.getInstrumento().setDisponibilidad(DisponibilidadInstrumento.DISPONIBLE);

        Reserva reservaCancelada = reservaRepositorio.save(reserva);

        // Notificar a los OBSERVERS
        gestorEventos.notificarCancelacionReserva(reservaCancelada);

        return reservaCancelada;
    }

    // =========================================================
    // CONSULTAS
    // =========================================================

    @Transactional(readOnly = true)
    public List<Reserva> listarTodas() {
        return reservaRepositorio.findAll();
    }

    @Transactional(readOnly = true)
    public List<Reserva> listarReservasDeCliente(UUID clienteId) {
        return reservaRepositorio.findByClienteId(clienteId);
    }

    @Transactional(readOnly = true)
    public List<Reserva> listarReservasActivas() {
        return reservaRepositorio.findByActivaTrue();
    }

    @Transactional(readOnly = true)
    public Reserva buscarPorId(UUID id) {
        return reservaRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontro reserva con id: " + id));
    }

    // =========================================================
    // METODO AUXILIAR: selecciona la estrategia de descuento
    // =========================================================

    /**
     * Selecciona la STRATEGY de descuento segun el tipo indicado.
     * Esto es exactamente el patron Strategy en accion:
     * segun el parametro, se devuelve un algoritmo diferente.
     */
    private EstrategiaDescuento seleccionarEstrategia(String tipoDescuento) {
        if (tipoDescuento == null) {
            return new DescuentoSinDescuento();
        }
        return switch (tipoDescuento.toUpperCase()) {
            case "VIP"       -> new DescuentoClienteVIP();
            case "TEMPORADA" -> new DescuentoTemporada(10, "Fin de año");
            default          -> new DescuentoSinDescuento();
        };
    }
}
