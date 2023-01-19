package com.school.school.payments;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "payments", schema = "school_db")
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
    private Boolean autoIdentified;
    /*
    autoIdentified может принимать следующие значения:
        true - запись создана обработчиком, идентифицирована студентом и не требует ручной проверки
        false - запись создана обработчиком, но требует ручной проверки
        null - запись создана но не идентифицирована студентом
     */
    private String info;

    private Payment() {
    }

    private Payment(long id,
                    LocalDateTime dateTime,
                    double sum,
                    long studentId,
                    Boolean autoIdentified,
                    String info) {
        this.id = id;
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

    public Boolean isAutoIdentified() {
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
        return id == payment.id && Double.compare(payment.sum, sum) == 0 && studentId == payment.studentId && Objects.equals(dateTime, payment.dateTime) && Objects.equals(autoIdentified, payment.autoIdentified) && Objects.equals(info, payment.info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateTime, sum, studentId, autoIdentified, info);
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", sum=" + sum +
                ", studentId=" + studentId +
                ", autoIdentified=" + autoIdentified +
                ", info='" + info + '\'' +
                '}';
    }

    public Builder clone() {
        return new Builder()
                .setId(this.id)
                .setDateTime(this.dateTime)
                .setSum(this.sum)
                .setStudentId(this.studentId)
                .setAutoIdentified(this.autoIdentified)
                .setInfo(this.info);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long id;
        private LocalDateTime dateTime;
        private double sum;
        private long studentId;
        private Boolean autoIdentified;
        private String info;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setDateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
            return this;
        }

        public Builder setSum(double sum) {
            this.sum = sum;
            return this;
        }

        public Builder setStudentId(long studentId) {
            this.studentId = studentId;
            return this;
        }

        public Builder setAutoIdentified(Boolean autoIdentified) {
            this.autoIdentified = autoIdentified;
            return this;
        }

        public Builder setInfo(String info) {
            this.info = info;
            return this;
        }

        public Payment build() {
            return new Payment(
                    this.id,
                    this.dateTime,
                    this.sum,
                    this.studentId,
                    this.autoIdentified,
                    this.info
            );
        }
    }
}
