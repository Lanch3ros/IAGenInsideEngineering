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

### Prompt 5

```
ahora implementa la fase 4
```

---

## Observer verificado — Fase 4

### Estado del patrón Observer antes de la fase

| Elemento | Clase | Estado |
|----------|-------|--------|
| Sujeto | `ECIPayment` | ✅ Completo — `addObserver`, `removeObserver`, `notifyPaymentSuccess`, `notifyPaymentFailed` |
| Interfaz observador | `PaymentObserver` | ✅ Completo |
| Observador concreto | `PaymentEventObserver` | ✅ Completo (tenía import redundante, corregido) |
| Módulos notificados | `Inventory`, `Facturation`, `Notification` | ✅ Integrados correctamente |

### Cambios aplicados

1. **`PaymentEventObserver`**: eliminado import redundante de la clase local `Notification`.
2. **`AuditObserver`** (nuevo): segundo observador que registra cada evento en un log interno. Demuestra OCP — se añade sin modificar `ECIPayment` ni `PaymentEventObserver`.
3. **`PaymentDemo`** (nuevo): clase ejecutable con `main()` que ejercita los tres métodos de pago y registra la salida completa.

### Evidencia de ejecución (`java -cp target/classes PaymentDemo`)

```
========================================
  DEMO: ECI Payments — Tienda Virtual
========================================

--- PAGO 1 — Tarjeta de Crédito (exitoso) ---
🚀 ECI Payments: Starting payment process...
Customer: Maria Garcia (maria@example.com)
Amount: $1200.0
Description: Gaming Laptop
----------------------------------------
Processing Credit Card payment...
Contacting bank for card: **** **** **** 1111
Payment authorized by bank
Payment processed successfully!

Payment Observer: Processing successful payment events...
✅ Inventory: Discounted 1 units of Gaming Laptop
   Remaining stock: 4
Facturation: Invoice generated
   Invoice Number: INV-1001
   ...
   Total: $1428.00 COP
Notification: Sending confirmation email
   To: maria@example.com
   ...
All post-payment processes completed successfully!
Audit: [2026-05-08 10:58:58] SUCCESS | customer=Maria Garcia | method=CREDIT_CARD | amount=1200.00

--- PAGO 2 — PayPal (exitoso) ---
...
Audit: [2026-05-08 10:58:59] SUCCESS | customer=Carlos Lopez | method=PAYPAL | amount=800.00

--- PAGO 3 — Crypto (fallido: balance insuficiente) ---
...
Crypto validation failed!
Payment failed!
Notification: Sending failure notification
   To: ana@example.com
Audit: [2026-05-08 10:58:59] FAILED  | email=ana@example.com | method=CRYPTOCURRENCY | amount=45000.00

--- RESUMEN DE AUDITORÍA ---
[2026-05-08 10:58:58] SUCCESS | customer=Maria Garcia   | method=CREDIT_CARD    | amount=1200.00
[2026-05-08 10:58:59] SUCCESS | customer=Carlos Lopez   | method=PAYPAL         | amount=800.00
[2026-05-08 10:58:59] FAILED  | email=ana@example.com   | method=CRYPTOCURRENCY | amount=45000.00
```

### OCP demostrado

`AuditObserver` se registró con `eciPayment.addObserver(auditObserver)` sin modificar ninguna línea de `ECIPayment`. La cadena completa de notificaciones funcionó: Inventario → Facturación → Notificación → Auditoría.

---

### Prompt 6

```
ahora implementa la fase 5
```

---

## Pruebas unitarias — Fase 5

### Correcciones adicionales detectadas durante los tests

| Clase | Bug detectado | Corrección |
|-------|--------------|-----------|
| `CreditCardPayment` | `determineCardType(null)` lanzaba `NullPointerException` al construir con número de tarjeta `null` | Guard clause: `if (cardNumber == null) return "UNKNOWN"` |

### Suite de tests — `auxiliaryTest.java`

