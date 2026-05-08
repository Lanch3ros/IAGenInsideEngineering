# ROADMAP2 — Ejercicio 2: Tienda Virtual

> Proyecto: IAGen Inside Engineering  
> Lenguaje: Java 17 / Maven  
> Paquete de trabajo: `eci.edu.byteProgramming.ejercicio.paper.util`  
> Objetivo: diagnosticar, corregir y completar el sistema de pagos multi-método usando los patrones **Abstract Factory** y **Observer**, hasta que el proyecto compile y los tests pasen.

---

## Visión general

```
Fase 1 ──► Fase 2 ──► Fase 3 ──► Fase 4 ──► Fase 5 ──► Fase 6
Análisis   Bugfix    Factory   Observer    Tests      Docs
```

---

## Fase 1 — Análisis y diagnóstico del código existente

**Rama:** `feat/e2-fase-1-analisis`

### Descripción
Auditar el código del paquete `util/` para identificar: qué patrones se usan, qué piezas faltan y qué errores impiden la compilación.

### Objetivos
- **Identificar patrones** implementados (parcialmente):
  - **Abstract Factory**: `PaymentFactory` (interfaz faltante) + `ECIPayment` que la consume.
  - **Observer**: `PaymentObserver` (interfaz), `ECIPayment` (sujeto), `PaymentEventObserver` (observador concreto).
- **Catalogar errores de compilación**:
  1. `ECIPayment` referencia `PaymentFactory` que no existía → interfaz creada en fix previo.
  2. `PaymentEventObserver` importa `javax.management.Notification` en lugar de la clase local → fix previo.
  3. `PaymentMethod` constructor asigna `this.customerID = customerID` pero `customerID` no es parámetro (siempre queda `null`).
  4. `CryptoFactory` asigna `this.token = token` pero `token` no es parámetro del constructor.
  5. `CreditCardFactory`, `PaypalFactory` y `CryptoFactory` extienden `PaymentMethod` pero sus nombres implican que son fábricas; en realidad son los métodos de pago. Deben existir fábricas separadas que implementen `PaymentFactory`.
- **Evaluar el diagrama de contexto** (`docs/imagenes/contexto.png`): documentar si es suficiente y pertinente.
- **Documentar hallazgos** en `solucion2.md`.

### Dependencias
Ninguna — punto de partida.

---

## Fase 2 — Corrección de errores existentes (bugfix)

**Rama:** `feat/e2-fase-2-bugfix`

### Descripción
Reparar los bugs que impiden la compilación y los errores de lógica presentes en el código dado.

### Objetivos
- Corregir `PaymentMethod`: añadir `customerId` como parámetro del constructor y asignarlo correctamente.
- Corregir `CryptoFactory`: eliminar la asignación `this.token = token` ya que `token` no llega por constructor; o añadirlo como parámetro si el diseño lo requiere.
- Verificar que el fix de `PaymentEventObserver` (import de `Notification`) permanece correcto.
- Verificar que la interfaz `PaymentFactory` existe y tiene la firma correcta.
- Ejecutar `mvn compile` y confirmar compilación limpia.

### Dependencias
Fase 1 completada (diagnóstico documentado).

---

## Fase 3 — Completar el patrón Abstract Factory

**Rama:** `feat/e2-fase-3-factory`

### Descripción
Separar la responsabilidad de *crear* un pago de la responsabilidad de *ejecutarlo*, creando fábricas concretas independientes que implementen `PaymentFactory`.

### Objetivos
- Renombrar/refactorizar las clases de pago:
  - `CreditCardFactory` → `CreditCardPayment` (implementa `PaymentMethod`, no es fábrica).
  - `PaypalFactory` → `PaypalPayment`.
  - `CryptoFactory` → `CryptoPayment`.
- Crear fábricas concretas que implementen `PaymentFactory`:
  - `CreditCardPaymentFactory`: recibe datos de tarjeta, devuelve `CreditCardPayment`.
  - `PaypalPaymentFactory`: recibe email y token, devuelve `PaypalPayment`.
  - `CryptoPaymentFactory`: recibe dirección de wallet y balance, devuelve `CryptoPayment`.
