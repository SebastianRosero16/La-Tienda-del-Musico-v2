package com.tienda.instrumentos.dominio.modelo;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * =====================================================================
 * TEMAS CUBIERTOS:
 *   - Encapsulamiento: atributos privados, validaciones en setters
 *   - Fundamentos del Modelado de Dominio: Cliente es una entidad
 *     del negocio con identidad propia (su numero de identificacion)
 *   - Encapsulamiento de Reglas de Negocio: el cliente no puede
 *     tener mas de 3 reservas activas a la vez
 *   - UUID: el id es un identificador unico universal generado
 *     automaticamente, independiente del motor de base de datos
 * =====================================================================
 */
@Entity
@Table(name = "clientes")
public class Cliente {

    // UUID como clave tecnica: no depende de autoincremento de la BD.
    // El numeroIdentificacion sigue siendo la clave del NEGOCIO (cedula/NIT).
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    // Datos personales del cliente - todos PRIVADOS
    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String numeroIdentificacion; // cedula o NIT

    @Column(nullable = false)
    private String celular;

    @Column
    private String correoElectronico;

    // Un cliente puede tener muchas reservas
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reserva> reservas = new ArrayList<>();

    // Constructor vacio requerido por JPA
    public Cliente() {}

    // Constructor con los datos minimos obligatorios
    public Cliente(String nombre, String numeroIdentificacion, String celular) {
        setNombre(nombre);                               // usamos el setter para validar
        setNumeroIdentificacion(numeroIdentificacion);
        setCelular(celular);
    }

    // =========================================================
    // ENCAPSULAMIENTO DE REGLAS DE NEGOCIO
    // La logica de negocio vive DENTRO del objeto, no dispersa
    // =========================================================

    /**
     * Regla de negocio: un cliente no puede tener mas de 3 reservas activas.
     * Esta regla esta encapsulada aqui y no en el servicio externo.
     */
    public boolean puedeHacerNuevaReserva() {
        long reservasActivas = reservas.stream()
                .filter(r -> r.estaActiva())
                .count();
        return reservasActivas < 3;
    }

    // Retornamos una copia para que nadie modifique la lista directamente
    public List<Reserva> getReservas() {
        return Collections.unmodifiableList(reservas);
    }

    // Este metodo es de uso interno del dominio
    public void agregarReserva(Reserva reserva) {
        this.reservas.add(reserva);
    }

    // GETTERS Y SETTERS CON VALIDACION
    public UUID getId() { return id; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del cliente no puede estar vacio");
        }
        this.nombre = nombre.trim();
    }

    public String getNumeroIdentificacion() { return numeroIdentificacion; }

    public void setNumeroIdentificacion(String numeroIdentificacion) {
        if (numeroIdentificacion == null || numeroIdentificacion.isBlank()) {
            throw new IllegalArgumentException("El numero de identificacion es obligatorio");
        }
        this.numeroIdentificacion = numeroIdentificacion.trim();
    }

    public String getCelular() { return celular; }

    public void setCelular(String celular) {
        if (celular == null || celular.isBlank()) {
            throw new IllegalArgumentException("El celular del cliente es obligatorio");
        }
        this.celular = celular.trim();
    }

    public String getCorreoElectronico() { return correoElectronico; }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    @Override
    public String toString() {
        return String.format("Cliente: %s | ID: %s | Cel: %s", nombre, numeroIdentificacion, celular);
    }
}
