package com.wp.mconto.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WompiMeta {
    // Este objeto está vacío en el ejemplo proporcionado
    // Pero se deja la clase por si en el futuro contiene datos
}