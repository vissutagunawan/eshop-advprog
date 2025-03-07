package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/create")
    public String createOrderForm(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "order/create";
    }

    @PostMapping("/create")
    public String createOrder(@RequestParam("author") String author,
                              @RequestParam(value = "selectedProducts", required = false) List<String> selectedProductIds,
                              Model model) {
        // Validate if products were selected
        if (selectedProductIds == null || selectedProductIds.isEmpty()) {
            model.addAttribute("errorMessage", "Please select at least one product");
            model.addAttribute("products", productService.findAll());
            return "order/create";
        }

        // Get the selected products
        List<Product> selectedProducts = selectedProductIds.stream()
                .map(productService::findById)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // Create the order
        Order order = new Order(UUID.randomUUID().toString(), selectedProducts, System.currentTimeMillis(), author);
        Order createdOrder = orderService.createOrder(order);

        // Redirect to order history for the author
        model.addAttribute("author", author);
        model.addAttribute("orders", orderService.findAllByAuthor(author));
        return "order/history";
    }

    @GetMapping("/history")
    public String historyForm() {
        return "order/history-form";
    }

    @PostMapping("/history")
    public String history(@RequestParam("author") String author, Model model) {
        List<Order> orders = orderService.findAllByAuthor(author);
        model.addAttribute("orders", orders);
        model.addAttribute("author", author);
        return "order/history";
    }

    @GetMapping("/pay/{id}")
    public String paymentForm(@PathVariable("id") String orderId, Model model) {
        Order order = orderService.findById(orderId);
        if (order == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }

        model.addAttribute("order", order);
        model.addAttribute("paymentMethods", Arrays.asList(PaymentMethod.values()));
        return "order/payment-form";
    }

    @PostMapping("/pay/{id}")
    public String processPayment(@PathVariable("id") String orderId,
                                 @RequestParam("paymentMethod") String paymentMethod,
                                 @RequestParam Map<String, String> allParams,
                                 Model model) {
        // Find the order
        Order order = orderService.findById(orderId);
        if (order == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }

        // Create payment data from parameters
        Map<String, String> paymentData = new HashMap<>();

        if (PaymentMethod.VOUCHER.getValue().equals(paymentMethod)) {
            paymentData.put("voucherCode", allParams.get("voucherCode"));
        } else if (PaymentMethod.CASH_ON_DELIVERY.getValue().equals(paymentMethod)) {
            paymentData.put("address", allParams.get("address"));
            paymentData.put("deliveryFee", allParams.get("deliveryFee"));
        } else if (PaymentMethod.BANK_TRANSFER.getValue().equals(paymentMethod)) {
            paymentData.put("bankName", allParams.get("bankName"));
            paymentData.put("referenceCode", allParams.get("referenceCode"));
        }

        // Add payment
        Payment payment = paymentService.addPayment(order, paymentMethod, paymentData);

        model.addAttribute("payment", payment);
        model.addAttribute("order", order);
        return "order/payment-success";
    }
}