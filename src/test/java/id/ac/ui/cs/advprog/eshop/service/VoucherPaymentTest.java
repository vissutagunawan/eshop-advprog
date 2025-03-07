package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VoucherPaymentTest {

    @InjectMocks
    PaymentServiceImpl paymentService;

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    OrderService orderService;

    private Order order;

    @BeforeEach
    void setUp() {
        // Set up an order
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);
        products.add(product);

        order = new Order("order-123", products, 1708560000L, "Safira Sudrajat");
    }

    @Test
    void testValidVoucherCode() {
        // Set up valid voucher payment data
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        // Mock repository save
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Mock order service
        when(orderService.updateStatus(anyString(), anyString())).thenReturn(order);

        // Call the service method
        Payment payment = paymentService.addPayment(order, PaymentMethod.VOUCHER.getValue(), paymentData);

        // Verify the payment status is SUCCESS
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
    }

    @Test
    void testInvalidVoucherCodeTooShort() {
        // Set up invalid voucher payment data - too short
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP123456");

        // Mock repository save
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Mock order service
        when(orderService.updateStatus(anyString(), anyString())).thenReturn(order);

        // Call the service method
        Payment payment = paymentService.addPayment(order, PaymentMethod.VOUCHER.getValue(), paymentData);

        // Verify the payment status is REJECTED
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testInvalidVoucherCodeTooLong() {
        // Set up invalid voucher payment data - too long
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678901");

        // Mock repository save
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Mock order service
        when(orderService.updateStatus(anyString(), anyString())).thenReturn(order);

        // Call the service method
        Payment payment = paymentService.addPayment(order, "VOUCHER", paymentData);

        // Verify the payment status is REJECTED
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testInvalidVoucherCodeWrongPrefix() {
        // Set up invalid voucher payment data - wrong prefix
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "STORE1234ABC5678");

        // Mock repository save
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Mock order service
        when(orderService.updateStatus(anyString(), anyString())).thenReturn(order);

        // Call the service method
        Payment payment = paymentService.addPayment(order, "VOUCHER", paymentData);

        // Verify the payment status is REJECTED
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testInvalidVoucherCodeNotEnoughNumbers() {
        // Set up invalid voucher payment data - not enough numbers
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOPABCDEFGHIJK");

        // Mock repository save
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Mock order service
        when(orderService.updateStatus(anyString(), anyString())).thenReturn(order);

        // Call the service method
        Payment payment = paymentService.addPayment(order, "VOUCHER", paymentData);

        // Verify the payment status is REJECTED
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testInvalidVoucherCodeNull() {
        // Set up invalid voucher payment data - null
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", null);

        // Mock repository save
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Mock order service
        when(orderService.updateStatus(anyString(), anyString())).thenReturn(order);

        // Call the service method
        Payment payment = paymentService.addPayment(order, "VOUCHER", paymentData);

        // Verify the payment status is REJECTED
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }
}