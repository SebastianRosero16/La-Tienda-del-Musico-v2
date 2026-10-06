package com.tienda.instrumentos.servicio;

import com.tienda.instrumentos.dominio.enums.DisponibilidadInstrumento;
import com.tienda.instrumentos.dominio.enums.EstadoInstrumento;
import com.tienda.instrumentos.dominio.enums.TipoInstrumento;
import com.tienda.instrumentos.dominio.modelo.Instrumento;
import com.tienda.instrumentos.patrones.comportamentales.*;
import com.tienda.instrumentos.patrones.creacionales.InstrumentoFactory;
import com.tienda.instrumentos.repositorio.InstrumentoRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * =====================================================================
 * TEMA: CAPA DE SERVICIOS + ENCAPSULAMIENTO DE REGLAS DE NEGOCIO
 * =====================================================================
 *
 * El Servicio es el COORDINADOR de la logica de negocio.
 * Conecta: Repository (persistencia) + Patrones + Reglas del dominio.
 *
 * @Service le dice a Spring que esta clase es un servicio
 *           y la registra como un bean para inyeccion de dependencias.
 * @Transactional garantiza que si algo falla a mitad de una operacion,
 *                 todos los cambios en base de datos se revierten.
 */
@Service
@Transactional
public class InstrumentoServicio {

    // Inyeccion de dependencias: Spring nos entrega el repositorio automaticamente
    private final InstrumentoRepositorio instrumentoRepositorio;

    public InstrumentoServicio(InstrumentoRepositorio instrumentoRepositorio) {
        this.instrumentoRepositorio = instrumentoRepositorio;
    }

    // =========================================================
    // REGISTRO DE INSTRUMENTOS (usa el Factory)
    // =========================================================

    /**
     * Registra un nuevo instrumento usando la InstrumentoFactory.
     * El servicio no sabe que subclase se crea, eso lo decide la Factory.
     */
    public Instrumento registrarInstrumento(TipoInstrumento tipo, String marca,
                                             String modelo, String numeroDeSerie,
                                             double precioPorDia) {
        // Verificar que no exista ya un instrumento con ese numero de serie
        if (instrumentoRepositorio.findByNumeroDeSerie(numeroDeSerie).isPresent()) {
            throw new IllegalArgumentException(
                "Ya existe un instrumento con el numero de serie: " + numeroDeSerie);
        }

        // Usamos la FACTORY para crear el instrumento correcto segun el tipo
        Instrumento nuevo = InstrumentoFactory.crearInstrumento(tipo, marca, modelo,
                                                                 numeroDeSerie, precioPorDia);
        return instrumentoRepositorio.save(nuevo);
    }

    // =========================================================
    // CAMBIO DE ESTADO (usa el patron STATE)
    // =========================================================

    /**
     * Cambia la disponibilidad del instrumento usando el patron State.
     * El State valida si la transicion es valida segun el estado actual.
     */
    /**
     * Guarda un instrumento que ya fue modificado externamente.
     */
    public Instrumento guardarInstrumento(Instrumento instrumento) {
        return instrumentoRepositorio.save(instrumento);
    }

    public Instrumento reservarInstrumento(UUID instrumentoId) {
        Instrumento instrumento = buscarPorId(instrumentoId);

        // Obtenemos el estado actual y ejecutamos la accion
        EstadoDisponibilidad estadoActual = obtenerEstado(instrumento.getDisponibilidad());
        estadoActual.reservar(instrumento); // el State cambia la disponibilidad o lanza error

        return instrumentoRepositorio.save(instrumento);
    }

    public Instrumento alquilarInstrumento(UUID instrumentoId) {
        Instrumento instrumento = buscarPorId(instrumentoId);
        EstadoDisponibilidad estadoActual = obtenerEstado(instrumento.getDisponibilidad());
        estadoActual.alquilar(instrumento);
        return instrumentoRepositorio.save(instrumento);
    }

    public Instrumento devolverInstrumento(UUID instrumentoId, EstadoInstrumento estadoAlDevolver) {
        Instrumento instrumento = buscarPorId(instrumentoId);
        EstadoDisponibilidad estadoActual = obtenerEstado(instrumento.getDisponibilidad());
        estadoActual.devolver(instrumento);
        // Actualizamos el estado fisico del instrumento segun como lo devolvio el cliente
        instrumento.setEstado(estadoAlDevolver);
        return instrumentoRepositorio.save(instrumento);
    }

    public Instrumento enviarAReparacion(UUID instrumentoId) {
        Instrumento instrumento = buscarPorId(instrumentoId);
        EstadoDisponibilidad estadoActual = obtenerEstado(instrumento.getDisponibilidad());
        estadoActual.enviarAReparacion(instrumento);
        return instrumentoRepositorio.save(instrumento);
    }

    public Instrumento terminarReparacion(UUID instrumentoId) {
        Instrumento instrumento = buscarPorId(instrumentoId);
        EstadoDisponibilidad estadoActual = obtenerEstado(instrumento.getDisponibilidad());
        estadoActual.terminarReparacion(instrumento);
        instrumento.setEstado(EstadoInstrumento.USADO); // despues de reparacion queda como usado
        return instrumentoRepositorio.save(instrumento);
    }

    // =========================================================
    // CONSULTAS
    // =========================================================

    @Transactional(readOnly = true)
    public List<Instrumento> listarTodos() {
        return instrumentoRepositorio.findAll();
    }

    @Transactional(readOnly = true)
    public List<Instrumento> listarDisponibles() {
        return instrumentoRepositorio.findByDisponibilidad(DisponibilidadInstrumento.DISPONIBLE);
    }

    @Transactional(readOnly = true)
    public List<Instrumento> listarPorTipo(TipoInstrumento tipo) {
        return instrumentoRepositorio.findByTipo(tipo);
    }

    @Transactional(readOnly = true)
    public Instrumento buscarPorId(UUID id) {
        return instrumentoRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontro instrumento con id: " + id));
    }

    // =========================================================
    // METODO AUXILIAR: convierte el enum en el objeto State
    // =========================================================
    private EstadoDisponibilidad obtenerEstado(DisponibilidadInstrumento disponibilidad) {
        return switch (disponibilidad) {
            case DISPONIBLE    -> new EstadoDisponible();
            case RESERVADO     -> new EstadoReservado();
            case ALQUILADO     -> new EstadoAlquilado();
            case EN_REPARACION -> new EstadoEnReparacion();
        };
    }
}
