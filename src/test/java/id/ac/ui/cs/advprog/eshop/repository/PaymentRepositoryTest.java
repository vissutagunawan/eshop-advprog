package id.ac.ui.cs.advprog.eshop.repository;

import enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentRepositoryTest {
    private PaymentRepository paymentRepository;
    private Payment payment;
    private Payment payment2;

    @BeforeEach
    void setUp(){
        paymentRepository = new PaymentRepository();

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);
        products.add(product);

        Order order1 = new Order("order-123", products, 1708560000L, "Safira Sudrajat");
        Order order2 = new Order("order-456", products, 1708570000L, "Bambang Sudrajat");

        Map<String, String> paymentData1 = new HashMap<>();
        paymentData1.put("voucherCode", "ESHOP1234ABC5678");

        Map<String, String> paymentData2 = new HashMap<>();
        paymentData2.put("bankName", "Bank Central Java");
        paymentData2.put("referenceCode", "REF123456");

        payment = new Payment("payment-123", order1, "VOUCHER", paymentData1);
        payment2 = new Payment("payment-456", order2, "BANK_TRANSFER", paymentData2);
    }

    @Test
    void testSaveCreate(){
        Payment savedPayment = paymentRepository.save(payment);
        assertNotNull(savedPayment);
        assertEquals(payment.getId(), savedPayment.getId());

        Payment retrievedPayment = paymentRepository.findById(payment.getId());
        assertEquals(payment.getId(), retrievedPayment.getId());
        assertEquals(payment.getMethod(), retrievedPayment.getMethod());
    }

    @Test
    void testSaveUpdate(){
        paymentRepository.save(payment);
        payment.setStatus(PaymentStatus.SUCCESS.getValue());
        Payment updatedPayment = paymentRepository.save(payment);
        assertEquals(PaymentStatus.SUCCESS.getValue(), updatedPayment.getStatus());
        Payment retrievedPayment = paymentRepository.findById(payment.getId());
        assertEquals(PaymentStatus.SUCCESS.getValue(), retrievedPayment.getStatus());
    }

    @Test
    void testFindByIdIfFound(){
        paymentRepository.save(payment);
        paymentRepository.save(payment2);

        Payment foundPayment = paymentRepository.findById(payment.getId());

        assertNotNull(foundPayment);
        assertEquals(payment.getId(), foundPayment.getId());
        assertEquals(payment.getMethod(), foundPayment.getMethod());
    }

    @Test
    void testFindByIdIfNotFound(){
        paymentRepository.save(payment);

        Payment foundPayment = paymentRepository.findById("zzz");
        assertNull(foundPayment);
    }

    @Test
    void testFindAll(){
        paymentRepository.save(payment);
        paymentRepository.save(payment2);

        List<Payment> allPayments = paymentRepository.findAll();
        assertEquals(2, allPayments.size());
        assertTrue(allPayments.contains(payment));
        assertTrue(allPayments.contains(payment2));
    }

    @Test
    void testFindAllIfRepositoryEmpty(){
        List<Payment> allPayments = paymentRepository.findAll();
        assertTrue(allPayments.isEmpty());
    }
}
