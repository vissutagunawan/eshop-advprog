package id.ac.ui.cs.advprog.eshop.service;

import enums.PaymentMethod;
import enums.PaymentStatus;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CashOnDeliveryPaymentTest {

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
    void testValidCashOnDelivery() {
        // Set up valid cash on delivery payment data
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("address", "Jl. Margonda Raya No. 100");
        paymentData.put("deliveryFee", "15000");

        // Mock repository save
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Mock order service
        when(orderService.updateStatus(anyString(), anyString())).thenReturn(order);

        // Call the service method
        Payment payment = paymentService.addPayment(order, PaymentMethod.CASH_ON_DELIVERY.getValue(), paymentData);

        // Verify the payment status is SUCCESS
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
    }

    @Test
    void testInvalidCashOnDeliveryNoAddress() {
        // Set up invalid cash on delivery payment data - no address
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("deliveryFee", "15000");

        // Mock repository save
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Mock order service
        when(orderService.updateStatus(anyString(), anyString())).thenReturn(order);

        // Call the service method
        Payment payment = paymentService.addPayment(order, "CASH_ON_DELIVERY", paymentData);

        // Verify the payment status is REJECTED
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testInvalidCashOnDeliveryEmptyAddress() {
        // Set up invalid cash on delivery payment data - empty address
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("address", "");
        paymentData.put("deliveryFee", "15000");

        // Mock repository save
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Mock order service
        when(orderService.updateStatus(anyString(), anyString())).thenReturn(order);

        // Call the service method
        Payment payment = paymentService.addPayment(order, "CASH_ON_DELIVERY", paymentData);

        // Verify the payment status is REJECTED
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testInvalidCashOnDeliveryNoDeliveryFee() {
        // Set up invalid cash on delivery payment data - no delivery fee
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("address", "Jl. Margonda Raya No. 100");

        // Mock repository save
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Mock order service
        when(orderService.updateStatus(anyString(), anyString())).thenReturn(order);

        // Call the service method
        Payment payment = paymentService.addPayment(order, "CASH_ON_DELIVERY", paymentData);

        // Verify the payment status is REJECTED
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testInvalidCashOnDeliveryEmptyDeliveryFee() {
        // Set up invalid cash on delivery payment data - empty delivery fee
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("address", "Jl. Margonda Raya No. 100");
        paymentData.put("deliveryFee", "");

        // Mock repository save
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Mock order service
        when(orderService.updateStatus(anyString(), anyString())).thenReturn(order);

        // Call the service method
        Payment payment = paymentService.addPayment(order, "CASH_ON_DELIVERY", paymentData);

        // Verify the payment status is REJECTED
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }
}