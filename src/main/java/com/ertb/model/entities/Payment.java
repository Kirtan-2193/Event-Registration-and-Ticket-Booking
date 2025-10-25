package com.ertb.model.entities;

import com.ertb.enumerations.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "payments")
@Data
public class Payment {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid")
    @Column(name = "payment_id")
    private String paymentId;

    private double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @Column(name = "transaction_reference_id")
    private String transactionReferenceId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
