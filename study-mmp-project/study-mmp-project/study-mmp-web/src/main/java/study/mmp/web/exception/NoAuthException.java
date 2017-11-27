package study.mmp.web.exception;

@SuppressWarnings("serial")
public class NoAuthException extends RuntimeException {

    public NoAuthException(String msg) {
        super(msg);
    }
}