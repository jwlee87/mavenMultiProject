package study.mmp.common.component.mail;

import java.io.File;

public abstract class MailTemplate {

    public String charset() {
        return "UTF-8";
    }
    
    public abstract String subject();
    
    public abstract String from();

    public String templateContent() {
    	return null;
    }
    
    public String templateFileName() {
    	return null;
    }
    
    public File[] attactFiles() {
		return null;
    };
}
