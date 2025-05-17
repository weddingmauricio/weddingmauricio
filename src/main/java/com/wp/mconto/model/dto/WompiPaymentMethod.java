package com.wp.mconto.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WompiPaymentMethod {

    @JsonProperty("type")
    private String type;

    @JsonProperty("extra")
    private WompiPaymentExtra extra;

    @JsonProperty("user_type")
    private Integer userType;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("user_legal_id")
    private String userLegalId;

    @JsonProperty("user_legal_id_type")
    private String userLegalIdType;

    @JsonProperty("payment_description")
    private String paymentDescription;

    @JsonProperty("financial_institution_code")
    private String financialInstitutionCode;
}
