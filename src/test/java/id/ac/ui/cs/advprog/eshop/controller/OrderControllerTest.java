package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private ProductService productService;

    @MockBean
    private PaymentService paymentService;

    private List<Product> products;
    private List<Order> orders;
    private Order order;

    @BeforeEach
    void setUp() {
        // Set up products
        this.products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(2);
        products.add(product1);

        Product product2 = new Product();
        product2.setProductId("a2c62328-4a37-4664-83c7-f32db8620155");
        product2.setProductName("Sabun Cap Usep");
        product2.setProductQuantity(1);
        products.add(product2);

        // Set up order
        this.order = new Order("13652556-012a-4c07-b546-54eb1396d79b",
                Collections.singletonList(product1), 1708560000L, "Safira Sudrajat");

        // Set up orders list
        this.orders = new ArrayList<>();
        this.orders.add(order);
    }

    @Test
    void testCreateOrderPage() throws Exception {
        when(productService.findAll()).thenReturn(products);

        mockMvc.perform(get("/order/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/create"))
                .andExpect(model().attributeExists("products"));
    }

    @Test
    void testHistoryFormPage() throws Exception {
        mockMvc.perform(get("/order/history"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/history-form"));
    }

    @Test
    void testHistoryPage() throws Exception {
        String author = "Safira Sudrajat";
        when(orderService.findAllByAuthor(author)).thenReturn(orders);

        mockMvc.perform(post("/order/history")
                        .param("author", author))
                .andExpect(status().isOk())
                .andExpect(view().name("order/history"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attribute("author", author));
    }

    @Test
    void testPayOrderPage() throws Exception {
        String orderId = "13652556-012a-4c07-b546-54eb1396d79b";
        when(orderService.findById(orderId)).thenReturn(order);

        mockMvc.perform(get("/order/pay/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(view().name("order/payment-form"))
                .andExpect(model().attributeExists("order"))
                .andExpect(model().attributeExists("paymentMethods"));
    }

    @Test
    void testPayOrderVoucher() throws Exception {
        String orderId = "13652556-012a-4c07-b546-54eb1396d79b";
        when(orderService.findById(orderId)).thenReturn(order);

        // Create mock payment
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment mockPayment = new Payment("payment-123", order, "VOUCHER", paymentData);

        when(paymentService.addPayment(any(Order.class), eq("VOUCHER"), anyMap())).thenReturn(mockPayment);

        mockMvc.perform(post("/order/pay/{id}", orderId)
                        .param("paymentMethod", "VOUCHER")
                        .param("voucherCode", "ESHOP1234ABC5678"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/payment-success"))
                .andExpect(model().attributeExists("payment"))
                .andExpect(model().attributeExists("order"));
    }

    @Test
    void testPayOrderCashOnDelivery() throws Exception {
        String orderId = "13652556-012a-4c07-b546-54eb1396d79b";
        when(orderService.findById(orderId)).thenReturn(order);

        // Create mock payment
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("address", "Jl. Margonda Raya No. 100");
        paymentData.put("deliveryFee", "15000");
        Payment mockPayment = new Payment("payment-123", order, "CASH_ON_DELIVERY", paymentData);

        when(paymentService.addPayment(any(Order.class), eq("CASH_ON_DELIVERY"), anyMap())).thenReturn(mockPayment);

        mockMvc.perform(post("/order/pay/{id}", orderId)
                        .param("paymentMethod", "CASH_ON_DELIVERY")
                        .param("address", "Jl. Margonda Raya No. 100")
                        .param("deliveryFee", "15000"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/payment-success"))
                .andExpect(model().attributeExists("payment"))
                .andExpect(model().attributeExists("order"));
    }

    @Test
    void testPayOrderBankTransfer() throws Exception {
        String orderId = "13652556-012a-4c07-b546-54eb1396d79b";
        when(orderService.findById(orderId)).thenReturn(order);

        // Create mock payment
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "Bank Central Java");
        paymentData.put("referenceCode", "REF123456");
        Payment mockPayment = new Payment("payment-123", order, "BANK_TRANSFER", paymentData);

        when(paymentService.addPayment(any(Order.class), eq("BANK_TRANSFER"), anyMap())).thenReturn(mockPayment);

        mockMvc.perform(post("/order/pay/{id}", orderId)
                        .param("paymentMethod", "BANK_TRANSFER")
                        .param("bankName", "Bank Central Java")
                        .param("referenceCode", "REF123456"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/payment-success"))
                .andExpect(model().attributeExists("payment"))
                .andExpect(model().attributeExists("order"));
    }

    @Test
    void testPayOrderNotFound() throws Exception {
        String orderId = "non-existent-id";
        when(orderService.findById(orderId)).thenReturn(null);

        mockMvc.perform(get("/order/pay/{id}", orderId))
                .andExpect(status().isNotFound());
    }
}