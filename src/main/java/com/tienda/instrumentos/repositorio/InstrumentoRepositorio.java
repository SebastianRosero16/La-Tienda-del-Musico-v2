package com.tienda.instrumentos.repositorio;

import com.tienda.instrumentos.dominio.enums.DisponibilidadInstrumento;
import com.tienda.instrumentos.dominio.enums.TipoInstrumento;
import com.tienda.instrumentos.dominio.modelo.Instrumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * =====================================================================
 * TEMA: PATRON REPOSITORY Y PERSISTENCIA
 * =====================================================================
 *
 * PROBLEMA QUE RESUELVE:
 *   La capa de servicio no deberia saber como se guardan los datos
 *   (si es en una base de datos, en un archivo, en memoria, etc.).
 *   El Repository abstrae el mecanismo de persistencia.
 *
 * COMO FUNCIONA CON SPRING DATA JPA:
 *   Solo con extender JpaRepository<Instrumento, Long>, Spring genera
 *   AUTOMATICAMENTE los metodos: save(), findById(), findAll(),
 *   deleteById(), etc. No escribimos SQL.
 *
 *   Los metodos "findBy..." se generan automaticamente segun el nombre:
 *   "findByDisponibilidad" -> Spring genera "SELECT * FROM instrumentos WHERE disponibilidad = ?"
 *
 * EN EL NEGOCIO:
 *   La tienda necesita buscar instrumentos por disponibilidad, por tipo,
 *   o por numero de serie. Todo eso lo hace el Repository.
 */
@Repository
public interface InstrumentoRepositorio extends JpaRepository<Instrumento, UUID> {

    // Spring genera el SQL automaticamente a partir del nombre del metodo
    List<Instrumento> findByDisponibilidad(DisponibilidadInstrumento disponibilidad);

    List<Instrumento> findByTipo(TipoInstrumento tipo);

    Optional<Instrumento> findByNumeroDeSerie(String numeroDeSerie);

    // Busca instrumentos disponibles de un tipo especifico
    List<Instrumento> findByTipoAndDisponibilidad(TipoInstrumento tipo,
                                                   DisponibilidadInstrumento disponibilidad);
}
