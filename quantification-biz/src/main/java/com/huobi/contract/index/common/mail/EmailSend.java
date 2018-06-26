package com.huobi.contract.index.common.mail;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSend {
	private static final Logger LOG = LoggerFactory.getLogger(EmailSend.class);
	@Autowired
	private JavaMailSender javaMailSender;
	@Value("${spring.mail.username}")
	private String username;

	public void sendEmail(String to, String subject, String content) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(username);
			message.setTo(to);
			message.setSubject(subject);
			message.setText(content);
			javaMailSender.send(message);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	public void sendEmailHtml(String to, String subject, String content) {
		try {
			MimeMessage msg = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(msg, true, "utf-8");
			helper.setFrom(username);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content, true);
			javaMailSender.send(msg);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
}
