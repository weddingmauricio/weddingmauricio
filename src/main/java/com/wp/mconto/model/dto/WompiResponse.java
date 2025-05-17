package com.wp.mconto.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WompiResponse {
    @JsonProperty("data")
    private WompiData data;

    @JsonProperty("meta")
    private WompiMeta meta;
}