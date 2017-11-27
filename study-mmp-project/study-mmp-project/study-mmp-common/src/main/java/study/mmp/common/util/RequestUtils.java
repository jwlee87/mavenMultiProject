package study.mmp.common.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import study.mmp.common.constant.AppOsType;
import study.mmp.common.web.filter.ReloadWritableHttpServletRequestWrapperFilter;

import com.google.common.collect.Maps;

/**
 * TO-DO
 *
 */
@Slf4j
public class RequestUtils {
	
	public static HttpServletRequest getRequest() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (sra == null) {
            return null;
        }
        HttpServletRequest request = sra.getRequest();
        if (request == null) {
        	return null;
        }
        return request;
	}
	
	/**
	 * Cant get Request in RequestContextHolder throw NullPointerException
	 * @return
	 */
    public static Map<String, Object> toMap() {
        return toMap(null);
    }
    
	public static Map<String, Object> toMap(HttpServletRequest request) {
        if (request == null) {
        	request = getRequest();
        }
        if (request == null) {
            throw new NullPointerException("Request is null or Can't found HttpRequest at RequestContextHolder");
        }
        
        Map<String, Object> map = Maps.newHashMap();
        map.put("addr", request.getRemoteAddr());
        String existingHeader = request.getHeader("X-Forwarded-For");
        if (!StringUtils.isEmpty(existingHeader)) {
            map.put("X-Forwarded-For", existingHeader);
        }
        map.put("uri", request.getRequestURI());
        map.put("method", request.getMethod());
        map.put("referer", request.getHeader("Referer"));
        map.put("param", getRequestParameter(request));
        map.put("content-type", request.getContentType());
        return map;
	}

    
	public static Object getRequestParameter(HttpServletRequest request) {
		return getRequestParameter(request, false);
	}
	
	/**
	 * 
	 * @return 맵으로 변환 가능하면 Map<String, Object> or String
	 */
    public static Object getRequestParameter(HttpServletRequest request, boolean forceCheckContentType) {

        if (isApplicationJson(request) || isMultipartFormData(request)) {
            String encodingSet = getEncodingSetFromRequest(request);
            return getParameterMapFromInputSteam(request, encodingSet);
            
        } else if (isApplicationWwwForm(request) || StringUtils.equalsIgnoreCase(request.getMethod(), "GET")) {
        	return getParameterMapForm(request);
        	
        } else {
        	if (forceCheckContentType) {
        		return "can't read request body, content-type=" + request.getContentType();
        	} else {
        		throw new HttpMessageNotReadableException("can't read request body, content-type=" + request.getContentType());
        	}
        }
    }
    

    private static Object getParameterMapFromInputSteam(HttpServletRequest request, String encodingSet) {
    	
        String val = (String) request.getAttribute(ReloadWritableHttpServletRequestWrapperFilter.IS_RELOAD_WRAPPER_FILTER);
        if (!"TRUE".equals(val)) {
            log.warn("Content-type json - ReloadHttpRequestWrapperFilter 적용 필요");
            return "";
        }
        
    	String jsonStr = "";
        try { 
            byte[] tmp = FileCopyUtils.copyToByteArray(request.getInputStream());
            jsonStr = new String(tmp, encodingSet);
            // * TO-DO  json array로 변환해야 하나
            Map<String, Object> map = JacksonUtil.toMap(jsonStr);
            return map;
        } catch (IOException ioe) {
            throw new HttpMessageNotReadableException("can't read request body", ioe);
        } catch (Exception e) {
        	/* Map으로 변환이 안되는 상황에서는 그냥 string으로 리턴*/
			return jsonStr;
		}
    }
    
    private static Map<String, Object> getParameterMapForm(HttpServletRequest request) {
    	Map<String, Object> paramMap = Maps.newHashMap();
        for (Entry<String, String[]> param :  request.getParameterMap().entrySet()) {
            
            String paramValue = Arrays.toString(param.getValue());
            paramValue = paramValue.substring(1, paramValue.length() - 1); //[ ] 제거
            paramMap.put(param.getKey(), paramValue);
        }
        
        return paramMap;
    }
    
    
    public static String getEncodingSetFromRequest(HttpServletRequest request) {
    	String conentType = request.getContentType();
    	String encodingSet = StringUtils.containsIgnoreCase(conentType, "euc-kr") ? "euc-kr" : "UTF-8";
        return encodingSet;
    }
    
    public static boolean isApplicationJson(HttpServletRequest request) {
        String conentType = request.getContentType();
        return (conentType != null && StringUtils.containsIgnoreCase(conentType, "json"));
    }
    
    public static boolean isApplicationWwwForm(HttpServletRequest request) {
        String conentType = request.getContentType();
        return (conentType != null && StringUtils.containsIgnoreCase(conentType, "www-form"));
    }
    
    public static boolean isMultipartFormData(HttpServletRequest request) {
        String conentType = request.getContentType();
        return (conentType != null && StringUtils.containsIgnoreCase(conentType, "multipart"));
    }
    
    /**
     * 
     * @return String IOS or AOS 
     */
    public static AppOsType getOSInfo(String userAgent) {
        userAgent = userAgent.toLowerCase();
        if (StringUtils.indexOfIgnoreCase(userAgent, "iphone") > -1 || StringUtils.indexOfIgnoreCase(userAgent, "ipad") > -1 || StringUtils.indexOfIgnoreCase(userAgent, "ipod") > -1) {
            return AppOsType.IOS;
        } else if (StringUtils.indexOfIgnoreCase(userAgent, "android") > -1) {
        	 return AppOsType.AOS;
        } else {
            log.debug("os 정보 확인 필요 userAgent={}", userAgent);
            return null;
        }
    }
    
    /**
	 * idNo 반환
	 * 
	 * @param request
	 * @return
	 */
	public static String getIdNo(HttpServletRequest request) {
		return (String)request.getAttribute("idNo");
	}
	
	public static String getUserName(HttpServletRequest request) {
		return (String)request.getAttribute("name");
	}
	
	public static Object getAttribute(String name) {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		if (!Objects.isNull(attr)) {
			//extract the request
			HttpServletRequest request = attr.getRequest();
			return  request.getAttribute(name);
		}
		return null;
	}
}
