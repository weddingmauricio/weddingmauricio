package com.wp.mconto.model.dto;

import lombok.Getter;

@Getter
public class PagoRequest {

    private Long montoCentavos;
    private String nombreCliente;
    private String emailCliente;
    private String telefonoCliente;
    private String type;
    private Integer userType;
    private String userLegalIdType;
    private String userLegalId;
    private String financialInstitutionCode;
    private String paymentDescription;
    private String acceptanceToken;

}
