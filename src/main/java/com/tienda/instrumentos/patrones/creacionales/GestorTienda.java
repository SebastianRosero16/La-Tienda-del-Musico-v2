package com.tienda.instrumentos.patrones.creacionales;

/**
 * =====================================================================
 * PATRON CREACIONAL: SINGLETON
 * =====================================================================
 *
 * PROBLEMA QUE RESUELVE:
 *   La tienda es UNA sola. No tiene sentido tener dos instancias
 *   de "GestorTienda" porque habria informacion duplicada o incoherente.
 *   El Singleton garantiza que en toda la aplicacion exista
 *   EXACTAMENTE UNA instancia de este objeto.
 *
 * COMO FUNCIONA:
 *   1. El constructor es PRIVADO -> nadie puede hacer "new GestorTienda()"
 *   2. La unica instancia se guarda en un atributo estatico privado
 *   3. Se accede a ella mediante el metodo estatico getInstancia()
 *
 * EN EL NEGOCIO:
 *   Aqui guardamos la configuracion global de la tienda: nombre,
 *   porcentaje de descuento, limite de reservas por cliente, etc.
 */
public class GestorTienda {

    // La unica instancia, de tipo estatico para que exista a nivel de clase
    private static GestorTienda instancia;

    // Configuracion global de la tienda
    private String nombreTienda;
    private String propietario;
    private double porcentajeDescuentoVIP;  // descuento para clientes frecuentes
    private int maxReservasPorCliente;

    // CONSTRUCTOR PRIVADO: nadie puede instanciar esta clase desde afuera
    private GestorTienda() {
        // Valores por defecto de la tienda
        this.nombreTienda       = "La Tienda del Musico";
        this.propietario        = "Don Carlos";
        this.porcentajeDescuentoVIP = 15.0; // 15% de descuento VIP
        this.maxReservasPorCliente  = 3;
    }

    /**
     * Punto de acceso global a la unica instancia.
     *
     * "synchronized" garantiza que en aplicaciones con multiples
     * hilos (como Spring) no se creen dos instancias al mismo tiempo.
     */
    public static synchronized GestorTienda getInstancia() {
        if (instancia == null) {
            // Solo se crea la primera vez que se llama
            instancia = new GestorTienda();
        }
        return instancia;
    }

    // Getters y setters de configuracion

    public String getNombreTienda() { return nombreTienda; }
    public void setNombreTienda(String nombreTienda) { this.nombreTienda = nombreTienda; }

    public String getPropietario() { return propietario; }
    public void setPropietario(String propietario) { this.propietario = propietario; }

    public double getPorcentajeDescuentoVIP() { return porcentajeDescuentoVIP; }
    public void setPorcentajeDescuentoVIP(double porcentaje) {
        this.porcentajeDescuentoVIP = porcentaje;
    }

    public int getMaxReservasPorCliente() { return maxReservasPorCliente; }
    public void setMaxReservasPorCliente(int max) { this.maxReservasPorCliente = max; }

    /**
     * Aplica el descuento VIP a un precio dado.
     * Esta logica centralizada evita que se calcule diferente en distintos lugares.
     */
    public double aplicarDescuentoVIP(double precio) {
        return precio * (1 - porcentajeDescuentoVIP / 100);
    }

    @Override
    public String toString() {
        return String.format("Tienda: %s | Propietario: %s | Descuento VIP: %.0f%%",
                nombreTienda, propietario, porcentajeDescuentoVIP);
    }
}
