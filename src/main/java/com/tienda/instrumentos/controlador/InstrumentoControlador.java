package com.tienda.instrumentos.controlador;

import com.tienda.instrumentos.dominio.enums.EstadoInstrumento;
import com.tienda.instrumentos.dominio.enums.TipoInstrumento;
import com.tienda.instrumentos.dominio.modelo.Instrumento;
import com.tienda.instrumentos.servicio.InstrumentoServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * =====================================================================
 * TEMA: SPRING BOOT Y ARQUITECTURA REST
 * =====================================================================
 *
 * El Controlador es la PUERTA DE ENTRADA al sistema desde el exterior.
 * Recibe peticiones HTTP y delega el trabajo al Servicio.
 * NUNCA tiene logica de negocio, solo:
 *   1. Recibe la peticion
 *   2. Llama al servicio
 *   3. Devuelve la respuesta HTTP
 *
 * @RestController = @Controller + @ResponseBody
 *   Le dice a Spring que esta clase maneja peticiones REST
 *   y que las respuestas se convierten automaticamente a JSON.
 *
 * @RequestMapping("/api/instrumentos") define la URL base de todos
 *   los endpoints de este controlador.
 */
@RestController
@RequestMapping("/api/instrumentos")
public class InstrumentoControlador {

    private final InstrumentoServicio instrumentoServicio;

    // Inyeccion de dependencias por constructor (buena practica en Spring)
    public InstrumentoControlador(InstrumentoServicio instrumentoServicio) {
        this.instrumentoServicio = instrumentoServicio;
    }

    // =========================================================
    // GET /api/instrumentos -> lista todos los instrumentos
    // =========================================================
    @GetMapping
    public ResponseEntity<List<Instrumento>> listarTodos() {
        List<Instrumento> instrumentos = instrumentoServicio.listarTodos();
        return ResponseEntity.ok(instrumentos); // HTTP 200 OK
    }

    // =========================================================
    // GET /api/instrumentos/disponibles -> solo los disponibles
    // =========================================================
    @GetMapping("/disponibles")
    public ResponseEntity<List<Instrumento>> listarDisponibles() {
        return ResponseEntity.ok(instrumentoServicio.listarDisponibles());
    }

    // =========================================================
    // GET /api/instrumentos/{id} -> busca uno por ID
    // =========================================================
    @GetMapping("/{id}")
    public ResponseEntity<Instrumento> buscarPorId(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(instrumentoServicio.buscarPorId(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build(); // HTTP 404
        }
    }

    // =========================================================
    // GET /api/instrumentos/tipo/{tipo} -> filtra por tipo
    // =========================================================
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Instrumento>> listarPorTipo(@PathVariable String tipo) {
        try {
            TipoInstrumento tipoEnum = TipoInstrumento.valueOf(tipo.toUpperCase());
            return ResponseEntity.ok(instrumentoServicio.listarPorTipo(tipoEnum));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // HTTP 400
        }
    }

    // =========================================================
    // POST /api/instrumentos -> registra un nuevo instrumento
    // Body JSON: { "tipo": "GUITARRA_ACUSTICA", "marca": "Yamaha",
    //              "modelo": "F310", "numeroDeSerie": "YAM001", "precioPorDia": 15000 }
    // =========================================================
    @PostMapping
    public ResponseEntity<?> registrarInstrumento(@RequestBody Map<String, Object> datos) {
        try {
            TipoInstrumento tipo = TipoInstrumento.valueOf(
                    datos.get("tipo").toString().toUpperCase());
            String marca          = datos.get("marca").toString();
            String modelo         = datos.get("modelo").toString();
            String numeroDeSerie  = datos.get("numeroDeSerie").toString();
            double precioPorDia   = Double.parseDouble(datos.get("precioPorDia").toString());

            Instrumento nuevo = instrumentoServicio.registrarInstrumento(
                    tipo, marca, modelo, numeroDeSerie, precioPorDia);

            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo); // HTTP 201
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // =========================================================
    // PUT /api/instrumentos/{id}/reservar -> marca como reservado
    // =========================================================
    @PutMapping("/{id}/reservar")
    public ResponseEntity<?> reservar(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(instrumentoServicio.reservarInstrumento(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // =========================================================
    // PUT /api/instrumentos/{id}/alquilar -> marca como alquilado
    // =========================================================
    @PutMapping("/{id}/alquilar")
    public ResponseEntity<?> alquilar(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(instrumentoServicio.alquilarInstrumento(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // =========================================================
    // PUT /api/instrumentos/{id}/devolver -> marca como disponible
    // Body JSON: { "estado": "USADO" }
    // =========================================================
    @PutMapping("/{id}/devolver")
    public ResponseEntity<?> devolver(@PathVariable UUID id,
                                       @RequestBody Map<String, String> datos) {
        try {
            EstadoInstrumento estado = EstadoInstrumento.valueOf(
                    datos.getOrDefault("estado", "USADO").toUpperCase());
            return ResponseEntity.ok(instrumentoServicio.devolverInstrumento(id, estado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // =========================================================
    // PUT /api/instrumentos/{id}/reparacion -> envia a reparar
    // =========================================================
    @PutMapping("/{id}/reparacion")
    public ResponseEntity<?> enviarAReparacion(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(instrumentoServicio.enviarAReparacion(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // =========================================================
    // PUT /api/instrumentos/{id}/reparacion/terminar
    // =========================================================
    @PutMapping("/{id}/reparacion/terminar")
    public ResponseEntity<?> terminarReparacion(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(instrumentoServicio.terminarReparacion(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
