package study.mmp.web.configuration.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;



import study.mmp.web.annotation.CompanyCd;

import javax.servlet.http.HttpServletRequest;

public class CompanyCdArgumentResolver implements HandlerMethodArgumentResolver {
 
    @Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterAnnotation(CompanyCd.class) != null;
    }
 
    @Override
    public Object resolveArgument(MethodParameter parameter,
                       ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			           WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

		return request.getAttribute("companyCd");
	}
 
}