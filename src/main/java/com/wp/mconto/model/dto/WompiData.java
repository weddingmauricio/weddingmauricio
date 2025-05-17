package com.wp.mconto.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WompiData {
    @JsonProperty("id")
    private String id;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("finalized_at")
    private String finalizedAt;

    @JsonProperty("amount_in_cents")
    private Long amountInCents;

    @JsonProperty("reference")
    private String reference;

    @JsonProperty("customer_email")
    private String customerEmail;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("payment_method_type")
    private String paymentMethodType;

    @JsonProperty("payment_method")
    private WompiPaymentMethod paymentMethod;

    @JsonProperty("status")
    private String status;

    @JsonProperty("status_message")
    private String statusMessage;

    @JsonProperty("billing_data")
    private Object billingData;

    @JsonProperty("shipping_address")
    private Object shippingAddress;

    @JsonProperty("redirect_url")
    private String redirectUrl;

    @JsonProperty("payment_source_id")
    private String paymentSourceId;

    @JsonProperty("payment_link_id")
    private String paymentLinkId;

    @JsonProperty("customer_data")
    private Object customerData;

    @JsonProperty("bill_id")
    private String billId;

    @JsonProperty("taxes")
    private List<Object> taxes;

    @JsonProperty("tip_in_cents")
    private Long tipInCents;
}