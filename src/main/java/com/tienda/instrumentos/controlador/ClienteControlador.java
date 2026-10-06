package com.tienda.instrumentos.controlador;

import com.tienda.instrumentos.dominio.modelo.Cliente;
import com.tienda.instrumentos.servicio.ClienteServicio;
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
 * Controlador REST para la gestion de clientes.
 * Endpoints disponibles:
 *   GET    /api/clientes              -> lista todos
 *   GET    /api/clientes/{id}         -> busca por id
 *   GET    /api/clientes/buscar?nombre=X -> busca por nombre
 *   POST   /api/clientes              -> registra nuevo cliente
 *   PUT    /api/clientes/{id}/correo  -> actualiza correo
 */
@RestController
@RequestMapping("/api/clientes")
public class ClienteControlador {

    private final ClienteServicio clienteServicio;

    public ClienteControlador(ClienteServicio clienteServicio) {
        this.clienteServicio = clienteServicio;
    }

    // GET /api/clientes
    @GetMapping
    public ResponseEntity<List<Cliente>> listarTodos() {
        return ResponseEntity.ok(clienteServicio.listarTodos());
    }

    // GET /api/clientes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(clienteServicio.buscarPorId(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // GET /api/clientes/buscar?nombre=Juan
    @GetMapping("/buscar")
    public ResponseEntity<List<Cliente>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(clienteServicio.buscarPorNombre(nombre));
    }

    // POST /api/clientes
    // Body: { "nombre": "Juan Perez", "numeroIdentificacion": "123456", "celular": "3001234567" }
    @PostMapping
    public ResponseEntity<?> registrarCliente(@RequestBody Map<String, String> datos) {
        try {
            String nombre               = datos.get("nombre");
            String numeroIdentificacion = datos.get("numeroIdentificacion");
            String celular              = datos.get("celular");

            Cliente nuevo = clienteServicio.registrarCliente(nombre, numeroIdentificacion, celular);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // PUT /api/clientes/{id}/correo
    // Body: { "correo": "juan@email.com" }
    @PutMapping("/{id}/correo")
    public ResponseEntity<?> actualizarCorreo(@PathVariable UUID id,
                                               @RequestBody Map<String, String> datos) {
        try {
            String correo = datos.get("correo");
            return ResponseEntity.ok(clienteServicio.actualizarCorreo(id, correo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
