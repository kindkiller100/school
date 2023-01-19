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

    @GetMapping("/{id}")
    public Payment getById(long id){
        return paymentService.getIfExists(id);
    }
    @GetMapping
    public List<Payment> getAll(){
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
    @PutMapping
    public void edit(@RequestBody Payment payment){
       paymentService.edit(payment);
    }

    @PutMapping("/list")
    public void editList(@RequestBody List<Payment> payments){
        paymentService.editList(payments);
    }

    @DeleteMapping("/{id}")
    public void delete(long id){
        paymentService.delete(id);
    }
}
