package com.wp.mconto.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class MensajeriaService implements IMensajeriaService {


    //TODO CAMBIAR APIKEY

private final String apiKey = "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";

    public void enviarCorreo(String para, String token) throws IOException {
        Email from = new Email("mconto@gmail.com");
        Email to = new Email(para);
        Content content = new Content("text/plain", token);
        Mail mail = new Mail(from, "recupera tu clave", to, content);

        SendGrid sg = new SendGrid(apiKey);
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
