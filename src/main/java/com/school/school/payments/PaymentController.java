package com.school.school.payments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    //@GetMapping("/{id}") //нужен ли?
    @GetMapping
    public List<Payment> getAll(){ //потом убрать
        return paymentService.getAll();
    }

    @PostMapping
    public void add(@RequestBody Payment payment){
        paymentService.add(payment);
    }

    @PostMapping("/list")
    public void addList(@RequestBody List<Payment> payments){
        paymentService.addList(payments);
    }
    @PutMapping("/edit")
    public void edit(@RequestBody Payment payment){
       paymentService.edit(payment);
    }

    @PutMapping("/edit/list")
    public void editList(@RequestBody List<Payment> payments){
        paymentService.editList(payments);
    }

    //@DeleteMapping //нужен ли?
}
