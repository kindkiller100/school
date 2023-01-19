package com.school.school.payments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public void add(Payment payment){//валидация?
        paymentRepository.save(payment);
    }

    public void addList(List<Payment> payments){
        payments.forEach(p -> paymentRepository.save(p));
    }
    public void edit(Payment payment){//какие параметры разрешено менять?
        //+ifExists
        paymentRepository.save(payment);
    }

    public void editList(List<Payment> payments){
        //+ifExists
        payments.forEach(p -> paymentRepository.save(p));
    }
}
