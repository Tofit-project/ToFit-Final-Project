package com.tofit.mvc.model.service;

import java.util.UUID;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.config.validate.Validated.Secret;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {
	
	private final JavaMailSender javaMailSender;
	
	public MailService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	private MimeMessage createMessage(String code, String email) throws MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		
		message.addRecipients(Message.RecipientType.TO, email); // 보낼 이메일
		message.setSubject("ToFit 회원가입 인증 번호입니다."); // 제목
		message.setText("이메일 인증코드: " + code); // 보
		
		message.setFrom("yoonji0624@naver.com");
		
		return message;
		
	}
	
	public boolean sendMail(String code, String email) throws Exception {
		try {
			MimeMessage mimeMessage = createMessage(code, email);
			javaMailSender.send(mimeMessage);
			return true;
		} catch (MailException mailException) {
			mailException.printStackTrace();
			throw new IllegalAccessException();
		}
	}
	
}
