package com.school.school.payments;

import com.school.school.students.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public List<Payment> getAll() { //потом убрать
        return paymentRepository.findAll();
    }

    public void add(Payment payment) {//валидация?
        paymentRepository.save(payment);
    }

    public void addList(List<Payment> payments) {
        payments.forEach(p -> paymentRepository.save(p));
    }

    public void edit(Payment editPayment) {//какие параметры разрешено менять?
        long id = editPayment.getId();
        if (!paymentRepository.existsById(id)) {
            throw new NotFoundException("Payment with id «" + id + "» not found.");
        }
        paymentRepository.save(editPayment);
    }

    public void editList(List<Payment> editPayments) {
        editPayments.forEach(this::edit);
    }
}
