package com.tienda.instrumentos.repositorio;

import com.tienda.instrumentos.dominio.modelo.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * =====================================================================
 * TEMA: PATRON REPOSITORY Y PERSISTENCIA
 * =====================================================================
 *
 * Repository para Reserva. Incluye una consulta JPQL personalizada
 * para mostrar como se pueden escribir queries propios cuando los
 * metodos generados automaticamente no son suficientes.
 */
@Repository
public interface ReservaRepositorio extends JpaRepository<Reserva, UUID> {

    // Todas las reservas de un cliente especifico
    List<Reserva> findByClienteId(UUID clienteId);

    // Todas las reservas de un instrumento especifico
    List<Reserva> findByInstrumentoId(UUID instrumentoId);

    // Solo las reservas activas
    List<Reserva> findByActivaTrue();

    // Reservas activas de un cliente especifico
    List<Reserva> findByClienteIdAndActivaTrue(UUID clienteId);

    /**
     * Consulta JPQL personalizada:
     * Busca reservas activas de un instrumento especifico.
     */
    @Query("SELECT r FROM Reserva r WHERE r.instrumento.id = :instrumentoId AND r.activa = true")
    List<Reserva> buscarReservasActivasPorInstrumento(@Param("instrumentoId") UUID instrumentoId);
}
