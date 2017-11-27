package study.mmp.common.result;

import java.util.Map;

/**
 * 응답에 대한 최상위 인터페이스 
 * 
 * code, message
 */
public interface JsonResult {
	
    public static final int SUCCESS = 0;
    
    public enum Type {
    	SUCCESS,
    	FAIL,
    	ERROR
    }
    
    public int getCode();
    
    public String getMessage();
    
    public Type getResultType();
    
    public int getStatusCode();
    
    public interface Fail extends JsonResult {
    	
    	public Map<String, Object> getInfo();
    	
    	public JsonResult.Fail addInfo(String fieldName, Object value);
    }
    
    public interface Error extends Fail {
    	
    	public Throwable getException(); 
    }
}
