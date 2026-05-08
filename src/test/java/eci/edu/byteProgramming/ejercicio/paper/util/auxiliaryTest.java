package eci.edu.byteProgramming.ejercicio.paper.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class auxiliaryTest {

    // ═══════════════════════════════════════════════════════════════════
    // CreditCardPayment — validación
    // ═══════════════════════════════════════════════════════════════════

    @Test
    void creditCard_validData_validates() {
        assertTrue(validCard().validatePaymentMethod());
    }

    @Test
    void creditCard_shortCardNumber_failsValidation() {
        assertFalse(card("123456789012", "123", "12/26").validatePaymentMethod()); // 12 < 13
    }

    @Test
    void creditCard_longCardNumber_failsValidation() {
        assertFalse(card("12345678901234567890", "123", "12/26").validatePaymentMethod()); // 20 > 19
    }

    @Test
    void creditCard_shortCVV_failsValidation() {
        assertFalse(card("4111111111111111", "12", "12/26").validatePaymentMethod()); // 2 < 3
    }

    @Test
    void creditCard_longCVV_failsValidation() {
        assertFalse(card("4111111111111111", "12345", "12/26").validatePaymentMethod()); // 5 > 4
    }

    @Test
    void creditCard_invalidExpiryFormat_failsValidation() {
        assertFalse(card("4111111111111111", "123", "1226").validatePaymentMethod());
    }

    @Test
    void creditCard_nullNumber_failsValidation() {
        assertFalse(card(null, "123", "12/26").validatePaymentMethod());
    }

    // ═══════════════════════════════════════════════════════════════════
    // CreditCardPayment — procesamiento
    // ═══════════════════════════════════════════════════════════════════

    @Test
    void creditCard_validData_processSucceeds() {
        CreditCardPayment p = validCard();
        assertTrue(p.processPayment());
        assertEquals(PaymentStatus.COMPLETED, p.getStatus());
    }

    @Test
    void creditCard_invalidData_processFails() {
        CreditCardPayment p = card("123", "1", "INVALID");
        assertFalse(p.processPayment());
        assertEquals(PaymentStatus.FAILED, p.getStatus());
    }

    @Test
    void creditCard_getters_returnExpectedValues() {
        CreditCardPayment p = validCard();
        assertEquals("CREDIT_CARD", p.getPaymentMethod());
        assertEquals(100.0, p.getAmount());
        assertEquals("CUST001", p.getCustomerId());
        assertNotNull(p.getTransactionId());
        assertNotNull(p.getTimestamp());
        assertEquals("John Doe", p.getCardHolderName());
        assertEquals("VISA", p.getCardType());
        assertEquals("**** **** **** 1111", p.maskCardNumber());
    }

    // ═══════════════════════════════════════════════════════════════════
    // PaypalPayment — validación
    // ═══════════════════════════════════════════════════════════════════

    @Test
    void paypal_validData_validates() {
        assertTrue(validPaypal().validatePaymentMethod());
    }

    @Test
    void paypal_emailWithoutAt_failsValidation() {
        assertFalse(new PaypalPayment(100, "C1", "desc", "notanemail", "validtoken1234").validatePaymentMethod());
    }

    @Test
    void paypal_emailWithoutDot_failsValidation() {
        assertFalse(new PaypalPayment(100, "C1", "desc", "test@nodot", "validtoken1234").validatePaymentMethod());
    }

    @Test
    void paypal_tokenTooShort_failsValidation() {
        assertFalse(new PaypalPayment(100, "C1", "desc", "test@test.com", "short").validatePaymentMethod());
    }

    @Test
    void paypal_nullEmail_failsValidation() {
        assertFalse(new PaypalPayment(100, "C1", "desc", null, "validtoken1234").validatePaymentMethod());
    }

    // ═══════════════════════════════════════════════════════════════════
    // PaypalPayment — procesamiento
    // ═══════════════════════════════════════════════════════════════════

    @Test
    void paypal_validData_processSucceeds() {
        PaypalPayment p = validPaypal();
        assertTrue(p.processPayment());
        assertEquals(PaymentStatus.COMPLETED, p.getStatus());
        assertNotNull(p.getPaypalTransactionId());
    }

    @Test
    void paypal_invalidData_processFails() {
        PaypalPayment p = new PaypalPayment(100, "C1", "desc", "notanemail", "short");
        assertFalse(p.processPayment());
        assertEquals(PaymentStatus.FAILED, p.getStatus());
    }

    @Test
    void paypal_getters_returnExpectedValues() {
        PaypalPayment p = validPaypal();
        assertEquals("PAYPAL", p.getPaymentMethod());
        assertEquals("test@test.com", p.getEmail());
        assertEquals(100.0, p.getAmount());
        assertEquals("CUST001", p.getCustomerId());
    }

    // ═══════════════════════════════════════════════════════════════════
    // CryptoPayment — validación
    // ═══════════════════════════════════════════════════════════════════

    @Test
    void crypto_validData_validates() {
        assertTrue(validCrypto(100.0).validatePaymentMethod());
    }

    @Test
    void crypto_shortWalletAddress_failsValidation() {
        CryptoPayment p = new CryptoPayment(100, "C1", "desc", "SHORT", "BTC", 500.0);
        assertFalse(p.validatePaymentMethod());
    }

    @Test
    void crypto_insufficientBalance_failsValidation() {
        CryptoPayment p = new CryptoPayment(99999.0, "C1", "desc",
                "1A2B3C4D5E6F7G8H9I0J1K2L3M", "BTC", 100.0);
        assertFalse(p.validatePaymentMethod());
    }

    @Test
    void crypto_nullAddress_failsValidation() {
        CryptoPayment p = new CryptoPayment(100, "C1", "desc", null, "BTC", 500.0);
        assertFalse(p.validatePaymentMethod());
    }

    // ═══════════════════════════════════════════════════════════════════
    // CryptoPayment — procesamiento
    // ═══════════════════════════════════════════════════════════════════

    @Test
    void crypto_validData_processSucceeds() {
        CryptoPayment p = validCrypto(100.0);
        assertTrue(p.processPayment());
        assertEquals(PaymentStatus.COMPLETED, p.getStatus());
        assertNotNull(p.getBlockchainHash());
        assertTrue(p.getBlockchainHash().startsWith("0x"));
    }

    @Test
    void crypto_invalidData_processFails() {
        CryptoPayment p = new CryptoPayment(100, "C1", "desc", "SHORT", "BTC", 500.0);
        assertFalse(p.processPayment());
        assertEquals(PaymentStatus.FAILED, p.getStatus());
    }

    @Test
    void crypto_getters_returnExpectedValues() {
        CryptoPayment p = validCrypto(100.0);
        assertEquals("CRYPTOCURRENCY", p.getPaymentMethod());
        assertEquals("1A2B3C4D5E6F7G8H9I0J1K2L3M", p.getWalletAddress());
        assertEquals("BTC", p.getCryptoType());
    }

    // ═══════════════════════════════════════════════════════════════════
    // Fábricas concretas
    // ═══════════════════════════════════════════════════════════════════

    @Test
    void creditCardFactory_createsCorrectPaymentMethod() {
        PaymentFactory factory = new CreditCardPaymentFactory(
                "4111111111111111", "John Doe", "12/26", "123", "Calle 1");
        PaymentMethod pm = factory.createPaymentMethod(200.0, "CUST01", "Laptop");
        assertInstanceOf(CreditCardPayment.class, pm);
        assertEquals("CREDIT_CARD", pm.getPaymentMethod());
        assertEquals(200.0, pm.getAmount());
        assertEquals("CUST01", pm.getCustomerId());
    }

    @Test
    void paypalFactory_createsCorrectPaymentMethod() {
        PaymentFactory factory = new PaypalPaymentFactory("user@test.com", "validtoken1234");
        PaymentMethod pm = factory.createPaymentMethod(300.0, "CUST02", "Phone");
        assertInstanceOf(PaypalPayment.class, pm);
        assertEquals("PAYPAL", pm.getPaymentMethod());
        assertEquals(300.0, pm.getAmount());
    }

    @Test
    void cryptoFactory_createsCorrectPaymentMethod() {
        PaymentFactory factory = new CryptoPaymentFactory(
                "1A2B3C4D5E6F7G8H9I0J1K2L3M", "ETH", 5000.0);
        PaymentMethod pm = factory.createPaymentMethod(100.0, "CUST03", "Book");
        assertInstanceOf(CryptoPayment.class, pm);
        assertEquals("CRYPTOCURRENCY", pm.getPaymentMethod());
    }

    // ═══════════════════════════════════════════════════════════════════
    // ECIPayment + AuditObserver (Observer pattern)
    // ═══════════════════════════════════════════════════════════════════

    @Test
    void eciPayment_successfulPayment_notifiesObserverWithSuccess() {
        ECIPayment eciPayment = new ECIPayment();
        AuditObserver audit = new AuditObserver();
        eciPayment.addObserver(audit);

        PaymentFactory factory = new PaypalPaymentFactory("test@test.com", "validtoken1234");
        boolean result = eciPayment.processPayment(factory, 100.0, "C1",
                "desc", "John", "john@test.com", "LAPTOP001");

        assertTrue(result);
        assertEquals(1, audit.getAuditLog().size());
        assertTrue(audit.getAuditLog().get(0).contains("SUCCESS"));
    }

    @Test
    void eciPayment_failedPayment_notifiesObserverWithFailure() {
        ECIPayment eciPayment = new ECIPayment();
        AuditObserver audit = new AuditObserver();
        eciPayment.addObserver(audit);

        PaymentFactory factory = new PaypalPaymentFactory("notanemail", "short");
        boolean result = eciPayment.processPayment(factory, 100.0, "C1",
                "desc", "John", "john@test.com", "LAPTOP001");

        assertFalse(result);
        assertEquals(1, audit.getAuditLog().size());
        assertTrue(audit.getAuditLog().get(0).contains("FAILED"));
    }

    @Test
    void eciPayment_removedObserver_doesNotReceiveNotification() {
        ECIPayment eciPayment = new ECIPayment();
        AuditObserver audit = new AuditObserver();
        eciPayment.addObserver(audit);
        eciPayment.removeObserver(audit);

        PaymentFactory factory = new PaypalPaymentFactory("test@test.com", "validtoken1234");
        eciPayment.processPayment(factory, 100.0, "C1",
                "desc", "John", "john@test.com", "LAPTOP001");

        assertTrue(audit.getAuditLog().isEmpty());
    }

    @Test
    void eciPayment_multipleObservers_allNotified() {
        ECIPayment eciPayment = new ECIPayment();
        AuditObserver audit1 = new AuditObserver();
        AuditObserver audit2 = new AuditObserver();
        eciPayment.addObserver(audit1);
        eciPayment.addObserver(audit2);

        PaymentFactory factory = new PaypalPaymentFactory("test@test.com", "validtoken1234");
        eciPayment.processPayment(factory, 100.0, "C1",
                "desc", "John", "john@test.com", "LAPTOP001");

        assertEquals(1, audit1.getAuditLog().size());
        assertEquals(1, audit2.getAuditLog().size());
    }

    // ═══════════════════════════════════════════════════════════════════
    // PaymentEventObserver
    // ═══════════════════════════════════════════════════════════════════

    @Test
    void paymentEventObserver_onSuccess_discountsInventoryByOne() {
        Inventory inventory = new Inventory();
        PaymentEventObserver observer = new PaymentEventObserver(
                inventory, new Facturation(), new Notification());

        int stockBefore = inventory.getStock("LAPTOP001");
        observer.onPaymentSuccess(validPaypal(), "John", "john@test.com", "LAPTOP001");
        assertEquals(stockBefore - 1, inventory.getStock("LAPTOP001"));
    }

    @Test
    void paymentEventObserver_onSuccess_unknownProduct_doesNotCrash() {
        PaymentEventObserver observer = new PaymentEventObserver(
                new Inventory(), new Facturation(), new Notification());
        assertDoesNotThrow(() ->
                observer.onPaymentSuccess(validPaypal(), "John", "john@test.com", "UNKNOWN_ID"));
    }

    @Test
    void paymentEventObserver_onFailure_doesNotCrash() {
        PaymentEventObserver observer = new PaymentEventObserver(
                new Inventory(), new Facturation(), new Notification());
        assertDoesNotThrow(() ->
                observer.onPaymentFailed(validPaypal(), "john@test.com"));
    }

    // ═══════════════════════════════════════════════════════════════════
    // AuditObserver
    // ═══════════════════════════════════════════════════════════════════

    @Test
    void auditObserver_getAuditLog_isUnmodifiable() {
        AuditObserver audit = new AuditObserver();
        assertThrows(UnsupportedOperationException.class,
                () -> audit.getAuditLog().add("manual entry"));
    }

    @Test
    void auditObserver_successEvent_containsMethodAndAmount() {
        AuditObserver audit = new AuditObserver();
        audit.onPaymentSuccess(validPaypal(), "Maria", "maria@test.com", "LAPTOP001");
        String log = audit.getAuditLog().get(0);
        assertTrue(log.contains("SUCCESS"));
        assertTrue(log.contains("PAYPAL"));
        assertTrue(log.contains("100.00"));
    }

    @Test
    void auditObserver_failedEvent_containsEmailAndMethod() {
        AuditObserver audit = new AuditObserver();
        audit.onPaymentFailed(validPaypal(), "maria@test.com");
        String log = audit.getAuditLog().get(0);
        assertTrue(log.contains("FAILED"));
        assertTrue(log.contains("maria@test.com"));
    }

    // ═══════════════════════════════════════════════════════════════════
    // Módulos de soporte: Inventory, Facturation, Product, Notification
    // ═══════════════════════════════════════════════════════════════════

    @Test
    void inventory_discountProduct_reducesStock() {
        Inventory inv = new Inventory();
        int before = inv.getStock("LAPTOP001");
        assertTrue(inv.discountProduct("LAPTOP001", 1));
        assertEquals(before - 1, inv.getStock("LAPTOP001"));
    }

    @Test
    void inventory_discountProduct_insufficientStock_returnsFalse() {
        Inventory inv = new Inventory();
        assertFalse(inv.discountProduct("LAPTOP001", 999));
    }

    @Test
    void inventory_getProduct_unknownId_returnsNull() {
        assertNull(new Inventory().getProduct("NONEXISTENT"));
    }

    @Test
    void inventory_getStock_unknownId_returnsZero() {
        assertEquals(0, new Inventory().getStock("NONEXISTENT"));
    }

    @Test
    void product_getters_returnExpectedValues() {
        Product p = new Product("P1", "Laptop", 1200.0, "Electronics");
        assertEquals("P1", p.getProductId());
        assertEquals("Laptop", p.getName());
        assertEquals(1200.0, p.getPrice());
        assertEquals("Electronics", p.getCategory());
    }

    @Test
    void facturation_calculateTax_appliesNineteenPercent() {
        Facturation f = new Facturation();
        assertEquals(19.0, f.calculateTax(100.0), 0.001);
    }

    @Test
    void facturation_calculateTotal_includesTax() {
        Facturation f = new Facturation();
        assertEquals(119.0, f.calculateTotal(100.0), 0.001);
    }

    @Test
    void facturation_getNextInvoiceNumber_startsAtExpectedValue() {
        Facturation f = new Facturation();
        assertEquals("INV-1001", f.getNextInvoiceNumber());
    }

    @Test
    void facturation_getters_returnExpectedValues() {
        Facturation f = new Facturation();
        assertNotNull(f.getCompanyName());
        assertNotNull(f.getTaxId());
        assertEquals(0.19, f.getTaxRate(), 0.001);
        assertNotNull(f.getCurrency());
    }

    @Test
    void facturation_generateInvoice_doesNotCrash() {
        assertDoesNotThrow(() ->
                new Facturation().generateInvoice(validPaypal(), "John", "Laptop"));
    }

    @Test
    void notification_sendConfirmationEmail_doesNotCrash() {
        assertDoesNotThrow(() ->
                new Notification().sendConfirmationEmail("u@test.com", "John", validPaypal()));
    }

    @Test
    void notification_sendFailureNotification_doesNotCrash() {
        assertDoesNotThrow(() ->
                new Notification().sendFailureNotification(validPaypal(), "u@test.com"));
    }

    @Test
    void notification_getters_returnExpectedValues() {
        Notification n = new Notification();
        assertNotNull(n.getCompanyName());
        assertNotNull(n.getFromEmail());
    }

    // ═══════════════════════════════════════════════════════════════════
    // PaymentStatus enum
    // ═══════════════════════════════════════════════════════════════════

    @Test
    void paymentStatus_names_areCorrect() {
        assertEquals("Pendiente",  PaymentStatus.PENDING.getName());
        assertEquals("Procesando", PaymentStatus.PROCESSING.getName());
        assertEquals("Completado", PaymentStatus.COMPLETED.getName());
        assertEquals("Fallido",    PaymentStatus.FAILED.getName());
        assertEquals("Cancelado",  PaymentStatus.CANCELED.getName());
    }

    // ═══════════════════════════════════════════════════════════════════
    // PaymentMethod — fix E3: customerID correctamente asignado
    // ═══════════════════════════════════════════════════════════════════

    @Test
    void paymentMethod_customerIdFix_isNotNull() {
        PaymentMethod pm = validCard();
        assertNotNull(pm.getCustomerId());
        assertEquals("CUST001", pm.getCustomerId());
    }

    @Test
    void paymentMethod_setAmount_updatesAmount() {
        PaymentMethod pm = validCard();
        pm.setAmount(500.0);
        assertEquals(500.0, pm.getAmount());
    }

    @Test
    void paymentMethod_setStatus_updatesStatus() {
        PaymentMethod pm = validCard();
        pm.setStatus(PaymentStatus.CANCELED);
        assertEquals(PaymentStatus.CANCELED, pm.getStatus());
    }

    // ═══════════════════════════════════════════════════════════════════
    // Helpers
    // ═══════════════════════════════════════════════════════════════════

    private CreditCardPayment validCard() {
        return card("4111111111111111", "123", "12/26");
    }

    private CreditCardPayment card(String number, String cvv, String expiry) {
        return new CreditCardPayment(100.0, "CUST001", "Test payment",
                number, "John Doe", expiry, cvv, "Calle 1");
    }

    private PaypalPayment validPaypal() {
        return new PaypalPayment(100.0, "CUST001", "Test payment",
                "test@test.com", "validtoken1234");
    }

    private CryptoPayment validCrypto(double amount) {
        return new CryptoPayment(amount, "CUST001", "Test payment",
                "1A2B3C4D5E6F7G8H9I0J1K2L3M", "BTC", 1_000_000.0);
    }
}
