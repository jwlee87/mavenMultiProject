package study.mmp.common.web.controller.advise;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import study.mmp.common.constant.Constants;
import study.mmp.common.result.JsonResult;
import study.mmp.common.result.JsonResults;
import study.mmp.common.util.JacksonUtil;
import study.mmp.common.web.controller.ErrorController;

/**
 * Global - 예외 처리
 * 
 * Default :: response status = 500
 * 
 * Cause 에 Exception 정의하지 않으면 기본으로 result Code = 500
 *
 */
@ControllerAdvice
@Order(value = Ordered.LOWEST_PRECEDENCE - 100)
public class CommonExceptionHandler {

    //@Autowired MessageSourceAccessor messageSourceAccessor;
    
	@Autowired ErrorController errorController;
	
    @ExceptionHandler
    @ResponseBody
    public Object handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpServletRequest request, HttpServletResponse response) {
        List<MediaType> mediaTypes = ex.getSupportedMediaTypes();
        if (!CollectionUtils.isEmpty(mediaTypes)) {
            response.setHeader("Accept", MediaType.toString(mediaTypes));
        }
        return handleView(ex, request);
    }
    
    @ExceptionHandler
    public void handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpServletResponse response) throws Exception {

    	response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JacksonUtil.toJson(JsonResults.handleException(ex, false)));
    }
    
    @ExceptionHandler
    @ResponseBody
    public JsonResult handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
    	JsonResult.Error result = JsonResults.handleException(ex, isHtmlResponse(request));
    	result.addInfo("errorMessage", ex.getBindingResult().getFieldError().getDefaultMessage());
        result.addInfo("errorField", ex.getBindingResult().getFieldError().getField());
        result.addInfo("inputValue", ex.getBindingResult().getFieldError().getRejectedValue());
        return result;
    }
    
    @ExceptionHandler
    @ResponseBody
    public JsonResult handleBindException(BindException ex, HttpServletRequest request) {
    	JsonResult.Error result = JsonResults.handleException(ex, isHtmlResponse(request));
        result.addInfo("errorMessage", ex.getBindingResult().getFieldError().getDefaultMessage());
        result.addInfo("errorField", ex.getBindingResult().getFieldError().getField());
        result.addInfo("inputValue", ex.getBindingResult().getFieldError().getRejectedValue());
        return result;
    }

    @ExceptionHandler
    @ResponseBody
    public JsonResult handleBindException(MissingServletRequestParameterException ex, HttpServletRequest request) {
    	JsonResult.Error result = JsonResults.handleException(ex, isHtmlResponse(request));
        return result;
    }
    
    
	@ExceptionHandler
    @ResponseBody
	@Order(value = Ordered.LOWEST_PRECEDENCE)
    public Object handleException(Exception ex, HttpServletRequest request) {

        return handleView(ex, request);
    }


	private Object handleView(Exception ex, HttpServletRequest request) {

		boolean htmlResponse = isHtmlResponse(request);
        JsonResult response = JsonResults.handleException(ex, htmlResponse);
        if (htmlResponse) {

            String viewName = errorController.viewNameByStatusCode(response.getStatusCode());
            ModelAndView view = new ModelAndView(viewName);
            view.addObject("code", response.getCode());
            return view;
        } else {
            return response;
        }
	}
	
	private boolean isHtmlResponse(HttpServletRequest request) {
		return StringUtils.endsWithIgnoreCase(request.getRequestURI(), "." + Constants.URL_HTML_EXTEND);
	}
}
