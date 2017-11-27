package study.mmp.common.component;

import java.util.*;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import study.mmp.common.util.RequestUtils;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 *
 */
public class RequestCyper {

	@Getter @Setter private Set<String> encrytTargetParameterSet;
	
    public Map<String, Object> encryptedMap() {
        return encrypt(RequestUtils.toMap());
    }

    public Map<String, Object> encryptedMap(HttpServletRequest request) {
        return encrypt(RequestUtils.toMap(request));
    }
    
    @SuppressWarnings("unchecked")
	private Map<String, Object> encrypt(Map<String, Object> requestInfo) {

    	Object requestParameter = requestInfo.get("param");
		if (requestParameter instanceof Map) {
			Map<String, Object> t = (Map<String, Object>) requestParameter;
			requestInfo.put("param", encrytParameter(t));
		}
    	return requestInfo;
    }
    
    
    @SuppressWarnings("unchecked")
	public Object encryptedParamterMap(HttpServletRequest request) {
    	Object requestParameter = RequestUtils.getRequestParameter(request);
    	if (requestParameter instanceof Map) {
			Map<String, Object> t = (Map<String, Object>) requestParameter;
    		return encrytParameter(t);
    	}
        return requestParameter;
    }
    

    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private Map<String, Object> encrytParameter(Map<String, Object> plainTextMap) {
    	if (encrytTargetParameterSet == null || encrytTargetParameterSet.size() == 0) {
    		return plainTextMap;
    	}
    	for (Entry<String, Object> param :  plainTextMap.entrySet()) {
    		if (param.getValue() instanceof String) {
        		for (String targetFiled : encrytTargetParameterSet) {
        			if (targetFiled.contains(param.getKey())) {
        				/*포함되어 있을 경우만*/
        				String enc = null; // 암호화 모듈
            			plainTextMap.put(param.getKey(), enc);
            			break;
        			}
        		}
    		} else if (param.getValue() instanceof Map) {
                Map<String, Object> value = (Map) param.getValue();
                this.encrytParameter(value);
            }
        }
    	return plainTextMap;
    }
}
