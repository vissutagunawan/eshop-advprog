package id.ac.ui.cs.advprog.eshop.model;

import org.apache.hc.core5.http.impl.routing.PathRoute;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class PaymentTest {
    private Order order;
    private Map<String, String> paymentData;

    @BeforeEach
    void setUp(){
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);
        products.add(product);

        order = new Order("13652556-012a-4c07-b546-54eb1396d79b", products, 1708560000L, "Safira Sudrajat");
        paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
    }

    @Test
    void testCreatePayment(){
        Payment payment = new Payment("payment-123", order, "VOUCHER", paymentData);
        assertEquals("payment-123", payment.getId());
        assertEquals(order, payment.getOrder());
        assertEquals("VOUCHER", payment.getMethod());
        assertEquals(paymentData, payment.getPaymentData());
        assertEquals("PENDING", payment.getStatus());
    }

    @Test
    void testCreatePaymentWithNullOrder(){
        assertThrows(IllegalArgumentException.class, () -> new Payment("payment-123", null, "VOUCHER", paymentData));
    }

    @Test
    void testCreatePaymentWithNullMethod() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("payment-123", order, null, paymentData);
        });
    }

    @Test
    void testCreatePaymentWithEmptyMethod() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("payment-123", order, "", paymentData);
        });
    }

    @Test
    void testCreatePaymentWithNullPaymentData() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("payment-123", order, "VOUCHER", null);
        });
    }

    @Test
    void testSetStatus() {
        Payment payment = new Payment("payment-123", order, "VOUCHER", paymentData);
        payment.setStatus("SUCCESS");

        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testSetStatusWithInvalidStatus() {
        Payment payment = new Payment("payment-123", order, "VOUCHER", paymentData);

        assertThrows(IllegalArgumentException.class, () -> {
            payment.setStatus("INVALID_STATUS");
        });
    }

}
