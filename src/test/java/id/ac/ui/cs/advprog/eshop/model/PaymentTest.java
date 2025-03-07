package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
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
        Payment payment = new Payment("payment-123", order, PaymentMethod.VOUCHER.getValue(), paymentData);
        assertEquals("payment-123", payment.getId());
        assertEquals(order, payment.getOrder());
        assertEquals(PaymentMethod.VOUCHER.getValue(), payment.getMethod());
        assertEquals(paymentData, payment.getPaymentData());
        assertEquals(PaymentStatus.PENDING.getValue(), payment.getStatus());
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
            new Payment("payment-123", order, PaymentMethod.VOUCHER.getValue(), null);
        });
    }

    @Test
    void testSetStatus() {
        Payment payment = new Payment("payment-123", order, PaymentMethod.VOUCHER.getValue(), paymentData);
        payment.setStatus(PaymentStatus.SUCCESS.getValue());

        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
    }

    @Test
    void testSetStatusWithInvalidStatus() {
        Payment payment = new Payment("payment-123", order, PaymentMethod.VOUCHER.getValue(), paymentData);

        assertThrows(IllegalArgumentException.class, () -> {
            payment.setStatus("INVALID_STATUS");
        });
    }

    @Test
    void testCreatePaymentWithInvalidMethod() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("payment-123", order, "INVALID_METHOD", paymentData);
        });
    }

    @Test
    void testCreatePaymentWithWhitespaceMethod() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("payment-123", order, "   ", paymentData);
        });
    }

    // Add this test to ensure all components of the compound condition are tested
    @Test
    void testMethodNullOrEmptyOrInvalid() {
        // Test null method (already covered in original test)
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("payment-123", order, null, paymentData);
        });

        // Test empty method (already covered in original test)
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("payment-123", order, "", paymentData);
        });

        // Test method with only spaces
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("payment-123", order, "    ", paymentData);
        });

        // Test invalid method (not in PaymentMethod enum)
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("payment-123", order, "INVALID_PAYMENT_METHOD", paymentData);
        });
    }

}
