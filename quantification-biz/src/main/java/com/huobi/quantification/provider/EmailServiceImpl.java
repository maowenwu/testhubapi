package com.huobi.quantification.provider;

import com.google.common.base.Throwables;
import com.huobi.quantification.api.email.EmailService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.SimpleMailReqDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public ServiceResult sendSimpleMail(SimpleMailReqDto reqDto) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo("542703524@qq.com");
            message.setSubject(reqDto.getSubject());
            message.setText(reqDto.getText());
            mailSender.send(message);
            return ServiceResult.buildSuccessResult(null);
        } catch (Throwable e) {
            logger.error("发送邮件失败", e);
            return ServiceResult.buildSystemErrorResult(Throwables.getStackTraceAsString(e));
        }
    }
}
