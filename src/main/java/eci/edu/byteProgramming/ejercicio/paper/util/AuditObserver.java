package eci.edu.byteProgramming.ejercicio.paper.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Observador de auditoría: registra todos los eventos de pago en un log interno.
 * Demuestra OCP — se añade sin modificar ECIPayment ni PaymentEventObserver.
 */
public class AuditObserver implements PaymentObserver {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final List<String> auditLog = new ArrayList<>();

    @Override
    public void onPaymentSuccess(PaymentMethod payment, String customerName,
                                 String customerEmail, String productId) {
        String entry = String.format("[%s] SUCCESS | customer=%s | method=%s | amount=%.2f | txId=%s",
                now(), customerName, payment.getPaymentMethod(),
                payment.getAmount(), payment.getTransactionId());
        auditLog.add(entry);
        System.out.println("Audit: " + entry);
    }

    @Override
    public void onPaymentFailed(PaymentMethod payment, String customerEmail) {
        String entry = String.format("[%s] FAILED  | email=%s | method=%s | amount=%.2f | txId=%s",
                now(), customerEmail, payment.getPaymentMethod(),
                payment.getAmount(), payment.getTransactionId());
        auditLog.add(entry);
        System.out.println("Audit: " + entry);
    }

    public List<String> getAuditLog() {
        return Collections.unmodifiableList(auditLog);
    }

    private String now() {
        return LocalDateTime.now().format(FMT);
    }
}
