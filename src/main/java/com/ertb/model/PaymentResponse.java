package com.ertb.model;

import com.ertb.enumerations.PaymentStatus;
import lombok.Data;

@Data
public class PaymentResponse {

    private String transactionReferenceId;

    private double amount;

    private String paymentStatus;

    private String message;
}
