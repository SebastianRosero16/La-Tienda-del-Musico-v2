package com.tienda.instrumentos.dominio.modelo;

import com.tienda.instrumentos.dominio.enums.DisponibilidadInstrumento;
import com.tienda.instrumentos.dominio.enums.EstadoInstrumento;
import com.tienda.instrumentos.dominio.enums.TipoInstrumento;
import jakarta.persistence.*;
import java.util.UUID;

/**
 * =====================================================================
 * TEMAS CUBIERTOS:
 *   - Abstraccion: clase abstracta que define el contrato general
 *   - Herencia: las subclases (Guitarra, Bajo, etc.) heredan de aqui
 *   - Encapsulamiento: todos los atributos son privados, se accede
 *     solo a traves de getters y setters
 *   - Fundamentos del Modelado de Dominio: Instrumento es la entidad
 *     central del sistema
 *   - ORM / JPA: las anotaciones mapean esta clase a la tabla "instrumentos"
 *     Hibernate (el ORM) convierte objetos Java a filas de base de datos
 * =====================================================================
 *
 * Instrumento es una clase ABSTRACTA porque no tiene sentido crear
 * un "Instrumento generico". Siempre se crea una Guitarra, un Bajo, etc.
 * Forzamos a las subclases a implementar calcularPrecioAlquiler() y
 * obtenerDescripcion(), lo que es el corazon de la ABSTRACCION.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// InheritanceType.SINGLE_TABLE: toda la jerarquia se guarda en una sola tabla
// Spring/JPA agrega automaticamente una columna "DTYPE" para saber el tipo real
@DiscriminatorColumn(name = "tipo_clase")
@Table(name = "instrumentos")
public abstract class Instrumento {

    // -------------------------
    // ENCAPSULAMIENTO: todos los atributos son PRIVADOS
    // Nadie desde afuera puede modificarlos directamente
    // -------------------------

    // UUID: identificador unico universal, independiente de la base de datos.
    // Garantiza unicidad global: dos sistemas distintos nunca generan el mismo ID.
    // Es diferente al numero de serie del fabricante: el UUID es la clave TECNICA
    // del sistema, el numeroDeSerie es el identificador del NEGOCIO.
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private String modelo;

    @Column(nullable = false)
    private String numeroDeSerie;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoInstrumento tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoInstrumento estado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DisponibilidadInstrumento disponibilidad;

    @Column(nullable = false)
    private double precioPorDia; // precio base en pesos

    // Constructor sin argumentos requerido por JPA
    protected Instrumento() {}

    // Constructor con los datos obligatorios al crear un instrumento
    protected Instrumento(String marca, String modelo, String numeroDeSerie,
                          TipoInstrumento tipo, double precioPorDia) {
        this.marca = marca;
        this.modelo = modelo;
        this.numeroDeSerie = numeroDeSerie;
        this.tipo = tipo;
        this.precioPorDia = precioPorDia;
        // Por defecto, cuando se registra un instrumento esta disponible y nuevo
        this.estado = EstadoInstrumento.NUEVO;
        this.disponibilidad = DisponibilidadInstrumento.DISPONIBLE;
    }

    // =========================================================
    // ABSTRACCION: metodos abstractos que CADA subclase DEBE
    // implementar segun sus propias caracteristicas.
    // Aqui definimos el "QUE" pero no el "COMO".
    // =========================================================

    /**
     * Cada tipo de instrumento calcula su precio de forma diferente.
     * Por ejemplo, una guitarra electrica puede tener recargo por amplificador.
     */
    public abstract double calcularPrecioAlquiler(int dias);

    /**
     * Cada instrumento describe sus caracteristicas propias.
     * Esto es lo que se muestra al cliente al consultar el instrumento.
     */
    public abstract String obtenerDescripcion();

    // =========================================================
    // ENCAPSULAMIENTO: GETTERS Y SETTERS
    // Solo exponemos lo que es necesario. Los setters incluyen
    // validaciones basicas de reglas de negocio.
    // =========================================================

    public UUID getId() { return id; }

    public String getMarca() { return marca; }

    public void setMarca(String marca) {
        if (marca == null || marca.isBlank()) {
            throw new IllegalArgumentException("La marca no puede estar vacia");
        }
        this.marca = marca;
    }

    public String getModelo() { return modelo; }

    public void setModelo(String modelo) {
        if (modelo == null || modelo.isBlank()) {
            throw new IllegalArgumentException("El modelo no puede estar vacio");
        }
        this.modelo = modelo;
    }

    public String getNumeroDeSerie() { return numeroDeSerie; }

    public TipoInstrumento getTipo() { return tipo; }

    public EstadoInstrumento getEstado() { return estado; }

    public void setEstado(EstadoInstrumento estado) {
        this.estado = estado;
    }

    public DisponibilidadInstrumento getDisponibilidad() { return disponibilidad; }

    public void setDisponibilidad(DisponibilidadInstrumento disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public double getPrecioPorDia() { return precioPorDia; }

    public void setPrecioPorDia(double precioPorDia) {
        if (precioPorDia <= 0) {
            throw new IllegalArgumentException("El precio por dia debe ser mayor a cero");
        }
        this.precioPorDia = precioPorDia;
    }

    // Metodo de conveniencia: saber si se puede alquilar sin exponer la logica al exterior
    public boolean estaDisponible() {
        return this.disponibilidad == DisponibilidadInstrumento.DISPONIBLE;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s %s - %s | Estado: %s | Disponibilidad: %s",
                tipo.getNombre(), marca, modelo, numeroDeSerie,
                estado.getDescripcion(), disponibilidad.getDescripcion());
    }
}
