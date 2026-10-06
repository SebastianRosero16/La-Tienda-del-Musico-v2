package com.tienda.instrumentos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * =====================================================================
 * PUNTO DE ENTRADA DE LA APLICACION SPRING BOOT
 * =====================================================================
 *
 * @SpringBootApplication es una anotacion que activa tres cosas:
 *   1. @Configuration      -> permite definir beans de Spring
 *   2. @EnableAutoConfiguration -> Spring configura todo automaticamente
 *   3. @ComponentScan      -> Spring busca y registra todos los
 *                             @Service, @Repository, @RestController
 *                             en el paquete y subpaquetes
 *
 * Con este unico archivo, Spring Boot levanta:
 *   - El servidor web embebido (Tomcat)
 *   - La base de datos H2 en memoria
 *   - Todos los endpoints REST
 *   - Toda la inyeccion de dependencias
 *
 * Para ejecutar: mvn spring-boot:run
 * La app queda disponible en: http://localhost:8080
 */
@SpringBootApplication
public class InstrumentosMusicalesApp {

    public static void main(String[] args) {
        SpringApplication.run(InstrumentosMusicalesApp.class, args);
        System.out.println("\n=========================================================");
        System.out.println("  Sistema de Gestion de Instrumentos Musicales");
        System.out.println("  Aplicacion iniciada correctamente!");
        System.out.println("  API disponible en: http://localhost:8080/api");
        System.out.println("  Consola H2:        http://localhost:8080/h2-console");
        System.out.println("=========================================================\n");
    }
}
