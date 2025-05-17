package com.wp.mconto.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.wp.mconto.config.WompiConfig;
import com.wp.mconto.model.dto.*;
import com.wp.mconto.model.pago.Transaccion;
import com.wp.mconto.repository.TransaccionRepo;
import com.wp.mconto.util.WompiSignatureUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Service
public class WompiService {

    @Value("${wompi.public.key}")
    private String publicKey;

    @Value("${wompi.private.key}")
    private String privateKey;

    @Value("${wompi.integrity.key}")
    private String integrityKey;

    private final WompiConfig wompiConfig;
    private final RestTemplate restTemplate;
    private final TransaccionRepo transaccionRepository;

    @Autowired
    public WompiService(WompiConfig wompiConfig, RestTemplate restTemplate, TransaccionRepo transaccionRepository) {
        this.wompiConfig = wompiConfig;
        this.restTemplate = restTemplate;
        this.transaccionRepository = transaccionRepository;
    }

    public WompiResponse crearTransaccion(WompiTransactionRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(publicKey);
        request.setAcceptanceToken(obtenerAcceptanceToken(publicKey));
        String signature = WompiSignatureUtil.generateSignature(
                request.getReference(),
                request.getAmountInCents(),
                request.getCurrency(),
                integrityKey
        );
        request.setSignature(signature);
        HttpEntity<WompiTransactionRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<WompiResponse> response = restTemplate.exchange(
                wompiConfig.getApiUrl() + "/transactions",
                HttpMethod.POST,
                entity,
                WompiResponse.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            WompiResponse transactionResponse = response.getBody();

            // Guardar la transacción en la base de datos
            Transaccion transaccion = new Transaccion();
            transaccion.setReferencia(request.getReference());
            transaccion.setWompiTransactionId(transactionResponse.getData().getId());
            transaccion.setMonto(new BigDecimal(request.getAmountInCents()).divide(new BigDecimal(100)));
            transaccion.setEstado(transactionResponse.getData().getStatus());
            transaccion.setMoneda(request.getCurrency());
            transaccion.setFechaCreacion(LocalDateTime.now());

            transaccionRepository.save(transaccion);

            return transactionResponse;
        } else {
            throw new RuntimeException("Error al crear transacción en Wompi: " + response.getStatusCodeValue());
        }
    }

    public WompiTransactionDetail consultarTransaccion(String wompiTransactionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + wompiConfig.getPrivateKey());

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<WompiTransactionDetail> response = restTemplate.exchange(
                wompiConfig.getApiUrl() + "/transactions/" + wompiTransactionId,
                HttpMethod.GET,
                entity,
                WompiTransactionDetail.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException("Error al consultar transacción en Wompi: " + response.getStatusCodeValue());
        }
    }

    // Método para manejar webhooks de Wompi
    public void procesarWebhook(String eventData) {
        // Parse JSON data
        // Update transaction status
        // Implement business logic

    }

    public String obtenerAcceptanceToken(String publicKey) {
        String url = "https://sandbox.wompi.co/v1/merchants/" + publicKey;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);
        return response.getBody()
                .get("data")
                .get("presigned_acceptance")
                .get("acceptance_token")
                .asText();
    }
}