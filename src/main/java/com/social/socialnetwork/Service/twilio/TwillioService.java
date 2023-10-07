package com.social.socialnetwork.Service.twilio;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwillioService {

    @Value("${twillio.client.id}")
    private static final String ACCOUNT_SID = "AC4493db833866957132f8f660fdd145ee";
    @Value("${twillio.client.secret}")
    private static final String AUTH_TOKEN = "a88a4ed9bcda5e3fb69f3ac64713f2f9";
    @Value("${twillio.client.from}")
    private String TWILLIO_SENDER = "(434) 433-8755";

    public void sendSMS(String phone, String token) {
        try {

            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            Message message = Message.creator(new PhoneNumber(phone), new PhoneNumber(TWILLIO_SENDER),"Your OTP is: " + token)
                    .create();
            System.out.println(message.getSid());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
