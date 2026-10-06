package com.tienda.instrumentos.controlador;

import com.tienda.instrumentos.dominio.modelo.Reserva;
import com.tienda.instrumentos.servicio.ReservaServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * =====================================================================
 * TEMA: SPRING BOOT Y ARQUITECTURA REST
 * =====================================================================
 *
 * Controlador REST para la gestion de reservas.
 *
 * Endpoints:
 *   GET    /api/reservas                        -> lista todas
 *   GET    /api/reservas/activas                -> lista solo activas
 *   GET    /api/reservas/{id}                   -> busca por id
 *   GET    /api/reservas/cliente/{clienteId}    -> reservas de un cliente
 *   POST   /api/reservas                        -> crea nueva reserva
 *   PUT    /api/reservas/{id}/devolucion        -> registra devolucion
 *   DELETE /api/reservas/{id}                   -> cancela reserva
 */
@RestController
@RequestMapping("/api/reservas")
public class ReservaControlador {

    private final ReservaServicio reservaServicio;

    public ReservaControlador(ReservaServicio reservaServicio) {
        this.reservaServicio = reservaServicio;
    }

    // GET /api/reservas
    @GetMapping
    public ResponseEntity<List<Reserva>> listarTodas() {
        return ResponseEntity.ok(reservaServicio.listarTodas());
    }

    // GET /api/reservas/activas
    @GetMapping("/activas")
    public ResponseEntity<List<Reserva>> listarActivas() {
        return ResponseEntity.ok(reservaServicio.listarReservasActivas());
    }

    // GET /api/reservas/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(reservaServicio.buscarPorId(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // GET /api/reservas/cliente/{clienteId}
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Reserva>> reservasDeCliente(@PathVariable UUID clienteId) {
        return ResponseEntity.ok(reservaServicio.listarReservasDeCliente(clienteId));
    }

    /**
     * POST /api/reservas -> crea una nueva reserva
     *
     * Body JSON:
     * {
     *   "clienteId": 1,
     *   "instrumentoId": 2,
     *   "fechaInicio": "2024-02-01",
     *   "fechaDevolucion": "2024-02-08",
     *   "tipoDescuento": "VIP"   <- opcional: NORMAL, VIP, TEMPORADA
     * }
     *
     * Este endpoint activa en cascada: Builder + State + Strategy + Observer
     */
    @PostMapping
    public ResponseEntity<?> crearReserva(@RequestBody Map<String, Object> datos) {
        try {
            UUID clienteId     = UUID.fromString(datos.get("clienteId").toString());
            UUID instrumentoId = UUID.fromString(datos.get("instrumentoId").toString());
            LocalDate inicio   = LocalDate.parse(datos.get("fechaInicio").toString());
            LocalDate fin      = LocalDate.parse(datos.get("fechaDevolucion").toString());
            String descuento   = datos.containsKey("tipoDescuento")
                                 ? datos.get("tipoDescuento").toString()
                                 : "NORMAL";

            Reserva nueva = reservaServicio.crearReserva(
                    clienteId, instrumentoId, inicio, fin, descuento);

            return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * PUT /api/reservas/{id}/devolucion -> registra la devolucion del instrumento
     *
     * Body JSON: { "estadoDevolucion": "RAYADO" }
     */
    @PutMapping("/{id}/devolucion")
    public ResponseEntity<?> registrarDevolucion(@PathVariable UUID id,
                                                  @RequestBody Map<String, String> datos) {
        try {
            String estado = datos.getOrDefault("estadoDevolucion", "USADO");
            return ResponseEntity.ok(reservaServicio.registrarDevolucion(id, estado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * DELETE /api/reservas/{id} -> cancela la reserva
     * Usamos DELETE porque semanticamente estamos "eliminando" la reserva activa.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelarReserva(@PathVariable UUID id) {
        try {
            Reserva cancelada = reservaServicio.cancelarReserva(id);
            return ResponseEntity.ok(Map.of(
                "mensaje", "Reserva cancelada exitosamente",
                "reservaId", cancelada.getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
