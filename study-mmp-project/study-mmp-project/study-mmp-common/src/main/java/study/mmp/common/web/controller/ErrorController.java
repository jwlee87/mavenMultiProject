package study.mmp.common.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import study.mmp.common.constant.Constants;
import study.mmp.common.result.JsonResults;
import study.mmp.common.util.JacksonUtil;


/**
 * Tomcat에서 처리하는 에러 처리
 *
 */
@Controller
public class ErrorController {
    
    public static final String ERROR_EXCEPTION = "javax.servlet.error.exception";
    public static final String ERROR_EXCEPTION_TYPE = "javax.servlet.error.exception_type";
    public static final String ERROR_MESSAGE = "javax.servlet.error.message";
    public static final String ERROR_REQUEST_URI = "javax.servlet.error.request_uri";
    public static final String ERROR_SERVLET_NAME = "javax.servlet.error.servlet_name";
    public static final String ERROR_STATUS_CODE = "javax.servlet.error.status_code";

	//@Autowired MessageSourceAccessor messageSourceAccessor;
	
    @Autowired Environment env;
    
	@RequestMapping(value = "/error." + Constants.URL_HTML_EXTEND, method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView errorDo(HttpServletRequest request) {
	    
    	ModelAndView view = new ModelAndView();
	    try {
	        Object code = request.getAttribute(ERROR_STATUS_CODE);
	        view.addObject("code", 500);
	        
	    	Object obj = request.getAttribute(ERROR_EXCEPTION);
			if (obj instanceof Exception) {
				Exception exception = (Exception) obj;
				view.addObject("exception", exception);
			}
			
			int statusCode = 500;
			if (code != null) {
				statusCode = (int) code;
			}
			
	        String viewName = viewNameByStatusCode(statusCode);
	        view.setViewName(viewName);
	        
	    } catch (Exception e) {
	    	e.printStackTrace();
	        view.setViewName(env.getProperty("500error"));
	    }
	    return view;
	}	
	
	@RequestMapping(value = "/error." + Constants.URL_JSON_EXTEND, method = {RequestMethod.GET, RequestMethod.POST})
	public void errorJson(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		
		int statusCode = 500;
		if (request.getAttribute(ERROR_STATUS_CODE) != null) {
			statusCode = (int) request.getAttribute(ERROR_STATUS_CODE);
		}
		String message = "[" + statusCode + "] 오류입니다.";
		if (request.getAttribute(ERROR_MESSAGE) instanceof String) {
			message = (String) request.getAttribute(ERROR_MESSAGE);
		}
		if (request.getAttribute(ERROR_EXCEPTION) instanceof Exception) {
			Exception exception = (Exception) request.getAttribute(ERROR_EXCEPTION);
	        response.getWriter().write(JacksonUtil.toJson(JsonResults.handleException(exception, false, "")));
		} else {
		    response.getWriter().write(JacksonUtil.toJson(JsonResults.fail(statusCode, message, false, 200)));
		}
	}
	
	
	@RequestMapping(value = "/error." + Constants.URL_ERROR_EXTEND, method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public Object error(HttpServletRequest request, HttpServletResponse response) throws IOException {

	    Object requestUriObj = request.getAttribute(ERROR_REQUEST_URI);
	    String requestUri = "";
	    if (requestUriObj instanceof String) {
	    	requestUri = (String) requestUriObj;
	    }
		if (StringUtils.endsWithIgnoreCase(requestUri, "." + Constants.URL_JSON_EXTEND)) {
			errorJson(request, response);
			return null;
		} else {
			return errorDo(request);
		}
	}
	
	
	
	
	
	/**
	 * properties에서 에러 페이지 가져옴
	 */
	public String viewNameByStatusCode(int statusCode) {
		
	    if (statusCode == 403) {
            return env.getProperty("403error");
        } else if (statusCode == 404 || statusCode == 405) {
            return env.getProperty("404error");
        } else if (statusCode == 503) {
            return env.getProperty("503error");
        } else {
            return env.getProperty("500error");
        }
	}
}
