package com.talk2amareswaran.projects.socialloginapp.email;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;


public class SendingEmail{
	
@Value("spring.mail.username")
private String MESSAGE_FROM;
	
private JavaMailSender javaMailSender;

private final Logger log = LoggerFactory.getLogger(SendingEmail.class);

@Autowired
public SendingEmail(JavaMailSender sender) {
	this.javaMailSender = sender;
}

public void sendEmail(String email) {
		
    SimpleMailMessage msg = null;
    try {
    msg = new SimpleMailMessage();
    msg.setFrom(MESSAGE_FROM);
    msg.setTo(email);
    msg.setSubject("Verification email");
    msg.setText("Click this link to confirm your email address and complete setup for your account."
			+ "\n\nVerification Link: " + "http://localhost:8080/");

    javaMailSender.send(msg);
    log.info("Email sent");
    }catch(Exception e) {
     log.error("Email not sent");
     
    }
   

}
}

