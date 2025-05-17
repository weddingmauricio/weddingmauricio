package com.wp.mconto.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WompiPaymentExtra {
    @JsonProperty("is_three_ds")
    private Boolean isThreeDs;

    @JsonProperty("three_ds_auth_type")
    private String threeDsAuthType;
}