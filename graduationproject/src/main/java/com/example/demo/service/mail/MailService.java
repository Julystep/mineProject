package com.example.demo.service.mail;

import com.example.demo.mapper.mail.MailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.SecureRandom;
import java.util.Random;

@Service
public class MailService {

    @Autowired
    JavaMailSenderImpl javaMailSender;

    @Resource
    MailMapper mailMapper;

    @Value("${spring.mail.username}")
    private String sender;

    /**
     * Math.random生成的是一般随机数，采用的是类似于统计学的随机数生成规则，其输出结果很容易预测，
     * 因此可能导致被攻击者击中。而SecureRandom是真随机数，采用的是类似于密码学的随机数生成规则，
     * 其输出结果较难预测，若想要预防被攻击者攻击，最好做到使攻击者根本无法，或不可能鉴别生成的随
     * 机值和真正的随机值。
     */
    private static final String SYMBOLS = "0123456789";
    private static final Random RANDOM = new SecureRandom();

    @Async
    public String sendEmailVerCode(String receiver) throws MailSendException {
        /*随机出来的验证码*/
        char[] nonceChars = new char[6];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        String verCode = new String(nonceChars);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("验证码");	//设置邮件标题
        message.setText("尊敬的用户,您好:\n"
                + "\n本次请求的邮件验证码为:" + verCode + ",本验证码5分钟内有效，请及时输入。（请勿泄露此验证码）\n"
                + "\n如非本人操作，请忽略该邮件。\n(这是一封自动发送的邮件，请不要直接回复）");	//设置邮件正文
        message.setTo(receiver);	//设置收件人
        message.setFrom(sender);	//设置发件人
        javaMailSender.send(message);	//发送邮件

        return verCode;
    }

    public String getEmailByUser_id(String user_id) {

        return mailMapper.getEmailByUser_id(user_id);

    }
}
