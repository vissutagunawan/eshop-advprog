package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
import java.util.Map;
import java.util.Arrays;
import java.util.List;

@Getter
public class Payment {
    private String id;
    private Order order;
    private String method;
    private String status;
    private Map<String, String> paymentData;

    private static final List<String> VALID_STATUSES = Arrays.asList(
            "PENDING", "SUCCESS", "REJECTED", "FAILED");

    public Payment(String id, Order order, String method, Map<String, String> paymentData) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        if (method == null || method.trim().isEmpty()) {
            throw new IllegalArgumentException("Method cannot be null or empty");
        }
        if (paymentData == null) {
            throw new IllegalArgumentException("Payment data cannot be null");
        }

        this.id = id;
        this.order = order;
        this.method = method;
        this.paymentData = paymentData;
        this.status = "PENDING";
    }

    public void setStatus(String status) {
        if (!VALID_STATUSES.contains(status)) {
            throw new IllegalArgumentException("Invalid payment status: " + status);
        }
        this.status = status;
    }


}