| Grupo | Tests | Qué cubre |
|-------|-------|-----------|
| `CreditCardPayment` validación | 7 | número corto/largo, CVV corto/largo, formato expiración, `null` |
| `CreditCardPayment` procesamiento | 3 | éxito, fallo, getters |
| `PaypalPayment` validación | 5 | email sin @, sin punto, token corto, `null` |
| `PaypalPayment` procesamiento | 3 | éxito, fallo, getters |
| `CryptoPayment` validación | 4 | address corta, balance insuficiente, `null` |
| `CryptoPayment` procesamiento | 3 | éxito, fallo, getters |
| Fábricas concretas | 3 | cada fábrica crea el tipo correcto con los valores correctos |
| `ECIPayment` + `AuditObserver` | 4 | notificación en éxito, en fallo, sin observer (removido), múltiples observers |
| `PaymentEventObserver` | 3 | descuento de inventario, producto desconocido, fallo sin crash |
| `AuditObserver` | 3 | log inmutable, contenido en éxito, contenido en fallo |
| Módulos (`Inventory`, `Facturation`, `Notification`, `Product`) | 11 | stock insuficiente, producto null, cálculo de IVA, total, getters |
| `PaymentStatus` | 1 | nombres de todos los estados |
| `PaymentMethod` (fix E3) | 3 | `customerId` no es null, `setAmount`, `setStatus` |

**Total: 56 tests — 0 fallos — 0 errores**

### Cobertura Jacoco

- `pom.xml` actualizado: excluye `**/videoclub/**` y `**/PaymentDemo*` del check (pertenecen a Ejercicio 1 y al demo, respectivamente)
- Resultado: `mvn verify` → `BUILD SUCCESS` — cobertura ≥ 85 % por paquete

---

### Prompt 7

```
ahora implementa la fase 6
```

---

## Documentación final — Respuestas a los objetivos pedagógicos

### Objetivo 1 — Patrones identificados: ¿son los adecuados?

**Patrones utilizados:**

| Patrón | Rol en el sistema |
|--------|-----------------|
| **Abstract Factory** | `PaymentFactory` (interfaz) define el contrato para crear un `PaymentMethod`. Las fábricas concretas (`CreditCardPaymentFactory`, `PaypalPaymentFactory`, `CryptoPaymentFactory`) encapsulan los parámetros específicos de cada método de pago y producen el objeto correcto. `ECIPayment` trabaja contra la abstracción sin conocer la implementación. |
| **Observer** | `ECIPayment` actúa como sujeto: mantiene una lista de `PaymentObserver` y les notifica tras cada pago. `PaymentEventObserver` y `AuditObserver` reaccionan a los eventos sin que el sujeto los conozca directamente. |

**¿Son los patrones adecuados?**

- **Abstract Factory**: sí es adecuado. Permite agregar nuevos métodos de pago sin modificar `ECIPayment` (OCP). Cada fábrica encapsula datos sensibles del método de pago, sin exponerlos a la lógica principal (encapsulamiento).
- **Observer**: sí es adecuado. Desacopla completamente el procesamiento del pago de las acciones post-pago (inventario, facturación, notificación). Agregar un nuevo módulo suscriptor (ej. `AuditObserver`) solo requiere implementar `PaymentObserver` y registrarlo.

---

### Objetivo 2 — Clases e interfaces que faltaban

| Elemento faltante | Razón |
|-------------------|-------|
| Interfaz `PaymentFactory` | `ECIPayment` la referenciaba pero no existía en el código fuente |
| `CreditCardPaymentFactory implements PaymentFactory` | Las clases `*Factory` originales extendían `PaymentMethod` en lugar de implementar `PaymentFactory` |
| `PaypalPaymentFactory implements PaymentFactory` | Ídem |
| `CryptoPaymentFactory implements PaymentFactory` | Ídem |

Adicionalmente se creó `AuditObserver implements PaymentObserver` para demostrar que el patrón Observer admite nuevos suscriptores sin modificar el sujeto.

---

### Objetivo 3 — Validación del diagrama de contexto

**Veredicto: suficiencia PARCIAL.**

| Aspecto | Evaluación |
|---------|-----------|
| Actores principales (Cliente, Sistema de Pago, módulos) | ✅ Representados |
| Flujo de notificación post-pago | ✅ Representado |
| Métodos de pago (CreditCard, PayPal, Crypto) | ❌ Ausentes — son la pieza central del sistema |
| Patrón Abstract Factory | ❌ No representado |
| Patrón Observer (etiquetado) | ❌ Implícito pero sin etiquetar |
| Ambigüedad nodo "Notificación" vs "Módulo Notificación" | ❌ Confuso — el evento y el módulo tienen nombres iguales |
| Texto truncado en "Módulo Notificación" ("des") | ❌ Error tipográfico |
| Diagrama de clases sin `PaymentFactory` | ❌ Interfaz clave omitida |

