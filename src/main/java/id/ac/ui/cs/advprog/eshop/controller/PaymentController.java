package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/detail")
    public String detailForm() {
        return "payment/detail-form";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") String paymentId, Model model) {
        Payment payment = paymentService.getPayment(paymentId);
        if (payment == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found");
        }

        model.addAttribute("payment", payment);
        return "payment/detail";
    }

    @PostMapping("/detail")
    public String detailById(@RequestParam("paymentId") String paymentId) {
        return "redirect:/payment/detail/" + paymentId;
    }

    @GetMapping("/admin/list")
    public String adminList(Model model) {
        List<Payment> payments = paymentService.getAllPayments();
        model.addAttribute("payments", payments);
        return "payment/admin-list";
    }

    @GetMapping("/admin/detail/{id}")
    public String adminDetail(@PathVariable("id") String paymentId, Model model) {
        Payment payment = paymentService.getPayment(paymentId);
        if (payment == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found");
        }

        model.addAttribute("payment", payment);
        model.addAttribute("statuses", Arrays.asList(PaymentStatus.values()));
        return "payment/admin-detail";
    }

    @PostMapping("/admin/set-status/{id}")
    public String setStatus(@PathVariable("id") String paymentId,
                            @RequestParam("status") String status) {
        Payment payment = paymentService.getPayment(paymentId);
        if (payment == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found");
        }

        paymentService.setStatus(payment, status);
        return "redirect:/payment/admin/detail/" + paymentId;
    }
}