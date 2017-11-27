package study.mmp.common.component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import study.mmp.common.component.mail.MailTemplate;


public class TemplateMailHelper {
	
	private Map<String, String> mailTemplateFileCache = new ConcurrentHashMap<>();
	
	@Setter @Getter private MailPublisher mailPublisher;
	
	
	public void send(MailTemplate template, Map<String, Object> values, String... to) {
		
		String content = getMailContent(template.templateFileName(), template.templateContent(), values);
		mailPublisher.publish(template.subject(), content, template.from(), to);
	}
	
	public void send(String subject, String mailTemplateFileName, Map<String, Object> values, File[] attachFileName, String from, String... to) {
		
		mailPublisher.publish(subject, getMailContent(mailTemplateFileName, null, values), from, to);
	}

	public String getMailContent(String mailContent, Map<String, Object> values) {
		return makeContent(mailContent, values, "");
	}

	
	
	private String getMailContent(String mailTemplateFileName, String content, Map<String, Object> values) {

		if (!mailTemplateFileCache.containsKey(mailTemplateFileName)) {
			String cont = getContents(mailTemplateFileName);
			if (StringUtils.isEmpty(cont)) {
				cont = content;
			}
			if (StringUtils.isEmpty(cont)) {
				throw new RuntimeException("메일 내용이 지정되지 않았습니다.");
			}
			mailTemplateFileCache.put(mailTemplateFileName, getContents(mailTemplateFileName));
		}
		
		String cachedMailContent = mailTemplateFileCache.get(mailTemplateFileName);
		return getMailContent(cachedMailContent, values);
	}
	
	
	@SuppressWarnings("unchecked")
	private String makeContent(String cont, Map<String, Object> values, String prefix) {
		
		for (String key : values.keySet()) {
			Object value = values.get(key);
			if (value instanceof List) {
				
				String mac = getMacroKey(key, prefix);
				String sub = "";
				int s = cont.indexOf(mac);
				int l = cont.lastIndexOf(mac);
				if (s > 0 && l > 0 && l > s) {
					sub = cont.substring(s, (l + mac.length()));
					cont = cont.replace(sub, "");
				} else {
					throw new RuntimeException("[" + mac + "] not found or odd.");
				}
				 
				List<Map<String, Object>> list = (List<Map<String, Object>>) value;
				Collections.reverse(list);
				for (Map<String, Object> tmpValue : list) {
					sub = StringUtils.replace(sub, mac, "");
					cont = cont.substring(0, s) + makeContent(sub, tmpValue, prefix + key + ".") + cont.substring(s);
				}
			 } else {
				 cont = StringUtils.replace(cont, getMacroKey(key, prefix), String.valueOf(value));

			 }
		}
		int s = cont.indexOf(MAIL_MACRO_DELITER);
		int l = cont.lastIndexOf(MAIL_MACRO_DELITER);
		if (s > 0 && l > 0 && l > s) {
			return cont.replace(cont.substring(s, (l + 7)), "");
 
		} else {
			return cont;
		}
	}
	
	
	private String getMacroKey(String key, String prefix) {
		if (StringUtils.isEmpty(prefix)) {
			return MAIL_MACRO_DELITER + key + MAIL_MACRO_DELITER;
		} else {
			System.out.println(MAIL_MACRO_DELITER + prefix + key + MAIL_MACRO_DELITER);
			return MAIL_MACRO_DELITER + prefix + key + MAIL_MACRO_DELITER;
		}
	}

	private static final String MAIL_MACRO_DELITER = "<marco>";
    private static final String MAIL_TEMPLATE_PATH = "classpath:mail/template/";

    public static String getContents(String mailTemplateFileName)  {
        DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
        Resource mailTemplate = defaultResourceLoader.getResource(MAIL_TEMPLATE_PATH + mailTemplateFileName);
 
        
        try (InputStream in = mailTemplate.getInputStream()) {
            
            byte[] mail = FileCopyUtils.copyToByteArray(in);
            return new String(mail, "utf-8");
            
        } catch (IOException ioe) {
            
            throw new NullPointerException("Not Found File :" + MAIL_TEMPLATE_PATH + mailTemplateFileName);
        }
    }
}
