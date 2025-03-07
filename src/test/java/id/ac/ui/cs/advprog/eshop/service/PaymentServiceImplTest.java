package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {
    @InjectMocks
    PaymentServiceImpl paymentService;

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    OrderService orderService;

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

        order = new Order("order-123", products, 1708560000L, "Safira Sudrajat");

        paymentData = new HashMap<>();
    }

    @Test
    void testAddVoucherPayment(){
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> {
            Payment payment = invocation.getArgument(0);
            return payment;
        });

        Payment payment = paymentService.addPayment(order, "VOUCHER", paymentData);
        assertNotNull(payment);
        assertEquals("VOUCHER", payment.getMethod());
        assertEquals(order, payment.getOrder());
        assertEquals(paymentData, payment.getPaymentData());

        verify(paymentRepository, times(2)).save(any(Payment.class));
    }

    @Test
    void testAddCashOnDeliveryPayment() {
        paymentData.put("address", "Jl. Margonda Raya No. 100");
        paymentData.put("deliveryFee", "15000");

        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> {
            Payment payment = invocation.getArgument(0);
            return payment;
        });

        Payment payment = paymentService.addPayment(order, PaymentMethod.CASH_ON_DELIVERY.getValue(), paymentData);
        assertNotNull(payment);
        assertEquals(PaymentMethod.CASH_ON_DELIVERY.getValue(), payment.getMethod());
        assertEquals(order, payment.getOrder());
        assertEquals(paymentData, payment.getPaymentData());

        verify(paymentRepository, times(2)).save(any(Payment.class));
    }

    @Test
    void testAddBankTransferPayment() {
        paymentData.put("bankName", "Bank Central Java");
        paymentData.put("referenceCode", "REF123456");

        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> {
            Payment payment = invocation.getArgument(0);
            return payment;
        });

        Payment payment = paymentService.addPayment(order, PaymentMethod.BANK_TRANSFER.getValue(), paymentData);
        assertNotNull(payment);
        assertEquals(PaymentMethod.BANK_TRANSFER.getValue(), payment.getMethod());
        assertEquals(order, payment.getOrder());
        assertEquals(paymentData, payment.getPaymentData());
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());

        verify(paymentRepository, times(2)).save(any(Payment.class));
    }

    @Test
    void testSetStatusToSuccess(){
        Payment payment = new Payment("123", order, "VOUCHER", paymentData);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(orderService.updateStatus(anyString(), anyString())).thenReturn(order);
        Payment updatedPayment = paymentService.setStatus(payment, PaymentStatus.SUCCESS.getValue());

        assertEquals(PaymentStatus.SUCCESS.getValue(), updatedPayment.getStatus());
        verify(orderService, times(1)).updateStatus(order.getId(), OrderStatus.SUCCESS.getValue());
    }

    @Test
    void testSetStatusToRejected(){
        Payment payment = new Payment("123", order, "VOUCHER", paymentData);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(orderService.updateStatus(anyString(), anyString())).thenReturn(order);
        Payment updatedPayment = paymentService.setStatus(payment, PaymentStatus.REJECTED.getValue());

        assertEquals(PaymentStatus.REJECTED.getValue(), updatedPayment.getStatus());
        verify(orderService, times(1)).updateStatus(order.getId(), OrderStatus.FAILED.getValue());

    }

    @Test
    void testSetStatusToOther() {
        Payment payment = new Payment("payment-123", order, "VOUCHER", paymentData);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        Payment updatedPayment = paymentService.setStatus(payment, PaymentStatus.PENDING.getValue());

        assertEquals(PaymentStatus.PENDING.getValue(), updatedPayment.getStatus());
        verify(orderService, never()).updateStatus(anyString(), anyString());
    }

    @Test
    void testGetPayment(){
        Payment payment = new Payment("123", order, "VOUCHER", paymentData);
        when(paymentRepository.findById("123")).thenReturn(payment);

        Payment foundPayment = paymentService.getPayment("123");

        assertNotNull(foundPayment);
        assertEquals(payment.getId(), foundPayment.getId());
    }

    @Test
    void testGetAllPayments(){
        Payment payment = new Payment("123", order, "VOUCHER", paymentData);
        Payment payment2 = new Payment("456", order, "BANK_TRANSFER", paymentData);
        List<Payment> payments = new ArrayList<>();
        payments.add(payment);
        payments.add(payment2);

        when(paymentRepository.findAll()).thenReturn(payments);

        List<Payment> allPayments = paymentService.getAllPayments();
        assertEquals(2, allPayments.size());
        assertTrue(allPayments.contains(payment));
        assertTrue(allPayments.contains(payment2));
    }

    @Test
    void testAddBankTransferWithDirectMethodCall() {
        // Set up valid bank transfer payment data
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "Bank Central Java");
        paymentData.put("referenceCode", "REF123456");

        String paymentId = UUID.randomUUID().toString();
        Payment payment = new Payment(paymentId, order, PaymentMethod.BANK_TRANSFER.getValue(), paymentData);

        // Mock repository save
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(orderService.updateStatus(anyString(), anyString())).thenReturn(order);

        // Directly test the processPaymentByMethod method through reflection
        try {
            java.lang.reflect.Method processMethod = PaymentServiceImpl.class.getDeclaredMethod(
                    "processPaymentByMethod", Payment.class);
            processMethod.setAccessible(true);
            processMethod.invoke(paymentService, payment);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }

        // Verify the bank transfer was processed correctly
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
        verify(orderService, times(1)).updateStatus(order.getId(), OrderStatus.SUCCESS.getValue());
    }
}
