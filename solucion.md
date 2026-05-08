# Solución — IAGen Inside Engineering

> Esta solución fue desarrollada usando **Claude Code en la app de escritorio con el modelo Claude Sonnet 4.6**.

---

## Registro de prompts de la sesión

### Prompt 1

```
Eres un ingeniero de software senior especializado en planificación y arquitectura de proyectos.

## Contexto
Un grupo de estudiantes está evaluando cómo los desarrolladores adoptan herramientas de IA en su flujo de trabajo. Para ello, clonamos este repositorio y debemos resolver el Ejercicio 1 descrito en el README, trabajando de forma natural como lo haríamos en un proyecto real.

## Tareas

### 1. Análisis del repositorio
Lee y comprende completamente el repositorio: estructura de archivos, README, código existente y cualquier documentación disponible.

### 2. Crear `ROADMAP.md`
Genera un archivo `ROADMAP.md` en la raíz del proyecto con:
- Las fases necesarias para implementar la solución al Ejercicio 1
- Por cada fase: descripción clara, objetivos y el nombre de la rama Git correspondiente (ej. `feat/fase-1-setup`)
- Orden lógico de implementación con dependencias entre fases si aplica

### 3. Crear `solucion.md`
Genera un archivo `solucion.md` en la raíz del proyecto que incluya:
- Todos los prompts que te envíe durante esta sesión (pégalos textualmente a medida que lleguen)
- Una sección inicial que indique: *"Esta solución fue desarrollada usando Claude Code en la app de escritorio con el modelo Claude Sonnet 4.6"*

## Consideraciones
- Trabaja como lo haría un desarrollador senior en un entorno real
- Sé explícito en los nombres de ramas y en la justificación de cada fase
- Mantén `solucion.md` actualizado con cada prompt nuevo que recibas en la sesión
```

---

### Prompt 2

```
Implementa la fase 1
```

---

## Análisis de arquitectura — Fase 1

### Patrones de diseño seleccionados

| Patrón | Aplicación en el Videoclub |
|--------|---------------------------|
| **Factory Method** | `MovieFactory` crea instancias de `PhysicalMovie` o `DigitalMovie` según el tipo de entrada, sin exponer la lógica de construcción al servicio de alquiler. |
| **Strategy** | `PricingStrategy` encapsula el algoritmo de cálculo de precio. `BasicPricingStrategy` devuelve el precio sin cambios; `PremiumPricingStrategy` aplica el 20 % de descuento. El `RentalService` recibe la estrategia correcta según la membresía del cliente. |

### Principios SOLID identificados

| Principio | Cómo se aplica |
|-----------|---------------|
| **SRP** | `Movie` modela la película; `PricingStrategy` calcula el precio; `RentalService` orquesta el alquiler; `Receipt` genera el recibo. Cada clase cambia por una única razón. |
| **OCP** | Agregar una nueva membresía o tipo de película solo requiere una nueva clase que implemente la interfaz. No se modifica código existente. |
| **LSP** | `PhysicalMovie` y `DigitalMovie` son sustitutos completos de `Movie` en cualquier contexto que use la abstracción. |
| **ISP** | Las interfaces son pequeñas y cohesivas: `Movie` (contrato de película), `PricingStrategy` (contrato de precio). |
| **DIP** | `RentalService` depende de `Movie` y `PricingStrategy` (abstracciones), no de `PhysicalMovie` ni de `BasicPricingStrategy` (implementaciones concretas). |

### Estructura de paquete creada

```
src/main/java/eci/edu/byteProgramming/ejercicio/paper/
├── Application.java          (Spring Boot — Problema #2, sin tocar)
├── util/                     (Problema #2 — sin tocar)
└── videoclub/                ← paquete nuevo para Ejercicio 1
    └── package-info.java     (documenta arquitectura de la fase)
```

### Prompt 3

```
Ahora haz el merge a develop, despues implementa la fase 2
```

---

*Este archivo se actualizará con cada nuevo prompt recibido durante la sesión.*
