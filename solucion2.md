# Solución — Ejercicio 2: Tienda Virtual

> Esta solución fue desarrollada usando **Claude Code en la app de escritorio con el modelo Claude Sonnet 4.6**.

---

## Registro de prompts de la sesión

### Prompt 1

```
Eres un ingeniero de software senior especializado en planificación y arquitectura de proyectos.

## Contexto
Un grupo de estudiantes está evaluando cómo los desarrolladores adoptan herramientas de IA en su flujo de trabajo. Para ello, clonamos este repositorio y debemos resolver el Ejercicio 2: Tienda Virtual, descrito en el README, trabajando de forma natural como lo haríamos en un proyecto real.

## Tareas

### 1. Análisis del repositorio
Lee y comprende completamente el repositorio: estructura de archivos, README, código existente y cualquier documentación disponible. **Enfócate únicamente en el Ejercicio 2: Tienda Virtual.** No modifiques, referencias ni mezcles nada relacionado con el Ejercicio 1.

### 2. Crear `ROADMAP2.md`
Genera un archivo `ROADMAP2.md` en la raíz del proyecto con:
- Las fases necesarias para implementar la solución al Ejercicio 2: Tienda Virtual
- Por cada fase: descripción clara, objetivos y el nombre de la rama Git correspondiente (ej. `feat/fase-1-setup`)
- Orden lógico de implementación con dependencias entre fases si aplica

### 3. Crear `solucion2.md`
Genera un archivo `solucion2.md` en la raíz del proyecto que incluya:
- Todos los prompts que te envíe durante esta sesión (pégalos textualmente a medida que lleguen)
- Una sección inicial que indique: *"Esta solución fue desarrollada usando Claude Code en la app de escritorio con el modelo Claude Sonnet 4.6"*

## Consideraciones
- Trabaja como lo haría un desarrollador senior en un entorno real
- Enfócate **exclusivamente** en el Ejercicio 2: Tienda Virtual; ignora por completo el Ejercicio 1 y cualquier trabajo previo relacionado con él
- Sé explícito en los nombres de ramas y en la justificación de cada fase
- Mantén `solucion2.md` actualizado con cada prompt nuevo que recibas en la sesión
```

---

### Prompt 2

```
Implementa la fase 1
```

---

## Análisis y diagnóstico — Fase 1

### 1. Patrones de diseño identificados

#### Abstract Factory
| Elemento | Clase/Interfaz | Estado |
|----------|---------------|--------|
| Interfaz fábrica | `PaymentFactory` | Faltaba — creada en fix previo |
| Fábricas concretas | `CreditCardFactory`, `PaypalFactory`, `CryptoFactory` | **Mal implementadas**: extienden `PaymentMethod` en vez de implementar `PaymentFactory`. Son métodos de pago, no fábricas. |
| Consumidor | `ECIPayment.processPayment(PaymentFactory, ...)` | Correcto — espera una `PaymentFactory` por parámetro |

#### Observer
| Elemento | Clase/Interfaz | Estado |
|----------|---------------|--------|
| Sujeto | `ECIPayment` | Correcto — gestiona lista de observadores |
| Interfaz observador | `PaymentObserver` | Correcto |
| Observador concreto | `PaymentEventObserver` | Tenía bug de import (corregido) |
| Notificados | `Inventory`, `Facturation`, `Notification` | Correctos |

---

### 2. Errores de compilación catalogados

| # | Archivo | Error | Causa | Estado |
|---|---------|-------|-------|--------|
| E1 | `ECIPayment.java` | `cannot find symbol: PaymentFactory` | La interfaz no existía | ✅ Corregido (interfaz creada) |
| E2 | `PaymentEventObserver.java` | Métodos `sendConfirmationEmail` / `sendFailureNotification` no encontrados | Import incorrecto: `javax.management.Notification` en lugar de la clase local | ✅ Corregido |
| E3 | `PaymentMethod.java` | `customerID` siempre queda `null` | El constructor recibe `transactionID` como segundo parámetro pero lo asigna a `this.customerID = customerID` usando una variable que no existe | ⏳ Pendiente Fase 2 |
| E4 | `CryptoFactory.java` | `this.token = token` asigna un campo que no proviene del constructor | `token` no es parámetro del constructor de `CryptoFactory` | ⏳ Pendiente Fase 2 |
| E5 | `CreditCardFactory`, `PaypalFactory`, `CryptoFactory` | No implementan `PaymentFactory` | Responsabilidad mezclada: son métodos de pago que se hacen pasar por fábricas | ⏳ Pendiente Fase 3 |

---

