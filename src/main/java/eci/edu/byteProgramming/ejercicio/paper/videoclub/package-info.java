/**
 * Ejercicio 1 — El Videoclub de Don Mario.
 *
 * Arquitectura definida en Fase 1:
 *
 * Patrones de diseño aplicados:
 *   - Factory Method: MovieFactory crea instancias de PhysicalMovie o DigitalMovie
 *     sin exponer la lógica de construcción al RentalService.
 *   - Strategy: PricingStrategy encapsula el algoritmo de precio según membresía,
 *     permitiendo añadir nuevos tipos sin modificar código existente (OCP).
 *
 * Principios SOLID:
 *   SRP  — cada clase tiene una única razón de cambio:
 *            Movie modela la película, PricingStrategy calcula precio,
 *            RentalService orquesta el alquiler, Receipt genera el recibo.
 *   OCP  — añadir un nuevo tipo de membresía o película solo requiere
 *            una nueva clase que implemente la interfaz correspondiente.
 *   LSP  — PhysicalMovie y DigitalMovie son intercambiables donde se usa Movie.
 *   ISP  — las interfaces son pequeñas y cohesivas (Movie, PricingStrategy).
 *   DIP  — RentalService depende de Movie y PricingStrategy (abstracciones),
 *            no de PhysicalMovie ni de BasicPricingStrategy (implementaciones).
 *
 * Estructura de clases prevista:
 *   Movie (abstract)
 *     ├── PhysicalMovie
 *     └── DigitalMovie
 *   MembershipType (enum)      — BASIC, PREMIUM
 *   Customer
 *   MovieCatalog
 *   PricingStrategy (interface)
 *     ├── BasicPricingStrategy
 *     └── PremiumPricingStrategy
 *   RentalService
 *   Receipt
 */
package eci.edu.byteProgramming.ejercicio.paper.videoclub;
