package com.ertb.model;

import lombok.Data;

@Data
public class PaymentResponse {

    private String transactionId;

    private double amount;

    private String userId;

    private boolean success;

    private String message;
}
