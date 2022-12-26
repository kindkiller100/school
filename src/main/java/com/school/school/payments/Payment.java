package com.school.school.payments;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "payments", schema="school_db")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "date_time")
    private LocalDateTime dateTime;
    private double sum;
    @Column(name = "student_id")
    private long studentId;
    @Column(name = "auto_identified")
    private boolean autoIdentified;
    private String info;

    public Payment() {
    }

    public Payment(LocalDateTime dateTime, double sum, long studentId, boolean autoIdentified, String info) {
        this.dateTime = dateTime;
        this.sum = sum;
        this.studentId = studentId;
        this.autoIdentified = autoIdentified;
        this.info = info;
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public double getSum() {
        return sum;
    }

    public long getStudentId() {
        return studentId;
    }

    public boolean isAutoIdentified() {
        return autoIdentified;
    }

    public String getInfo() {
        return info;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return id == payment.id && Double.compare(payment.sum, sum) == 0 && studentId == payment.studentId && autoIdentified == payment.autoIdentified && Objects.equals(dateTime, payment.dateTime) && Objects.equals(info, payment.info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateTime, sum, studentId, autoIdentified, info);
    }

    @Override
    public String toString() {
        return "Payment{" +
                "dateTime=" + dateTime +
                ", sum=" + sum +
                ", studentId=" + studentId +
                ", autoIdentified=" + autoIdentified +
                ", info='" + info + '\'' +
                '}';
    }
}