### 3. Evaluación del diagrama de contexto (`docs/imagenes/contexto.png`)

**Veredicto: suficiencia PARCIAL — requiere mejoras.**

**Lo que el diagrama muestra correctamente:**
- Los actores principales: Cliente, Sistema de Pago, tres módulos (Inventario, Facturación, Notificación).
- El flujo de notificación post-pago hacia los tres módulos.
- Que el módulo de notificación envía confirmación al cliente.

**Lo que el diagrama omite o confunde:**
1. No representa los métodos de pago disponibles (Tarjeta de Crédito, PayPal, Criptomonedas) — pieza central del sistema.
2. No muestra el patrón Abstract Factory ni cómo `ECIPayment` recibe una fábrica para crear el método de pago.
3. El nodo "Notificación" (evento/trigger) y el "Módulo Notificación" (servicio de correo) tienen nombres ambiguos y se prestan a confusión.
4. La descripción del Módulo Notificación está truncada en el texto ("des").
5. El patrón Observer no está etiquetado explícitamente.

**Cambios sugeridos** (documentados aquí — no se modifican las imágenes):
- Añadir nodo "Métodos de Pago" con sus tres variantes como entrada al Sistema de Pago.
- Etiquetar el flujo de notificación como «patrón Observer».
- Renombrar el nodo genérico "Notificación" a "Evento de Pago" para diferenciarlo del módulo.
- Añadir `PaymentFactory` al diagrama de clases (`docs/uml/clases.png`).

---

### 4. Evaluación del diagrama de clases (`docs/uml/clases.png`)

El diagrama muestra `CryptoFactory`, `CreditCardFactory` y `PaypalFactory` exteniendo `PaymentMethod`, lo cual refleja el código actual pero **no el patrón Abstract Factory correcto**. La interfaz `PaymentFactory` no aparece en el diagrama. El diagrama de casos de uso (`users.png`) es pertinente y suficiente para entender los actores del sistema.

---

### Prompt 3

```
ahora implementa la fase 2
```

---

## Correcciones aplicadas — Fase 2

| Error | Archivo | Corrección |
|-------|---------|-----------|
| E1 | `ECIPayment.java` | Creada interfaz `PaymentFactory` faltante |
| E2 | `PaymentEventObserver.java` | Import `javax.management.Notification` → `eci.edu...Notification` |
| E3 | `PaymentMethod.java` | Parámetro renombrado de `transactionID` a `customerId`; asignación corregida a `this.customerID = customerId` |
| E4 | `CryptoFactory.java` | Eliminada línea `this.token = token` (auto-asignación de campo nulo) y campo `token` sin uso |

**Resultado:** `mvn compile` finaliza sin errores.

---

### Prompt 4

```
implementa la fase 3
```

---

## Abstract Factory completado — Fase 3

### Problema encontrado
`CreditCardFactory`, `PaypalFactory` y `CryptoFactory` extendían `PaymentMethod` (eran métodos de pago) pero tenían nombre de fábricas y **no implementaban `PaymentFactory`**. `ECIPayment` no podía usarlas como fábricas.

### Solución aplicada: separación de responsabilidades

#### Métodos de pago (extienden `PaymentMethod`)
| Clase anterior | Clase nueva | Cambio |
|----------------|------------|--------|
| `CreditCardFactory` | `CreditCardPayment` | Renombrada, lógica intacta |
| `PaypalFactory` | `PaypalPayment` | Renombrada, lógica intacta |
| `CryptoFactory` | `CryptoPayment` | Renombrada, lógica intacta |

#### Fábricas concretas (implementan `PaymentFactory`)
| Clase nueva | Parámetros pre-configurados | Crea |
|-------------|----------------------------|------|
| `CreditCardPaymentFactory` | `cardNumber`, `cardHolderName`, `expirationDate`, `cvv`, `address` | `CreditCardPayment` |
| `PaypalPaymentFactory` | `email`, `authToken` | `PaypalPayment` |
| `CryptoPaymentFactory` | `walletAddress`, `cryptoType`, `walletBalance` | `CryptoPayment` |

### Por qué este diseño respeta OCP
Agregar un nuevo método de pago (ej. `BankTransferPayment`) solo requiere:
1. Crear `BankTransferPayment extends PaymentMethod`
2. Crear `BankTransferPaymentFactory implements PaymentFactory`

No se toca `ECIPayment`, ni `PaymentObserver`, ni ningún código existente.

**Resultado:** `mvn compile` en verde. Patrón Abstract Factory completo y correcto.

---

*Este archivo se actualizará con cada nuevo prompt recibido durante la sesión.*
