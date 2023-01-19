package com.school.school.payments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment getIfExists(long id) {
        return paymentRepository
                .findById(id)
                .orElseThrow(
                        () -> new NotFoundException("Платеж с номером " + id + " не найден.")
                );
    }

    public List<Payment> getAll() {
        return paymentRepository.findAll();
    }

    public void add(Payment payment) {
        if (payment.getSum() <= 0){
            throw new NumberFormatException("Сумма должна быть больше нуля.");
        }
        paymentRepository.save(payment);
    }

    public void addList(List<Payment> payments) {
        payments.forEach(this::add);
    }

    public void edit(Payment editPayment) {
        long id = editPayment.getId();
        Payment existedPayment = getIfExists(id);
        //клонируем поля существующего платежа, перезаписываем autoIdentified и studentId новыми данными
        Payment paymentUpdated = existedPayment
                .clone()
                .setAutoIdentified(editPayment.isAutoIdentified()) //
                .setStudentId(editPayment.getStudentId())
                .build();
        paymentRepository.save(paymentUpdated);
    }

    public void editList(List<Payment> editPayments) {
        editPayments.forEach(this::edit);
    }

    public void delete(long id){
        Payment existedPayment = getIfExists(id);
        paymentRepository.delete(existedPayment);
    }
}
