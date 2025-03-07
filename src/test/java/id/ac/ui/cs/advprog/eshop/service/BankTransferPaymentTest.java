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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankTransferPaymentTest {

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
    void testValidBankTransfer() {
        // Set up valid bank transfer payment data
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "Bank Central Java");
        paymentData.put("referenceCode", "REF123456");

        // Mock repository save
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Mock order service
        when(orderService.updateStatus(anyString(), anyString())).thenReturn(order);

        // Call the service method
        Payment payment = paymentService.addPayment(order, PaymentMethod.BANK_TRANSFER.getValue(), paymentData);

        // Verify the payment status is SUCCESS
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
    }

    @Test
    void testInvalidBankTransferNoBankName() {
        // Set up invalid bank transfer payment data - no bank name
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("referenceCode", "REF123456");

        // Mock repository save
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Mock order service
        when(orderService.updateStatus(anyString(), anyString())).thenReturn(order);

        // Call the service method
        Payment payment = paymentService.addPayment(order, "BANK_TRANSFER", paymentData);

        // Verify the payment status is REJECTED
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testInvalidBankTransferEmptyBankName() {
        // Set up invalid bank transfer payment data - empty bank name
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "");
        paymentData.put("referenceCode", "REF123456");

        // Mock repository save
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Mock order service
        when(orderService.updateStatus(anyString(), anyString())).thenReturn(order);

        // Call the service method
        Payment payment = paymentService.addPayment(order, "BANK_TRANSFER", paymentData);

        // Verify the payment status is REJECTED
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testInvalidBankTransferNoReferenceCode() {
        // Set up invalid bank transfer payment data - no reference code
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "Bank Central Java");

        // Mock repository save
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Mock order service
        when(orderService.updateStatus(anyString(), anyString())).thenReturn(order);

        // Call the service method
        Payment payment = paymentService.addPayment(order, "BANK_TRANSFER", paymentData);

        // Verify the payment status is REJECTED
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testInvalidBankTransferEmptyReferenceCode() {
        // Set up invalid bank transfer payment data - empty reference code
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "Bank Central Java");
        paymentData.put("referenceCode", "");

        // Mock repository save
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Mock order service
        when(orderService.updateStatus(anyString(), anyString())).thenReturn(order);

        // Call the service method
        Payment payment = paymentService.addPayment(order, "BANK_TRANSFER", paymentData);

        // Verify the payment status is REJECTED
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }
}