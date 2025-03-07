package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    private Payment payment;
    private List<Payment> payments;

    @BeforeEach
    void setUp() {
        // Set up product
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);
        List<Product> products = Collections.singletonList(product);

        // Set up order
        Order order = new Order("order-123", products, 1708560000L, "Safira Sudrajat");

        // Set up payment data
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        // Set up payment
        payment = new Payment("payment-123", order, "VOUCHER", paymentData);
        payment.setStatus(PaymentStatus.SUCCESS.getValue());

        // Set up payments list
        payments = new ArrayList<>();
        payments.add(payment);
    }

    @Test
    void testPaymentDetailForm() throws Exception {
        mockMvc.perform(get("/payment/detail"))
                .andExpect(status().isOk())
                .andExpect(view().name("payment/detail-form"));
    }

    @Test
    void testPaymentDetail() throws Exception {
        when(paymentService.getPayment("payment-123")).thenReturn(payment);

        mockMvc.perform(get("/payment/detail/payment-123"))
                .andExpect(status().isOk())
                .andExpect(view().name("payment/detail"))
                .andExpect(model().attributeExists("payment"));
    }

    @Test
    void testPaymentDetailNotFound() throws Exception {
        when(paymentService.getPayment("non-existent-id")).thenReturn(null);

        mockMvc.perform(get("/payment/detail/non-existent-id"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPaymentListAdmin() throws Exception {
        when(paymentService.getAllPayments()).thenReturn(payments);

        mockMvc.perform(get("/payment/admin/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("payment/admin-list"))
                .andExpect(model().attributeExists("payments"));
    }

    @Test
    void testPaymentDetailAdmin() throws Exception {
        when(paymentService.getPayment("payment-123")).thenReturn(payment);

        mockMvc.perform(get("/payment/admin/detail/payment-123"))
                .andExpect(status().isOk())
                .andExpect(view().name("payment/admin-detail"))
                .andExpect(model().attributeExists("payment"))
                .andExpect(model().attributeExists("statuses"));
    }

    @Test
    void testPaymentDetailAdminNotFound() throws Exception {
        when(paymentService.getPayment("non-existent-id")).thenReturn(null);

        mockMvc.perform(get("/payment/admin/detail/non-existent-id"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSetPaymentStatus() throws Exception {
        when(paymentService.getPayment("payment-123")).thenReturn(payment);
        when(paymentService.setStatus(any(Payment.class), anyString())).thenReturn(payment);

        mockMvc.perform(post("/payment/admin/set-status/payment-123")
                        .param("status", "SUCCESS"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/admin/detail/payment-123"));
    }

    @Test
    void testSetPaymentStatusNotFound() throws Exception {
        when(paymentService.getPayment("non-existent-id")).thenReturn(null);

        mockMvc.perform(post("/payment/admin/set-status/non-existent-id")
                        .param("status", "SUCCESS"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPaymentDetailById() throws Exception {
        // Test the POST endpoint that redirects to the detail page
        mockMvc.perform(post("/payment/detail")
                        .param("paymentId", "payment-123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/detail/payment-123"));
    }
}