package study.mmp.common.configuration;


import java.io.InputStream;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;

import study.mmp.common.component.MailPublisher;
import study.mmp.common.component.TemplateMailHelper;



/**
 * Mail Context Configure - 메일 발송 관련 Context
 * 
 * 1.TemplateEmailPublisher 템플릿 메일 발송 컴포넌트
 *   리얼 : 기본 발송, sendMail 실행 옵션 있다면 실행 옵션에 따라...
 *   테스트 : 기본 미발송, sendMail 실행 옵션 있다면 실행 옵션에 따라
 *   
 *   
 * 2.JobExceptionMailNotifyHandler 예외, 오류 메일 알림 컴포넌트
 *   REAL : observer가 등록되어 있지 않다면
 *   DEV  : observer가 등록되어 있지 않다면 미발송

 *
 */
@Configuration
@Slf4j
public class MailSenderConfig {
    
    @Value("${smtp.host.clean}")
    private String externalSmtpHost;    //외부 발송요 도메인
    
    @Value("${smtp.host.support}")
    private String internalSmtpHost;    //내부 - 개발용 도메인

    @Bean
    public TemplateMailHelper templateMailHelper(MailPublisher mailPublisher) {
    	TemplateMailHelper templateMailHelper = new TemplateMailHelper();
    	templateMailHelper.setMailPublisher(mailPublisher);
    	return templateMailHelper;
    }
    
    /**
     * TemplateMail 발송 Component
     * @return
     */
    @Bean
    public MailPublisher mailPublisher(JavaMailSender externalMailSender, JavaMailSender dummyMailSender) {
    	MailPublisher mailPublisher = new MailPublisher(externalMailSender);
    	return mailPublisher;
    }

    
    /**
     * 실제 메일 발송 MailSender
     * @return
     */
    @Bean
    public JavaMailSender externalMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(externalSmtpHost);
        return mailSender;   
    }
    
    /**
     * 실제 메일 발송 MailSender
     * @return
     */
    @Bean
    public JavaMailSender internalMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(internalSmtpHost);
        return mailSender;   
    }
    
    /**
     * 더미 Mail MailSender
     * @return
     */
    @Bean
    public JavaMailSender dummyMailSender() {

        JavaMailSender dummyMailSender = new JavaMailSender() {
            
            @Override
            public void send(SimpleMailMessage... simpleMessages) throws MailException { }
            
            @Override
            public void send(SimpleMailMessage simpleMessage) throws MailException { }
            
            @Override
            public void send(MimeMessagePreparator... mimeMessagePreparators) throws MailException { }
            
            @Override
            public void send(MimeMessagePreparator mimeMessagePreparator) throws MailException { }
            
            @Override
            public void send(MimeMessage... mimeMessages) throws MailException { }
            
            @Override
            public void send(MimeMessage mimeMessage) throws MailException {
                log.info("Dummy Mail Sender - Not Send");   
            }
            
            @Override
            public MimeMessage createMimeMessage(InputStream contentStream) throws MailException { return null; }
            
            @Override
            public MimeMessage createMimeMessage() {
                Session msgSession = Session.getDefaultInstance(new Properties(), null);
                msgSession.setDebug(true);

                //Message 클래스의 객체를 Session을 이용해 생성
                MimeMessage msg = new MimeMessage(msgSession);
                return msg;
            }
        };
        return dummyMailSender;
    }
}
