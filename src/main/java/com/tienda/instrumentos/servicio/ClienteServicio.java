package com.tienda.instrumentos.servicio;

import com.tienda.instrumentos.dominio.modelo.Cliente;
import com.tienda.instrumentos.repositorio.ClienteRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * =====================================================================
 * TEMA: CAPA DE SERVICIOS + ENCAPSULAMIENTO DE REGLAS DE NEGOCIO
 * =====================================================================
 *
 * Servicio que maneja la logica de negocio relacionada con los clientes.
 * Valida reglas antes de guardar: no duplicar clientes, datos obligatorios, etc.
 */
@Service
@Transactional
public class ClienteServicio {

    private final ClienteRepositorio clienteRepositorio;

    public ClienteServicio(ClienteRepositorio clienteRepositorio) {
        this.clienteRepositorio = clienteRepositorio;
    }

    // =========================================================
    // REGISTRO DE CLIENTES
    // =========================================================

    /**
     * Registra un nuevo cliente.
     * REGLA DE NEGOCIO: no puede haber dos clientes con el mismo numero de ID.
     */
    public Cliente registrarCliente(String nombre, String numeroIdentificacion, String celular) {
        // Regla de negocio encapsulada en el servicio
        if (clienteRepositorio.existsByNumeroIdentificacion(numeroIdentificacion)) {
            throw new IllegalArgumentException(
                "Ya existe un cliente con el numero de identificacion: " + numeroIdentificacion);
        }

        // El constructor de Cliente ya valida que los datos no esten vacios
        Cliente nuevoCliente = new Cliente(nombre, numeroIdentificacion, celular);
        return clienteRepositorio.save(nuevoCliente);
    }

    /**
     * Actualiza el correo electronico de un cliente.
     */
    public Cliente actualizarCorreo(UUID clienteId, String correo) {
        Cliente cliente = buscarPorId(clienteId);
        cliente.setCorreoElectronico(correo);
        return clienteRepositorio.save(cliente);
    }

    // =========================================================
    // CONSULTAS
    // =========================================================

    @Transactional(readOnly = true)
    public Cliente buscarPorId(UUID id) {
        return clienteRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontro cliente con id: " + id));
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorIdentificacion(String numeroIdentificacion) {
        return clienteRepositorio.findByNumeroIdentificacion(numeroIdentificacion)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontro cliente con identificacion: " + numeroIdentificacion));
    }

    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepositorio.findAll();
    }

    @Transactional(readOnly = true)
    public List<Cliente> buscarPorNombre(String nombre) {
        return clienteRepositorio.findByNombreContainingIgnoreCase(nombre);
    }
}
