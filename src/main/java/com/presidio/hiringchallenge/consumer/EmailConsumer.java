package com.presidio.hiringchallenge.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailConsumer {
    @Autowired
    JavaMailSender mailSender;
    @Value("${from}")
    String from;
    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
    public void receive(String message) {
        try{
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            String content = "We are pleased to inform you that your details have been successfully saved in our system.\n\n"
                    + "Thank you for choosing Rentify! You can now proceed to explore and enjoy the services we offer.\n\n"
                    + "If you have any questions or need further assistance, feel free to reach out to our support team.\n\n"
                    + "Best regards,\n"
                    + "The Rentify Team";
            simpleMailMessage.setTo(message.substring(message.indexOf("Saved ") + 6));
            simpleMailMessage.setText(content);
            simpleMailMessage.setFrom(from);
            simpleMailMessage.setSubject("Welcome to Rentify");
            mailSender.send(simpleMailMessage);
            log.info("Welcome email Sent !!!!");
        }
        catch(Exception e){
            e.printStackTrace();
            log.error("Error while sending mail to customer " +e.getMessage());
        }
    }
}
