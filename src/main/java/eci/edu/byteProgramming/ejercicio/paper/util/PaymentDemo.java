package eci.edu.byteProgramming.ejercicio.paper.util;

/**
 * Demo ejecutable del sistema de pagos.
 * Muestra Abstract Factory + Observer actuando juntos sobre los tres métodos de pago.
 *
 * Ejecutar: mvn exec:java -Dexec.mainClass="eci.edu.byteProgramming.ejercicio.paper.util.PaymentDemo"
 */
public class PaymentDemo {

    public static void main(String[] args) {

        // --- Infraestructura compartida (módulos de la tienda) ---
        Inventory    inventory    = new Inventory();
        Facturation  facturation  = new Facturation();
        Notification notification = new Notification();

        // --- Sujeto Observer ---
        ECIPayment eciPayment = new ECIPayment();

        // --- Observadores registrados ---
        PaymentEventObserver eventObserver = new PaymentEventObserver(inventory, facturation, notification);
        AuditObserver        auditObserver = new AuditObserver();

        eciPayment.addObserver(eventObserver);
        eciPayment.addObserver(auditObserver);   // OCP: añadido sin tocar ECIPayment

        System.out.println("========================================");
        System.out.println("  DEMO: ECI Payments — Tienda Virtual  ");
        System.out.println("========================================\n");

        // --- Pago 1: Tarjeta de crédito (exitoso) ---
        printSeparator("PAGO 1 — Tarjeta de Crédito (exitoso)");
        PaymentFactory creditCardFactory = new CreditCardPaymentFactory(
                "4111111111111111", "Maria Garcia", "12/26", "123", "Calle 100 #45-30");
        eciPayment.processPayment(creditCardFactory, 1200.00, "CUST001",
                "Gaming Laptop", "Maria Garcia", "maria@example.com", "LAPTOP001");

        // --- Pago 2: PayPal (exitoso) ---
        printSeparator("PAGO 2 — PayPal (exitoso)");
        PaymentFactory paypalFactory = new PaypalPaymentFactory(
                "carlos@paypal.com", "AUTH_TOKEN_XYZ_SECURE_12345");
        eciPayment.processPayment(paypalFactory, 800.00, "CUST002",
                "Smartphone", "Carlos Lopez", "carlos@example.com", "PHONE001");

        // --- Pago 3: Criptomoneda (fallido — balance insuficiente) ---
        printSeparator("PAGO 3 — Crypto (fallido: balance insuficiente)");
        PaymentFactory cryptoFactory = new CryptoPaymentFactory(
                "1A2B3C4D5E6F7G8H9I0J1K2L3M4N5O6P", "BTC", 10.00);
        eciPayment.processPayment(cryptoFactory, 45000.00, "CUST003",
                "Bitcoin Purchase", "Ana Ruiz", "ana@example.com", "BOOK001");

        // --- Resumen de auditoría ---
        printSeparator("RESUMEN DE AUDITORÍA");
        auditObserver.getAuditLog().forEach(System.out::println);

        System.out.println("\n========================================");
        System.out.println("            Demo finalizado             ");
        System.out.println("========================================");
    }

    private static void printSeparator(String title) {
        System.out.println("\n--- " + title + " ---");
    }
}
