package com.devsuperior.dsmeta.services;

import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class SmsService {
    @Autowired
    private SaleRepository saleRepository;
    @Value("${twilio.sid}")
    private String twilioSid;
    @Value("${twilio.key}")
    private String twilioKey;
    @Value("${twilio.phone.from}")
    private String twilioPhoneFrom;
    @Value("${twilio.phone.to}")
    private String twilioPhoneTo;

    public String createMessageContent(Long saleId){
        Sale sale = saleRepository.findSaleById(saleId);
        String date = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH).format(sale.getDate());
        return "Venda nº" + sale.getId() + ". " +
               "Vendedor: " + sale.getSellerName() + ". " +
               "Data: " + date + ". " +
               "Total vendido em " + sale.getVisited() + " visitas: R$" + String.format("%.2f", sale.getAmount()) + ". ";
    }
    public void sendSms(Long saleId) {
        String messageContent = createMessageContent(saleId);
        Twilio.init(twilioSid, twilioKey);

        PhoneNumber to = new PhoneNumber(twilioPhoneTo);
        PhoneNumber from = new PhoneNumber(twilioPhoneFrom);

        Message message = Message.creator(to, from, messageContent).create();

        System.out.println(message.getSid());
    }
}