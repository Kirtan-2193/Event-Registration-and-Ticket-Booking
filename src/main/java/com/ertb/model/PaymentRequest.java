package com.ertb.model;

import lombok.Data;

@Data
public class PaymentRequest {

    private double amount;
    private String userId;
    private String description;

    public PaymentRequest(double totalAmount, String userId, String message) {
        this.amount = totalAmount;
        this.userId = userId;
        this.description = message;
    }
}
