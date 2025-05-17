package com.wp.mconto.controller;

import com.wp.mconto.model.dto.*;
import com.wp.mconto.service.WompiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/wp-mconto/pagos")
public class PagoCtr {

    @Autowired
    WompiService wompiService;


    @PostMapping("/crear")
    public ResponseEntity<WompiResponse> crearPago(@RequestBody PagoRequest request) {
        // Convertir PagoRequest a WompiTransactionRequest
        WompiTransactionRequest wompiRequest = new WompiTransactionRequest();
        wompiRequest.setAmountInCents(request.getMontoCentavos());
        wompiRequest.setCurrency("COP");  // O la moneda que corresponda
        wompiRequest.setReference(UUID.randomUUID().toString());  // O tu sistema de referencia
        wompiRequest.setRedirectUrl("https://tudominio.com/pagos/respuesta");
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setType(request.getType());
        paymentMethod.setUserType(request.getUserType());
        paymentMethod.setPaymentDescription(request.getPaymentDescription());
        paymentMethod.setFinancialInstitutionCode(request.getFinancialInstitutionCode());
        paymentMethod.setUserLegalId(request.getUserLegalId());
        paymentMethod.setUserLegalIdType(request.getUserLegalIdType());
        paymentMethod.setPhoneNumber(request.getTelefonoCliente());

        wompiRequest.setPaymentMethod(paymentMethod);

        wompiRequest.setFullName(request.getNombreCliente());
        wompiRequest.setEmail(request.getEmailCliente());
        wompiRequest.setPhoneNumber(request.getTelefonoCliente());
        wompiRequest.setAcceptanceToken(request.getAcceptanceToken());

        WompiResponse response = wompiService.crearTransaccion(wompiRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verificar/{transactionId}")
    public ResponseEntity<WompiTransactionDetail> verificarPago(@PathVariable String transactionId) {
        WompiTransactionDetail detail = wompiService.consultarTransaccion(transactionId);
        return ResponseEntity.ok(detail);
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> wompiWebhook(@RequestBody String eventData) {
        wompiService.procesarWebhook(eventData);
        return ResponseEntity.ok("Evento procesado correctamente");
    }
}