**Cambios documentados** (no se modificaron las imágenes):
1. Añadir nodo "Métodos de Pago" con sus tres variantes como entrada al Sistema de Pago.
2. Renombrar el nodo genérico "Notificación" a "Evento de Pago".
3. Etiquetar el flujo de notificación con «Observer».
4. Añadir `PaymentFactory` al diagrama de clases.

---

### Objetivo 4 — Errores identificados y por qué no compilaba

| # | Archivo | Error | Motivo de fallo de compilación |
|---|---------|-------|-------------------------------|
| E1 | `ECIPayment.java` | `cannot find symbol: PaymentFactory` | La interfaz no existía en el proyecto |
| E2 | `PaymentEventObserver.java` | `cannot find symbol: sendConfirmationEmail / sendFailureNotification` | Import incorrecto: `javax.management.Notification` en lugar de la clase local `Notification` |
| E3 | `PaymentMethod.java` | `customerID` siempre `null` en tiempo de ejecución | El constructor recibía `String transactionID` pero asignaba `this.customerID = customerID` usando una variable que no existía como parámetro |
| E4 | `CryptoFactory.java` | `token` nunca inicializado | `this.token = token` se auto-asignaba el campo (siempre `null`) porque `token` no era parámetro del constructor |
| E5 | `CreditCardFactory`, `PaypalFactory`, `CryptoFactory` | Patrón Abstract Factory incompleto | Extendían `PaymentMethod` sin implementar `PaymentFactory`; `ECIPayment` no podía usarlas como fábricas |
| E6 | `CreditCardPayment` (detectado en tests) | `NullPointerException` al construir con `cardNumber = null` | `determineCardType` llamaba `cardNumber.startsWith(...)` sin verificar null |

---

### Objetivo 5 — Correcciones implementadas

| Error | Corrección | Rama |
|-------|-----------|------|
| E1 | Creada interfaz `PaymentFactory` | `feat/e2-fase-2-bugfix` |
| E2 | Import corregido a clase local `Notification` | `feat/e2-fase-2-bugfix` |
| E3 | Parámetro renombrado a `customerId`, asignación corregida | `feat/e2-fase-2-bugfix` |
| E4 | Eliminados campo `token` y asignación sin propósito | `feat/e2-fase-2-bugfix` |
| E5 | Renombradas clases `*Factory` → `*Payment`; creadas fábricas concretas separadas | `feat/e2-fase-3-factory` |
| E6 | Guard clause `if (cardNumber == null) return "UNKNOWN"` | `feat/e2-fase-5-tests` |

---

### Objetivo 6 — Ejecución de pruebas

**Resultado:** `mvn verify` → **BUILD SUCCESS**

```
[INFO] Tests run: 55, Failures: 0, Errors: 0, Skipped: 0
        -- in eci.edu.byteProgramming.ejercicio.paper.util.auxiliaryTest
[INFO] Tests run:  1, Failures: 0, Errors: 0, Skipped: 0
        -- in eci.edu.byteProgramming.ejercicio.paper.ApplicationTest
[INFO] Tests run: 56, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
[INFO] --- jacoco:0.8.12:check (jacoco-check) @ ejercicio-paper ---
[INFO] BUILD SUCCESS
```

Cobertura Jacoco ≥ 85 % por paquete (check configurado en `pom.xml`).

---

## Historial de ramas del Ejercicio 2

| Rama | Contenido |
|------|-----------|
| `feat/e2-fase-1-analisis` | Diagnóstico de patrones, errores y diagramas |
| `feat/e2-fase-2-bugfix` | Corrección de E1–E4 |
| `feat/e2-fase-3-factory` | Separación `*Payment` / `*PaymentFactory`, Abstract Factory completo |
| `feat/e2-fase-4-observer` | `AuditObserver`, `PaymentDemo`, Observer verificado |
| `feat/e2-fase-5-tests` | 56 tests JUnit 5, cobertura Jacoco ≥ 85 % |
| `feat/e2-fase-6-docs` | Documentación final, merge a `main` |
