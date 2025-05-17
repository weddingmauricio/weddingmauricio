package com.wp.mconto.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WompiTransactionDetail {

    private String id;
    private String status;
    private Amount amount;
    private String paymentMethodType;
    private PaymentMethod paymentMethod;
    private String referenceId;
    private CustomerData customer;
    private LocalDateTime createdAt;
    private String currency;
    private PaymentStatus paymentStatus;

    @Data
    public static class Amount {
        private Long amountInCents;
        private String currency;
    }

    @Data
    public static class PaymentMethod {
        private String type;
        private String token;
        private String installments;
        private CardInfo card;
    }

    @Data
    public static class CardInfo {
        private String brand;
        private String lastFour;
        private String expirationMonth;
        private String expirationYear;
        private String cardHolder;
    }

    @Data
    public static class CustomerData {
        private String email;
        private String fullName;
        private String phoneNumber;
        private String legalId;
        private String legalIdType;
    }

    @Data
    public static class PaymentStatus {
        private String status;
        private String message;
        private String reason;
    }

}
