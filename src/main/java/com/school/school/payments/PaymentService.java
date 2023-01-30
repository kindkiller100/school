package com.school.school.payments;

import com.school.school.exceptions.ValidationException;
import com.school.school.utils.PageableValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository repository;

    public Payment getIfExists(long id) {
        return repository.getIfExists(id);
    }

    public Page<Payment> getAll(Pageable pageable) {
        PageableValidator.checkIsSortValid(Payment.class, pageable);
        return repository.findAll(pageable);
    }

    public void add(Payment payment) {
        ValidationException validationException = new ValidationException();

        if (payment.getSum() <= 0){
            validationException.put("sum", "Сумма должна быть больше нуля.");
        }

        if(repository.existsById(payment.getId())){
            validationException.put("id", "Платеж с id «" + payment.getId() + "» уже существует.");
        }

        validationException.throwExceptionIfIsNotEmpty();

        repository.save(payment);
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
        repository.save(paymentUpdated);
    }

    public void editList(List<Payment> editPayments) {
        editPayments.forEach(this::edit);
    }

    public void delete(long id){
        Payment existedPayment = getIfExists(id);
        repository.delete(existedPayment);
    }
}
