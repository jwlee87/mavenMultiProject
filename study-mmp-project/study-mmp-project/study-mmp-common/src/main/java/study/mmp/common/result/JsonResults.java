package study.mmp.common.result;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import study.mmp.common.exception.define.HasClientMessage;
import study.mmp.common.exception.define.HasCode;
import study.mmp.common.exception.define.HasIsLogging;
import study.mmp.common.result.definition.ExceptionDefinition;
import study.mmp.common.result.definition.ExceptionDefinitions;
import study.mmp.common.util.LogUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;

import static javax.servlet.http.HttpServletResponse.*;

@Slf4j
public class JsonResults {
	
    public final static String REQUEST_ATTR_RESULT = "_REQUEST_ATTR_RESULT_";
    public final static String DEFAULT_ERROR_CLIENT_MESSAGE = "오류가 발생하였습니다. 잠시 후 다시 시도해 주세요.";
    
    /**
     * 성공 응답 생성
     */
    public static JsonResult success() {
        addResultToReuestAttr(SC_OK, SIMPLE_SUCCESS);
        return SIMPLE_SUCCESS;
    }
    
    /**
     * result 필드에 Object result값 설정된 성공 응답생성
     */
    public static JsonResult success(Object result) {
    	SuccessJsonResult successResult = new SuccessJsonResult();
        successResult.setResult(result);
        addResultToReuestAttr(SC_OK, successResult);
        return successResult;
    }
    
    /**
     * 실패 응답 생성
     */
    public static JsonResult.Fail fail(int code, String message) {
        return fail(code, message, false);
    }
    
    /**
     * 실패 응답 생성
     */
    public static JsonResult.Fail fail(String code, String message) {
        return fail(Integer.parseInt(code), message, false);
    }
    
    /**
     * 실패 응답 생성
     */
    public static JsonResult.Fail fail(int code, String message, boolean isLogging) {
    	 return fail(code, message, false, 200);
    }
    
    /**
     * 실패 응답 생성
     */
    public static JsonResult.Fail fail(int code, String message, boolean isLogging, int statusCode) {

    	FailJsonResult result = new FailJsonResult(code, message);
        
        if (isLogging) {
            log.error(LogUtils.format("실패 응답 발생", result, true));
        }
        
        result.setStatusCode(statusCode);
        
        addResultToReuestAttr(200, result);
        return result;
    }
    
    /**
     * 직접 호출하지 마세요.
     */
    public static JsonResult.Error handleException(Exception exception, boolean isHtmlResponse, Object...arg) {

        ExceptionDefinition exceptionDefinition = ExceptionDefinitions.getExceptionDefinition(exception);
        String message = null;
        if (!StringUtils.isEmpty(exceptionDefinition.getMessage())) {
            FormattingTuple ft = MessageFormatter.arrayFormat(exceptionDefinition.getMessage(), arg);
            message = ft.getMessage();
        } else {
            message = exception.getMessage();
        }
        
        ErrorJsonResult result = new ErrorJsonResult(exceptionDefinition.getCode(), message, exceptionDefinition.getStatusCode(), exception);
        
        if (exception instanceof HasCode) {
            int code = ((HasCode) exception).getCode();
            try {
                result.setCode(code);
            } catch (NumberFormatException nfe) {
            	log.error("에러 code 재정의 필요. 확인해 주세요 code={}", code);
                result.setCode(500);
            }
        }
        
        if (!isHtmlResponse) {
            Map<String, Object> info = Maps.newHashMap();
            if (exception instanceof HasClientMessage) {
                String clientMessage = ((HasClientMessage) exception).getClientMessage();
                info.put("clientMessage", clientMessage);
            } else {
                if (exceptionDefinition.getClientMessageMap() == null) {
                    info.put("clientMessage", DEFAULT_ERROR_CLIENT_MESSAGE);
                } else {
                    info.put("clientMessage", exceptionDefinition.getClientMessageMap());
                }
            }
            result.setInfo(info);
        }
        
        addResultToReuestAttr(exceptionDefinition.getStatusCode(), result);
        
        if (exception instanceof HasIsLogging) {
            boolean isLogging = ((HasIsLogging) exception).isLogging();
            if (isLogging) {
                log.error(LogUtils.format(message, isHtmlResponse ? null : result, true), exception);
            }
        } else {
            if (exceptionDefinition.isLogging()) {
            	log.error(LogUtils.format(message, isHtmlResponse ? null : result, true), exception);
            }
        }
        return result;
    }
    
    
    @SuppressWarnings("unchecked")
    public static <T> T getResult(JsonResult result) {
        if (result instanceof SuccessJsonResult) {
        	SuccessJsonResult successResult = (SuccessJsonResult) result;
            if (successResult.getResult() == null) {
                return null;
            }
            T resultObject = (T) successResult.getResult();
            return resultObject;
        } else {
            return null;
        }
    }
    
    /**
     * 1. response status code값 설정
     * 2. request에 attribute에 응답(result)값 설정
     *   - ExceptionHandler와 Controller가 @ResponseBody에 result오브젝트를 direct로 write하기 때문에 
     *     ExceptionHander나 Controller이후 단계에서 Result값에 접근이 불가능하기 때문에 request에 result 넣음
     */
    private static void addResultToReuestAttr(int responseStatus, JsonResult result) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.setStatus(responseStatus);
        
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        request.setAttribute(REQUEST_ATTR_RESULT, result);
    }
    
    
    private final static JsonResult SIMPLE_SUCCESS = new JsonResult() {
        
        @Override
        public int getCode() {
            return 0;
        }
        
        @Override
        public String getMessage() {
            return "SUCCESS";
        }
        
        @Override @JsonIgnore
        public Type getResultType() {
        	return JsonResult.Type.SUCCESS;
        }
        
        @Override
        @JsonIgnore
        public int getStatusCode() {
        	return 0;
        }
    };
}
