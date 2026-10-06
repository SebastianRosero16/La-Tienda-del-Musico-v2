package com.tienda.instrumentos;

import com.tienda.instrumentos.dominio.enums.TipoInstrumento;
import com.tienda.instrumentos.servicio.ClienteServicio;
import com.tienda.instrumentos.servicio.InstrumentoServicio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * =====================================================================
 * DATOS INICIALES DE PRUEBA
 * =====================================================================
 *
 * CommandLineRunner: se ejecuta automaticamente cuando Spring Boot arranca.
 * Carga datos de ejemplo para que el sistema ya tenga informacion al inicio.
 * Esto es muy util para la DEMO en la exposicion.
 *
 * Demuestra el uso del SINGLETON (GestorTienda) y la FACTORY (InstrumentoFactory).
 */
@Component
public class DatosIniciales implements CommandLineRunner {

    private final InstrumentoServicio instrumentoServicio;
    private final ClienteServicio     clienteServicio;

    public DatosIniciales(InstrumentoServicio instrumentoServicio,
                          ClienteServicio clienteServicio) {
        this.instrumentoServicio = instrumentoServicio;
        this.clienteServicio     = clienteServicio;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n--- Cargando datos iniciales de prueba ---");

        // Registramos instrumentos usando la FACTORY a traves del servicio
        instrumentoServicio.registrarInstrumento(
                TipoInstrumento.GUITARRA_ACUSTICA, "Yamaha", "F310", "YAM-001", 25000);

        instrumentoServicio.registrarInstrumento(
                TipoInstrumento.GUITARRA_ELECTRICA, "Fender", "Stratocaster", "FEN-001", 45000);

        instrumentoServicio.registrarInstrumento(
                TipoInstrumento.GUITARRA_CLASICA, "Admira", "Malaga", "ADM-001", 20000);

        instrumentoServicio.registrarInstrumento(
                TipoInstrumento.GUITARRA_ACUSTICA, "Cort", "AD810", "COR-001", 22000);

        instrumentoServicio.registrarInstrumento(
                TipoInstrumento.GUITARRA_ELECTRICA, "Gibson", "Les Paul", "GIB-001", 60000);

        // Registramos clientes de prueba
        clienteServicio.registrarCliente("Carlos Ramirez",  "10001234", "3001234567");
        clienteServicio.registrarCliente("Maria Gonzalez",  "10005678", "3109876543");
        clienteServicio.registrarCliente("Andres Torres",   "10009999", "3154567890");

        System.out.println("--- Datos iniciales cargados exitosamente ---");
        System.out.println("  - 5 instrumentos registrados");
        System.out.println("  - 3 clientes registrados");
        System.out.println("--- Listo para la demo! ---\n");
    }
}
