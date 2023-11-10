package jmaster.io.demo.service;

import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Service
public class EmailService {
	@Autowired
	JavaMailSender javaMailSender;
	
	@Autowired
	SpringTemplateEngine templateEngine;
	
	public void testEmail() {
		String to = "vuthanh123456666@gmail.com";
		String subject = "Tieu de email";
		String body = "Noi dung email";
		
		sendEmail(to, subject, body);
	}
	
	
	public void sendBirthdayEmail(String to , String name) {
		String subject = "Happy Birthday"+ name;
		
		Context ctx = new Context();
		ctx.setVariable("name", name);
		
		String body =templateEngine.process("email/hpbd.html", ctx);
		
		sendEmail(to, subject, body);
	}
	
	
	public void sendEmail(String to , String subject , String body) {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());
		try {
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(body, true);
			helper.setFrom("vuthanh10235@gmail.com"); // youremail@gmail.com
			
			javaMailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
