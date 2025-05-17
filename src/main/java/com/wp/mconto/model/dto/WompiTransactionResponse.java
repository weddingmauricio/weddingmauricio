package com.wp.mconto.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WompiTransactionResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("status")
    private String status;

    @JsonProperty("redirect_url")
    private String redirectUrl;

    @Override
    public String toString() {
        return "WompiTransactionResponse{" +
                "id='" + id + '\'' +
                ", status='" + status + '\'' +
                ", redirectUrl='" + redirectUrl + '\'' +
                '}';
    }
}