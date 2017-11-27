package study.mmp.web.exception;

@SuppressWarnings("serial")
public class NeedLoginException extends RuntimeException {

    public NeedLoginException(String msg) {
        super(msg);
    }
}