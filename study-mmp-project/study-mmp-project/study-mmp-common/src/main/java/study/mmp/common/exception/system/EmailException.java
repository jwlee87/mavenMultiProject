package study.mmp.common.exception.system;

@SuppressWarnings("serial")
public class EmailException extends RuntimeException {

    public EmailException(String msg, Exception ex) {
        super(msg, ex);
    }
}
