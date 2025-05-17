package com.wp.mconto.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WompiTransactionRequest {
    @JsonProperty("amount_in_cents")
    private Long amountInCents;
    @JsonProperty("customer_full_name")
    private String fullName;
    @JsonProperty("customer_email")
    private String email;
    @JsonProperty("customer_phone_number")
    private String phoneNumber;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("reference")
    private String reference;
    @JsonProperty("redirect_url")
    private String redirectUrl;
    @JsonProperty("payment_method")
    private PaymentMethod paymentMethod;
    @JsonProperty("acceptance_token")
    private String acceptanceToken;
    @JsonProperty("signature")
    private String signature;
}
