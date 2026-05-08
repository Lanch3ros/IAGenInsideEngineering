package eci.edu.byteProgramming.ejercicio.paper.util;

public class PaypalPaymentFactory implements PaymentFactory {
    private final String email;
    private final String authToken;

    public PaypalPaymentFactory(String email, String authToken) {
        this.email = email;
        this.authToken = authToken;
    }

    @Override
    public PaymentMethod createPaymentMethod(double amount, String customerId, String description) {
        return new PaypalPayment(amount, customerId, description, email, authToken);
    }
}
