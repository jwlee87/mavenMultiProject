package study.mmp.common.component;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import study.mmp.common.exception.system.EmailException;

import com.google.common.base.Charsets;


/**
 * 템플릿 메일 발송 컴포넌트
 * 
 * MailSender + 메일 발송 여부는 MailSenderConfig.class 참조
 */
@Slf4j
public class MailPublisher {

    private JavaMailSender mailSender; 
    
    public MailPublisher(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    public void send(MimeMessage message) throws MessagingException {
        log.debug(message.getSubject() + "send mail to :: {}", Arrays.toString(message.getAllRecipients()));
        mailSender.send(message);
    }
    
    

    public void publish(String subject, String contents, String mailFrom, String[] mailTo) {
        publish(subject, contents, mailFrom, mailTo, new File[]{});
    }
    
    public void publish(String subject, String contents, String mailFrom, String[] mailTo, File attachFile) {
        publish(subject, contents, mailFrom, mailTo, new File[]{attachFile});
    }

    public void publish(String subject, String contents, String mailFrom, String[] mailTo, File[] attachFiles) {
        MimeMessage message;
		try {
			message = createMessage(Charsets.UTF_8, subject, contents, mailFrom, mailTo, attachFiles);
			send(message);
		} catch (UnsupportedEncodingException | MessagingException e) {
			throw new EmailException("", e);
		}
    }
    

    public void publish(MimeMessage message) throws MessagingException {
        send(message);
    }

    
    private MimeMessage createMessage(Charset charSet, String subject, String content, String mailFrom, String[] mailTo, File[] attachFiles) 
            throws MessagingException, UnsupportedEncodingException {
        
        MimeMessage message = mailSender.createMimeMessage();
 
        MimeMessageHelper helper = new MimeMessageHelper(message, attachFiles != null ? true : false, charSet.name());
        helper.setSubject(subject);
        helper.setText(content, isHtml(content));
        helper.setFrom(mailFrom);
        helper.setTo(mailTo);
        if (attachFiles != null) {
        	for (File attachFile : attachFiles) {
                helper.addAttachment(MimeUtility.encodeText(attachFile.getName()), attachFile);
        	}
        }
        return message;
    }
    
    private boolean isHtml(String content) {
    	return StringUtils.contains(content, "<html>");
    }
}
