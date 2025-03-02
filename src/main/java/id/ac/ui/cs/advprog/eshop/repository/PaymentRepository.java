package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.ArrayList;

@Repository
public class PaymentRepository {
    private List<Payment> paymentData = new ArrayList<>();

    public Payment save(Payment payment){
        for (int i=0; i<paymentData.size(); i++){
            if (paymentData.get(i).getId() == payment.getId()){
                paymentData.set(i, payment);
                return payment;
            }
        }
        paymentData.add(payment);
        return payment;
    }

    public Payment findById(String id){
        for (int i=0; i<paymentData.size(); i++){
            if (paymentData.get(i).getId().equals(id)){
                return paymentData.get(i);
            }
        }
        return null;
    }

    public List<Payment> findAll(){
        return new ArrayList<>(paymentData);
    }
}
