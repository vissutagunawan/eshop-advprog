package id.ac.ui.cs.advprog.eshop.service;

import enums.OrderStatus;
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
    void testAddPayment(){
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
}
