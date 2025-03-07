package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    OrderService orderService;

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData){
        String paymentId = UUID.randomUUID().toString();
        Payment payment = new Payment(paymentId, order, method, paymentData);
        processPaymentByMethod(payment);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment setStatus(Payment payment, String status){
        payment.setStatus(status);

        if (PaymentStatus.SUCCESS.getValue().equals(status)){
            orderService.updateStatus(payment.getOrder().getId(), OrderStatus.SUCCESS.getValue());
        } else if (PaymentStatus.REJECTED.getValue().equals(status)){
            orderService.updateStatus(payment.getOrder().getId(), OrderStatus.FAILED.getValue());
        }

        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPayment(String paymentId){
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments(){
        return paymentRepository.findAll();
    }

    private void processPaymentByMethod(Payment payment){
        String method = payment.getMethod();

        if (PaymentMethod.VOUCHER.getValue().equals(method)){
            processVoucherPayment(payment);
        } else if (PaymentMethod.BANK_TRANSFER.getValue().equals(method)){
            processBankTransferPayment(payment);
        } else if (PaymentMethod.CASH_ON_DELIVERY.getValue().equals(method)){
            processCashOnDeliveryPayment(payment);
        }
    }

    private void processVoucherPayment(Payment payment) {
        Map<String, String> paymentData = payment.getPaymentData();
        String voucherCode = paymentData.get("voucherCode");

        if (isValidVoucherCode(voucherCode)) {
            setStatus(payment, PaymentStatus.SUCCESS.getValue());
        } else {
            setStatus(payment, PaymentStatus.REJECTED.getValue());
        }
    }

    private boolean isValidVoucherCode(String voucherCode) {
        return voucherCode != null && voucherCode.length() == 16 &&
                voucherCode.startsWith("ESHOP") && countDigits(voucherCode) == 8;
    }

    private int countDigits(String voucherCode) {
        int cnt = 0;
        for (int i = 0; i < voucherCode.length(); i++) {
            if (Character.isDigit(voucherCode.charAt(i))) {
               cnt++;
            }
        }
        return cnt;
    }

    private void processCashOnDeliveryPayment(Payment payment) {
        Map<String, String> paymentData = payment.getPaymentData();
        String address = paymentData.get("address");
        String deliveryFee = paymentData.get("deliveryFee");

        // Check if any required field is empty
        if (address == null || address.isEmpty() || deliveryFee == null || deliveryFee.isEmpty()) {
            setStatus(payment, PaymentStatus.REJECTED.getValue());
        } else {
            setStatus(payment, PaymentStatus.SUCCESS.getValue());
        }
    }

    private void processBankTransferPayment(Payment payment) {
        Map<String, String> paymentData = payment.getPaymentData();
        String bankName = paymentData.get("bankName");
        String referenceCode = paymentData.get("referenceCode");

        // Check if any required field is empty
        if (bankName == null || bankName.isEmpty() || referenceCode == null || referenceCode.isEmpty()) {
            setStatus(payment, PaymentStatus.REJECTED.getValue());
        } else {
            setStatus(payment, PaymentStatus.SUCCESS.getValue());
        }
    }
}
