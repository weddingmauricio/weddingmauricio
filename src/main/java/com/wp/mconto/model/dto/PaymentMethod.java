package com.wp.mconto.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PaymentMethod {
    private String type; // "PSE"
    @JsonProperty("user_type")
    private Integer userType; // 0 o 1
    @JsonProperty("user_legal_id_type")
    private String userLegalIdType; // "CC", "CE", etc.
    @JsonProperty("user_legal_id")
    private String userLegalId; // número de cédula o NIT
    @JsonProperty("financial_institution_code")
    private String financialInstitutionCode; // código del banco
    @JsonProperty("payment_description")
    private String paymentDescription; // descripción
    @JsonProperty("phone_number")
    private String phoneNumber;
}