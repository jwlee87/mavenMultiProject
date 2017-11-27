package study.mmp.common.component.http;


/**
 * API 프로토콜 예외
 */
@SuppressWarnings("serial")
public class ApiProtocolException extends RuntimeException {
    
    private ApiRequestContext requestContext;

    
    public ApiProtocolException(String msg, ApiRequestContext context) {
        super("message=" + msg + ", API request info=" + context.requestToString());
        this.requestContext = context;
    }
    
    public ApiProtocolException(Throwable cause, ApiRequestContext context) {
        super("message=" + cause.getMessage() + ", API request info=" + context.requestToString(), cause);
        this.requestContext = context;
    }
    
    public ApiProtocolException(String msg, Throwable cause) {
        super("message=" + msg, cause);
    }
    
    public ApiProtocolException(String msg, Throwable cause, ApiRequestContext context) {
        super("message=" + msg + ", API request info=" + context.requestToString(), cause);
        this.requestContext = context;
    }

    public ApiRequestContext getHelperRequestContext() {
        return this.requestContext;
    }
}
