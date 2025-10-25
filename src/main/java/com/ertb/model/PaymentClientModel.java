package com.ertb.model;

import lombok.Data;

@Data
public class PaymentClientModel {

    private String paymentId;

    private double amount;

    private String paymentStatus;
}
