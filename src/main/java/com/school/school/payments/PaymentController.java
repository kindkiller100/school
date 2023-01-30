package com.school.school.payments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    PaymentService service;

    @GetMapping("/{id}")
    public Payment getById(long id){
        return service.getIfExists(id);
    }
    @GetMapping
    public Page<Payment> getAll(Pageable pageable){
        return service.getAll(pageable);
    }

    @PostMapping
    public void add(@RequestBody Payment payment){
        service.add(payment);
    }

    @PostMapping("/list")
    public void addList(@RequestBody List<Payment> payments){
        service.addList(payments);
    }
    @PutMapping
    public void edit(@RequestBody Payment payment){
       service.edit(payment);
    }

    @PutMapping("/list")
    public void editList(@RequestBody List<Payment> payments){
        service.editList(payments);
    }

    @DeleteMapping("/{id}")
    public void delete(long id){
        service.delete(id);
    }
}
