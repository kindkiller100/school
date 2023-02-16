package com.school.school.payments;

import com.school.school.CustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long>, CustomRepository<Payment> {
    boolean existsByStudentId(long id);
}
