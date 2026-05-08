/**
 * Ejercicio 2 — Tienda Virtual: Sistema de Pagos.
 *
 * === DIAGNÓSTICO DE FASE 1 ===
 *
 * PATRONES IDENTIFICADOS
 * ──────────────────────
 * 1. Abstract Factory
 *    - Interfaz: PaymentFactory  (faltaba, agregada en fix previo)
 *    - Fábricas concretas: PENDIENTES — CreditCardFactory, PaypalFactory y
 *      CryptoFactory actualmente extienden PaymentMethod en lugar de
 *      implementar PaymentFactory. Son métodos de pago, no fábricas.
 *    - Consumidor: ECIPayment.processPayment(PaymentFactory, ...)
 *
 * 2. Observer
 *    - Interfaz sujeto: implícita en ECIPayment (addObserver / removeObserver)
 *    - Interfaz observador: PaymentObserver
 *    - Observador concreto: PaymentEventObserver
 *    - Notificados: Inventory, Facturation, Notification
 *
 * ERRORES DE COMPILACIÓN CATALOGADOS
 * ───────────────────────────────────
 * E1. ECIPayment referencia PaymentFactory que no existía.
 *     → Interfaz creada: PaymentFactory.java
 *
 * E2. PaymentEventObserver importa javax.management.Notification
 *     en lugar de la clase local Notification.
 *     → Import corregido.
 *
 * E3. PaymentMethod constructor asigna this.customerID = customerID
 *     pero customerID no es parámetro (el parámetro es transactionID).
 *     → customerID siempre queda null. Pendiente corrección en Fase 2.
 *
 * E4. CryptoFactory asigna this.token = token pero token no llega
 *     por constructor (campo nunca inicializado).
 *     → Pendiente corrección en Fase 2.
 *
 * E5. CreditCardFactory, PaypalFactory, CryptoFactory implementan
 *     el rol de método de pago (extienden PaymentMethod) pero sus
 *     nombres y la arquitectura de ECIPayment requieren fábricas
 *     separadas que implementen PaymentFactory.
 *     → Pendiente refactor en Fase 3.
 *
 * EVALUACIÓN DEL DIAGRAMA DE CONTEXTO
 * ────────────────────────────────────
 * Suficiencia: PARCIAL
 *   + Muestra los actores principales (Cliente, Sistema de Pago, tres módulos).
 *   + Refleja el flujo de notificación post-pago.
 *   - No representa los métodos de pago (CreditCard, PayPal, Crypto).
 *   - No muestra el patrón Factory ni cómo se crean los métodos de pago.
 *   - El nodo "Notificación" y el "Módulo notificación" son ambiguos y confusos.
 *   - La descripción del Módulo notificación está truncada ("des").
 *   - El diagrama de clases no incluye la interfaz PaymentFactory.
 *
 * Cambios sugeridos (documentados en solucion2.md, sin modificar imágenes):
 *   1. Añadir un nodo "Métodos de Pago" con sus tres variantes.
 *   2. Añadir la interfaz PaymentFactory al diagrama de clases.
 *   3. Separar visualmente el evento "Notificación" del "Módulo Notificación".
 *   4. Etiquetar explícitamente los patrones usados en el diagrama.
 */
package eci.edu.byteProgramming.ejercicio.paper.util;
