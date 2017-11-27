package study.mmp.common.component.http;

/**
 * API 응답코드 예외
 *
 */
@SuppressWarnings("serial")
public class ApiResultException extends RuntimeException {
    
    private String resultCode;
    private String resultMessage;
    private ApiRequestContext requestContext;
    
    public ApiResultException(String resultCode, String resultMessage, ApiRequestContext reqContext) {
        
        super("Invalid api result '" + resultCode + "': " + resultMessage + ", API request info=" + reqContext.requestToString());
        
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
    }

    public ApiResultException(String resultCode, String resultMessage) {

        super("Invalid api result '" + resultCode + "': " + resultMessage);

        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
    }
    
    public String getResultCode() {
        return resultCode;
    }
    
    public String getResultMessage() {
        return resultMessage;
    }
    
    public ApiRequestContext getHelperRequestContext() {
        return this.requestContext;
    }
}
