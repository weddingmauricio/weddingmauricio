package com.wp.mconto.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class MensajeriaService implements IMensajeriaService {

    @Value("${apiKeyMensajeria}")
    private String apikeyMensajeria;

    //TODO CAMBIAR APIKEY

    public void enviarCorreo(String para, String token) throws IOException {
        Email from = new Email("mconto@gmail.com");
        Email to = new Email(para);
        Content content = new Content("text/plain", token);
        Mail mail = new Mail(from, "recupera tu clave", to, content);

        SendGrid sg = new SendGrid(apikeyMensajeria);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            System.out.println("Status: " + response.getStatusCode());
            System.out.println("Body: " + response.getBody());
            System.out.println("Headers: " + response.getHeaders());
        } catch (IOException ex) {
            throw ex;
        }
    }
}
