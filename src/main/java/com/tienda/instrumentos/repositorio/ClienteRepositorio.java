package com.tienda.instrumentos.repositorio;

import com.tienda.instrumentos.dominio.modelo.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * =====================================================================
 * TEMA: PATRON REPOSITORY Y PERSISTENCIA
 * =====================================================================
 *
 * Repository para la entidad Cliente.
 * El numero de identificacion es unico, por eso podemos buscar por el.
 */
@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, UUID> {

    // Busca un cliente por su numero de cedula/NIT
    Optional<Cliente> findByNumeroIdentificacion(String numeroIdentificacion);

    // Busca por nombre (ignora mayusculas/minusculas)
    java.util.List<Cliente> findByNombreContainingIgnoreCase(String nombre);

    // Verifica si ya existe un cliente con ese numero de identificacion
    boolean existsByNumeroIdentificacion(String numeroIdentificacion);
}
