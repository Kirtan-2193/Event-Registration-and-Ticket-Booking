package com.ertb.services;

import com.ertb.exceptions.DataNotFoundException;
import com.ertb.exceptions.DataValidationException;
import com.ertb.exceptions.PaymentValidationException;
import com.ertb.model.PaymentClientModel;
import com.ertb.model.PaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private static final String PAYMENT_SERVICE_ENDPOINT = "/payments";

    private final WebClient paymentWebClient;


    public PaymentClientModel makePayment(final Long accountNumber, final double amount) {
        if (accountNumber == null || amount <= 0) {
            throw new DataValidationException("Account number not valid, Please Enter a valid account number");
        } else {
            final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
            if (accountNumber != null) {
                queryParams.add("accountNumber", String.valueOf(accountNumber));
            }
            if (amount > 0) {
                queryParams.add("amount", String.valueOf(amount));
            }

            try {
                return paymentWebClient.post()
                        .uri(uriBuilder -> uriBuilder
                                .path(PAYMENT_SERVICE_ENDPOINT+"/create")
                                .queryParams(queryParams).build())
                        .retrieve()
                        .bodyToFlux(PaymentClientModel.class)
                        .blockFirst();
            } catch (final Exception e) {
                log.error("Error occurred while making payment: {}", e.getMessage());
                throw new PaymentValidationException("Payment failed, Please try again.");
            }
        }
    }



    public PaymentClientModel refundPayment(final String paymentReferenceId) {
        try {
            return paymentWebClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path(PAYMENT_SERVICE_ENDPOINT+"/refund")
                            .queryParam("paymentId", paymentReferenceId).build())
                    .retrieve()
                    .bodyToFlux(PaymentClientModel.class)
                    .blockFirst();
        } catch (Exception e) {
            log.error("Error occurred while refund payment: {}", e.getMessage());
            throw new PaymentValidationException("Refund is fail, Please try again.");
        }
    }
}
