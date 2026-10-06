# Sistema de Gestión de Préstamos de Instrumentos Musicales
**Proyecto Evaluativo — Programación Orientada a Objetos | Momento 2**

**Integrantes:** 3 personas  
**Tecnología:** Java 17 + Spring Boot 3.2 + H2 + Maven

---

## Cómo ejecutar el proyecto

### Requisitos
- Java 17 o superior
- Maven 3.6+
- No se necesita instalar ninguna base de datos (H2 corre en memoria)

### Pasos para correr

```bash
# 1. Entrar a la carpeta del proyecto
cd instrumentos-musicales

# 2. Correr el backend
mvn spring-boot:run
```

### Cuando veas esto en la consola, ya está listo:
```
Tomcat started on port(s): 8080
--- Datos iniciales cargados exitosamente ---
  - 5 instrumentos registrados
  - 3 clientes registrados
=========================================================
  Sistema de Gestion de Instrumentos Musicales
  Aplicacion iniciada correctamente!
=========================================================
```

### URLs disponibles
| Qué es | URL |
|--------|-----|
| **Frontend (interfaz visual)** | http://localhost:8080 |
| **API REST** | http://localhost:8080/api |
| **Consola H2 (base de datos)** | http://localhost:8080/h2-console |

**Consola H2:**
- JDBC URL: `jdbc:h2:mem:tiendainstrumentos`
- Usuario: `sa`
- Contraseña: (dejar vacía)

---

## Estructura del proyecto

```
instrumentos-musicales/
├── src/main/java/com/tienda/instrumentos/
│   ├── InstrumentosMusicalesApp.java        ← Clase main, arranca Spring Boot
│   ├── DatosIniciales.java                  ← Carga datos de prueba al arrancar
│   │
│   ├── dominio/
│   │   ├── enums/
│   │   │   ├── EstadoInstrumento.java       ← NUEVO, USADO, RAYADO, DAÑADO
│   │   │   ├── DisponibilidadInstrumento.java ← DISPONIBLE, RESERVADO, ALQUILADO, EN_REPARACION
│   │   │   └── TipoInstrumento.java         ← GUITARRA_ACUSTICA, ELECTRICA, CLASICA...
│   │   └── modelo/
│   │       ├── Instrumento.java             ← Clase ABSTRACTA (Herencia + Abstracción)
│   │       ├── GuitarraAcustica.java        ← Subclase (Herencia + Polimorfismo)
│   │       ├── GuitarraElectrica.java       ← Subclase (Herencia + Polimorfismo)
│   │       ├── GuitarraClasica.java         ← Subclase (Herencia + Polimorfismo)
│   │       ├── Cliente.java                 ← Encapsulamiento + Reglas de Negocio
│   │       └── Reserva.java                 ← Entidad central del negocio
│   │
│   ├── patrones/
│   │   ├── creacionales/
│   │   │   ├── GestorTienda.java            ← SINGLETON
│   │   │   ├── InstrumentoFactory.java      ← FACTORY
│   │   │   └── ReservaBuilder.java          ← BUILDER
│   │   ├── estructurales/
│   │   │   ├── NotificadorExterno.java      ← Interfaz del ADAPTER
│   │   │   ├── ServicioSmsLegado.java       ← Sistema incompatible (ADAPTER)
│   │   │   ├── AdaptadorSms.java            ← ADAPTER (traduce interfaces)
│   │   │   ├── InstrumentoDecorador.java    ← Base del DECORATOR
│   │   │   ├── InstrumentoConSeguro.java    ← DECORATOR concreto
│   │   │   └── InstrumentoConAfinador.java  ← DECORATOR concreto (apilable)
│   │   └── comportamentales/
│   │       ├── EstrategiaDescuento.java     ← Interfaz STRATEGY
│   │       ├── DescuentoSinDescuento.java   ← STRATEGY: precio normal
│   │       ├── DescuentoClienteVIP.java     ← STRATEGY: 20% off
│   │       ├── DescuentoTemporada.java      ← STRATEGY: % configurable
│   │       ├── EstadoDisponibilidad.java    ← Interfaz STATE
│   │       ├── EstadoDisponible.java        ← STATE concreto
│   │       ├── EstadoReservado.java         ← STATE concreto
│   │       ├── EstadoAlquilado.java         ← STATE concreto
│   │       ├── EstadoEnReparacion.java      ← STATE concreto
│   │       ├── ObservadorReserva.java       ← Interfaz OBSERVER
│   │       ├── ObservadorNotificacionSms.java ← OBSERVER: notifica por SMS
│   │       ├── ObservadorRegistroLog.java   ← OBSERVER: registra en log
│   │       └── GestorEventosReserva.java    ← Publicador del OBSERVER
│   │
│   ├── repositorio/
│   │   ├── InstrumentoRepositorio.java      ← REPOSITORY (JpaRepository)
│   │   ├── ClienteRepositorio.java          ← REPOSITORY (JpaRepository)
│   │   └── ReservaRepositorio.java          ← REPOSITORY + @Query personalizado
│   │
│   ├── servicio/
│   │   ├── InstrumentoServicio.java         ← Capa de Servicios
│   │   ├── ClienteServicio.java             ← Capa de Servicios
│   │   └── ReservaServicio.java             ← Capa de Servicios (orquesta todos los patrones)
│   │
│   └── controlador/
│       ├── InstrumentoControlador.java      ← REST Controller
│       ├── ClienteControlador.java          ← REST Controller
│       ├── ReservaControlador.java          ← REST Controller
│       └── CorsConfig.java                  ← Configuración CORS
│
├── src/main/resources/
│   ├── application.properties               ← Configuración H2 + Spring Boot
│   └── static/
│       └── index.html                       ← Frontend (servido por Spring Boot en /)
│
├── pom.xml                                  ← Dependencias Maven
├── README.md                                ← Este archivo
└── GUION_EXPOSICION.md                      ← Guión completo para la exposición
```

