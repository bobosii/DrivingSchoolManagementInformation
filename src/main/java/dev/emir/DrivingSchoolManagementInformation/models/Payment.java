package dev.emir.DrivingSchoolManagementInformation.models;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;

import java.time.LocalDate;

@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long amount;
    private LocalDate paymentDate;
    private String method;

    @ManyToOne
    private Student student;

    public Payment(){}

    public Payment(Long id, Long amount, LocalDate paymentDate, String method, Student student) {
        this.id = id;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.method = method;
        this.student = student;
    }
}