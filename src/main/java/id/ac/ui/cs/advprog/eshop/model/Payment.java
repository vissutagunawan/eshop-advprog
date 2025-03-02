package id.ac.ui.cs.advprog.eshop.model;

import enums.PaymentMethod;
import lombok.Getter;
import java.util.Map;
import enums.PaymentStatus;

@Getter
public class Payment {
    private String id;
    private Order order;
    private String method;
    private String status;
    private Map<String, String> paymentData;

    public Payment(String id, Order order, String method, Map<String, String> paymentData) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        if (method == null || method.trim().isEmpty() || !PaymentMethod.contains(method)) {
            throw new IllegalArgumentException("Method cannot be null or empty");
        }
        if (paymentData == null) {
            throw new IllegalArgumentException("Payment data cannot be null");
        }

        this.id = id;
        this.order = order;
        this.method = method;
        this.paymentData = paymentData;
        this.status = PaymentStatus.PENDING.getValue();
    }

    public void setStatus(String status) {
        if (!PaymentStatus.contains(status)) {
            throw new IllegalArgumentException("Invalid payment status: " + status);
        }
        this.status = status;
    }


}