---

## Temas del curso reflejados en el código

| Tema | Archivo(s) |
|------|-----------|
| Modelado de Dominio | `Instrumento.java`, `Cliente.java`, `Reserva.java`, enums |
| Encapsulamiento | `Instrumento.java`, `Cliente.java` — atributos `private` + setters validados |
| Herencia | `GuitarraAcustica/Electrica/Clasica extends Instrumento` |
| Abstracción | `Instrumento.java` — clase `abstract` + métodos `abstract` |
| Polimorfismo | `calcularPrecioAlquiler()` diferente en cada subclase |
| Singleton | `GestorTienda.java` |
| Factory | `InstrumentoFactory.java` |
| Builder | `ReservaBuilder.java` |
| Adapter | `AdaptadorSms.java` + `ServicioSmsLegado.java` |
| Decorator | `InstrumentoConSeguro.java` + `InstrumentoConAfinador.java` |
| Strategy | `EstrategiaDescuento.java` + 3 implementaciones |
| State | 4 clases `Estado*.java` |
| Observer | `GestorEventosReserva.java` + 2 observadores |
| Encapsulamiento de Reglas | `Cliente.puedeHacerNuevaReserva()`, `Reserva.calcularDias()` |
| Repository y Persistencia | 3 interfaces `JpaRepository` |
| Capa de Servicios | `ReservaServicio.crearReserva()` — orquesta 5 patrones |
| Spring Boot REST | 3 controladores `@RestController` con verbos HTTP |
| Evolución incremental | Diseño abierto a extensión en cada capa |

---

## Endpoints de la API REST

### Instrumentos
| Método | URL | Descripción |
|--------|-----|-------------|
| GET | /api/instrumentos | Lista todos los instrumentos |
| GET | /api/instrumentos/disponibles | Solo los disponibles |
| GET | /api/instrumentos/{id} | Buscar por ID |
| POST | /api/instrumentos | Registrar nuevo (activa Factory) |
| PUT | /api/instrumentos/{id}/reservar | Cambiar a RESERVADO (activa State) |
| PUT | /api/instrumentos/{id}/alquilar | Cambiar a ALQUILADO (activa State) |
| PUT | /api/instrumentos/{id}/devolver | Registrar devolución (activa State) |
| PUT | /api/instrumentos/{id}/reparacion | Enviar a reparación (activa State) |
| PUT | /api/instrumentos/{id}/reparacion/terminar | Terminar reparación (activa State) |

### Clientes
| Método | URL | Descripción |
|--------|-----|-------------|
| GET | /api/clientes | Lista todos los clientes |
| GET | /api/clientes/{id} | Buscar por ID |
| GET | /api/clientes/buscar?nombre=X | Buscar por nombre |
| POST | /api/clientes | Registrar nuevo cliente |
| PUT | /api/clientes/{id}/correo | Actualizar correo |

### Reservas
| Método | URL | Descripción |
|--------|-----|-------------|
| GET | /api/reservas | Lista todas las reservas |
| GET | /api/reservas/activas | Solo las activas |
| GET | /api/reservas/cliente/{id} | Reservas de un cliente |
| POST | /api/reservas | Crear reserva (activa Builder+State+Strategy+Observer) |
| PUT | /api/reservas/{id}/devolucion | Registrar devolución |
| DELETE | /api/reservas/{id} | Cancelar reserva |

---

## Ejemplos JSON para probar la API

### Registrar instrumento
```json
POST /api/instrumentos
{
  "tipo": "GUITARRA_ACUSTICA",
  "marca": "Yamaha",
  "modelo": "F310",
  "numeroDeSerie": "YAM-100",
  "precioPorDia": 25000
}
```

### Registrar cliente
```json
POST /api/clientes
{
  "nombre": "Juan Pérez",
  "numeroIdentificacion": "12345678",
  "celular": "3001234567"
}
```

### Crear reserva — activa Builder + State + Strategy + Observer
```json
POST /api/reservas
{
  "clienteId": 1,
  "instrumentoId": 1,
  "fechaInicio": "2024-02-01",
  "fechaDevolucion": "2024-02-08",
  "tipoDescuento": "VIP"
}
```
> Al crear una reserva, en la consola del backend aparecen automáticamente
> el SMS del Observer y el Log de auditoría.

### Registrar devolución
```json
PUT /api/reservas/1/devolucion
{
  "estadoDevolucion": "RAYADO"
}
```

---

## Datos cargados automáticamente al arrancar

Al iniciar, `DatosIniciales.java` registra automáticamente:

**5 instrumentos:**
| ID | Tipo | Marca | Modelo | Precio/día |
|----|------|-------|--------|-----------|
| 1 | Guitarra Acústica | Yamaha | F310 | $25.000 |
| 2 | Guitarra Eléctrica | Fender | Stratocaster | $45.000 |
| 3 | Guitarra Clásica | Admira | Malaga | $20.000 |
| 4 | Guitarra Acústica | Cort | AD810 | $22.000 |
| 5 | Guitarra Eléctrica | Gibson | Les Paul | $60.000 |

**3 clientes:**
| ID | Nombre | Cédula | Celular |
|----|--------|--------|---------|
| 1 | Carlos Ramírez | 10001234 | 3001234567 |
| 2 | María González | 10005678 | 3109876543 |
| 3 | Andrés Torres | 10009999 | 3154567890 |
