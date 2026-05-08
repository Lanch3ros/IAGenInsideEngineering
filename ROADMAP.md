# ROADMAP — Ejercicio 1: El Videoclub de Don Mario

> Proyecto: IAGen Inside Engineering  
> Lenguaje: Java 17 / Maven  
> Objetivo: implementar el sistema de alquiler de películas descrito en el Problema #1 del README aplicando patrones de diseño, principios SOLID, polimorfismo y encapsulamiento.

---

## Visión general

```
Fase 1 ──► Fase 2 ──► Fase 3 ──► Fase 4 ──► Fase 5 ──► Fase 6
 Setup     Modelo     Lógica    Sistema     Tests      Docs
```

---

## Fase 1 — Configuración y análisis de arquitectura

**Rama:** `feat/fase-1-setup`

### Descripción
Preparar el entorno de desarrollo y definir la arquitectura de la solución antes de escribir código de negocio.

### Objetivos
- Leer y comprender completamente el enunciado del Problema #1.
- Identificar los patrones de diseño a aplicar (Factory Method para crear los tipos de película; Strategy para calcular precios según membresía).
- Mapear los principios SOLID relevantes:
  - **SRP**: cada clase tiene una única responsabilidad (película, cliente, recibo, calculador de precio).
  - **OCP**: se pueden agregar nuevos tipos de película o membresías sin modificar código existente.
  - **LSP**: `PhysicalMovie` y `DigitalMovie` son sustituibles por `Movie`.
  - **DIP**: la lógica de alquiler depende de abstracciones, no de implementaciones concretas.
- Crear el paquete `eci.edu.byteProgramming.ejercicio.paper.videoclub` para no mezclar con el código del Problema #2.
- Actualizar `SOLUCION.md` con el análisis previo.

### Dependencias
Ninguna — es el punto de partida.

---

## Fase 2 — Modelo de dominio (películas y clientes)

**Rama:** `feat/fase-2-domain-model`

### Descripción
Implementar las entidades centrales del dominio: la jerarquía de películas y el modelo de cliente.

### Objetivos
- Crear la interfaz (o clase abstracta) `Movie` con los atributos: `title`, `price`, `available`.
- Implementar `PhysicalMovie` y `DigitalMovie` extendiendo `Movie` (polimorfismo).
- Crear el enum `MembershipType` con los valores `BASIC` y `PREMIUM`.
- Crear la clase `Customer` con `name` y `membershipType`.
- Crear `MovieCatalog`: colección inicial de las 4 películas definidas en el README y método para consultar disponibilidad.

### Dependencias
Fase 1 completada.

---

## Fase 3 — Lógica de negocio: precios y descuentos

**Rama:** `feat/fase-3-business-logic`

### Descripción
Implementar la estrategia de cálculo de precios según membresía usando el patrón **Strategy**, manteniendo el principio OCP.

### Objetivos
- Crear la interfaz `PricingStrategy` con el método `double calculate(double basePrice)`.
- Implementar `BasicPricingStrategy`: devuelve el precio sin modificación.
- Implementar `PremiumPricingStrategy`: aplica un descuento del 20 %.
- Crear `PricingContext` (o integrarlo en `RentalService`) para seleccionar la estrategia según la membresía del cliente.
- Garantizar que agregar una nueva membresía solo requiere una nueva clase, sin tocar las existentes (OCP).

### Dependencias
Fase 2 completada.

---

## Fase 4 — Sistema de alquiler y generación de recibo

**Rama:** `feat/fase-4-rental-system`

### Descripción
Implementar el flujo completo de alquiler: selección de películas, validación de disponibilidad, cálculo del total y emisión del recibo por consola.

### Objetivos
- Crear `RentalService` que orquesta:
  1. Mostrar el catálogo con numeración.
  2. Leer la selección del usuario (números separados por coma) desde `Scanner`.
  3. Validar disponibilidad de cada película elegida.
  4. Aplicar la `PricingStrategy` correspondiente.
  5. Calcular subtotal, descuento y total.
- Crear `Receipt` (o método `printReceipt`) que imprime el recibo con el formato exacto indicado en el README:
  ```
  --- RECIBO DE ALQUILER ---
  Cliente: <membresía>
  Películas:
   - <título> (<tipo>) - $<precio>
  Subtotal: $<X>
  Descuento (20%): $<Y>    ← solo si aplica
  Total a pagar: $<Z>
  --------------------------
  ¡Disfrute su película!
  ```
- Actualizar `Application.java` (o crear `VideoclubApplication.java`) para ejecutar el sistema desde `main()`.
- Verificar el caso de ejemplo del README (membresía Premium, películas 1 y 3).

### Dependencias
Fases 2 y 3 completadas.

---

## Fase 5 — Pruebas unitarias y validación

**Rama:** `feat/fase-5-testing`

### Descripción
Escribir y ejecutar pruebas unitarias para garantizar que el sistema funciona correctamente bajo distintos escenarios.

### Objetivos
- Extender `auxiliaryTest.java` con casos de prueba usando JUnit 5:
  - Cálculo de precio para membresía Basic (sin descuento).
  - Cálculo de precio para membresía Premium (20 % de descuento).
  - Validación de disponibilidad: película disponible vs. no disponible.
  - Selección de múltiples películas y cálculo del total correcto.
  - Caso borde: selección de película no disponible ("El Padrino").
- Ejecutar `mvn test` y corregir cualquier falla hasta obtener todos los tests en verde.
- Verificar que la cobertura de Jacoco supera el 85 % exigido en `pom.xml`.

### Dependencias
Fase 4 completada.

---

## Fase 6 — Documentación y cierre

**Rama:** `feat/fase-6-docs`

### Descripción
Consolidar la documentación de la solución y preparar la entrega final.

### Objetivos
- Completar `SOLUCION.md`:
  - Todos los prompts utilizados durante la sesión.
  - Explicación de los patrones de diseño aplicados (Factory Method + Strategy).
  - Principios SOLID identificados y cómo se aplican en cada clase.
  - Capturas o trazas de consola que evidencien la ejecución exitosa (caso del README).
- Revisar que el código compila sin warnings relevantes: `mvn compile`.
- Hacer merge de todas las ramas de feature hacia `main` siguiendo el orden de las fases.
- Tagging de la versión: `git tag v1.0.0-ejercicio1`.

### Dependencias
Fase 5 completada.

---

## Resumen de ramas

| Fase | Rama                         | Entregable principal                              |
|------|------------------------------|---------------------------------------------------|
| 1    | `feat/fase-1-setup`          | Arquitectura definida, paquete creado             |
| 2    | `feat/fase-2-domain-model`   | `Movie`, `PhysicalMovie`, `DigitalMovie`, `Customer`, `MovieCatalog` |
| 3    | `feat/fase-3-business-logic` | `PricingStrategy`, `BasicPricingStrategy`, `PremiumPricingStrategy` |
| 4    | `feat/fase-4-rental-system`  | `RentalService`, `Receipt`, flujo de consola completo |
| 5    | `feat/fase-5-testing`        | Tests JUnit 5 en verde, cobertura ≥ 85 %         |
| 6    | `feat/fase-6-docs`           | `SOLUCION.md` completo, tag `v1.0.0-ejercicio1`  |
