package com.ertb.controllers;

import com.ertb.model.PaymentRequest;
import com.ertb.model.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/mock-payment")
@RequiredArgsConstructor
public class MockPaymentController {

    @PostMapping("/pay")
    public ResponseEntity<PaymentResponse> makePayment(@RequestBody PaymentRequest request) {
        // simulate processing delay
        try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        PaymentResponse response = new PaymentResponse();
        response.setTransactionId(UUID.randomUUID().toString());
        response.setAmount(request.getAmount());
        response.setUserId(request.getUserId());
        response.setSuccess(true); // toggle to false to simulate failure
        response.setMessage("Mock payment processed successfully");

        return ResponseEntity.ok(response);
    }
}
