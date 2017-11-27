package study.mmp.common.util;

import javax.servlet.http.HttpServletRequest;

import study.mmp.common.component.RequestCyper;
import study.mmp.common.result.JsonResult;
import study.mmp.common.util.JacksonUtil;
import lombok.Getter;
import lombok.Setter;


public class LogUtils {
	
	@Setter @Getter private static RequestCyper requestCyper;

    public static String format(String message) {
        return map(message, null, true).toString();
    }
    
    public static String format(String message, boolean includeHttpParamMap) {
        return map(message, null, includeHttpParamMap).toString();
    }
    
    public static String format(String message, JsonResult.Fail result, boolean includeHttpParamMap) {
        return map(message, result, includeHttpParamMap).toString();
    }
    
    /** 
     * @message 오류 내용. 로깅할 내용
     * 
       { 
                     에러 메세지=선물 정보 업데이트 실패,
                     요청정보={
                     refer=null, 
                     param=..... 
                     X-Forwarded-For=10.77.79.218, 
                     addr=10.24.108.110, 
                     uri=...
                     contentType=application/x-www-form-urlencoded; charset=UTF-8
                 }
                     응답정보={code=...message... result} 
       }
     * 
     */
    private static String map(String exceptionMsg, JsonResult result, boolean includeHttpParamMap) {
    	
    	StringBuilder sb = new StringBuilder();
    	if (exceptionMsg != null) {
    		sb.append("MESSAGE=").append(exceptionMsg);
        }
    	try {
	        if (includeHttpParamMap) {
	        	if (sb.length() > 0) {
	        		sb.append(",");
	        	}
	        	
	        	HttpServletRequest req = RequestUtils.getRequest();
	        	if (req != null) {
	
	            	if (requestCyper == null) {
	            		sb.append("REQUEST=").append(RequestUtils.toMap(req));
	            	} else {
	                	sb.append("REQUEST=").append(requestCyper.encryptedMap(req).toString());
	            	}
	        	} else {
	        		sb.append("REQUEST=No request!");
	        	}
	        } else {
	        	//????
	        }
    	} catch (Error err) {
    		//
    	}
        if (result != null) {
        	if (sb.length() > 0) {
        		sb.append(",");
        	}
        	sb.append("RESPONSE=").append(JacksonUtil.toJson(result));
        }
        return sb.toString();
    }
}