- Verificar que `ECIPayment.processPayment(PaymentFactory, ...)` funciona correctamente con las nuevas fábricas.
- Añadir nueva fábrica de ejemplo para confirmar OCP (agregar método de pago sin tocar código existente).

### Dependencias
Fase 2 completada (proyecto compila sin errores).

---

## Fase 4 — Completar y verificar el patrón Observer

**Rama:** `feat/e2-fase-4-observer`

### Descripción
Asegurarse de que la cadena de notificación (Inventario → Facturación → Notificación) funciona correctamente cuando un pago es procesado.

### Objetivos
- Revisar `ECIPayment` como sujeto (Subject): `addObserver`, `removeObserver`, `notifyPaymentSuccess`, `notifyPaymentFailed`.
- Revisar `PaymentEventObserver` como observador concreto: descontar inventario, generar factura, enviar email.
- Verificar que `Inventory`, `Facturation` y `Notification` están correctamente integrados.
- Escribir un flujo de integración manual (`main` de prueba) que procese un pago con las tres notificaciones y mostrar la salida por consola.
- Confirmar que agregar un nuevo observador (ej. `AuditObserver`) no requiere modificar `ECIPayment` (OCP).

### Dependencias
Fase 3 completada (Abstract Factory operativo).

---

## Fase 5 — Pruebas unitarias y cobertura

**Rama:** `feat/e2-fase-5-tests`

### Descripción
Completar y ejecutar el suite de pruebas para validar el sistema de pagos.

### Objetivos
- Extender `auxiliaryTest.java` con casos JUnit 5:
  - Validación de tarjeta de crédito (número válido, CVV, fecha de expiración).
  - Validación de PayPal (email con `@`, token de longitud suficiente).
  - Validación de Crypto (dirección ≥ 26 caracteres, balance suficiente).
  - Procesamiento exitoso y fallido por cada método de pago.
  - Observer notificado correctamente tras pago exitoso.
  - Observer notificado correctamente tras pago fallido.
- Ejecutar `mvn test` y corregir cualquier falla.
- Verificar cobertura Jacoco ≥ 85 % exigida en `pom.xml`.

### Dependencias
Fase 4 completada (flujo completo operativo).

---

## Fase 6 — Documentación y cierre

**Rama:** `feat/e2-fase-6-docs`

### Descripción
Consolidar la documentación y preparar la entrega final del Ejercicio 2.

### Objetivos
- Completar `solucion2.md`:
  - Respuesta a cada objetivo pedagógico del README:
    1. Patrones identificados y si son los adecuados.
    2. Clases/interfaces que faltaban.
    3. Validación del diagrama de contexto (suficiencia y pertinencia).
    4. Errores encontrados en el código y por qué no compilaba.
    5. Correcciones implementadas.
    6. Evidencia de ejecución de pruebas (salida de consola).
  - Todos los prompts de la sesión.
- Hacer merge de todas las ramas hacia `main`.

### Dependencias
Fase 5 completada.

---

## Resumen de ramas

| Fase | Rama                        | Entregable principal                                             |
|------|-----------------------------|------------------------------------------------------------------|
| 1    | `feat/e2-fase-1-analisis`   | Diagnóstico documentado en `solucion2.md`                        |
| 2    | `feat/e2-fase-2-bugfix`     | Bugs corregidos, `mvn compile` en verde                          |
| 3    | `feat/e2-fase-3-factory`    | `CreditCardPaymentFactory`, `PaypalPaymentFactory`, `CryptoPaymentFactory` |
| 4    | `feat/e2-fase-4-observer`   | Cadena Observer completa, flujo de consola demostrado            |
| 5    | `feat/e2-fase-5-tests`      | Tests JUnit 5 en verde, cobertura ≥ 85 %                        |
| 6    | `feat/e2-fase-6-docs`       | `solucion2.md` completo, merge a `main`                          |
