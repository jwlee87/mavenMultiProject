package study.mmp.web.configuration.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import study.mmp.web.annotation.NoAuth;


/**
 * No 로그인 전역 인터셉터
 */
@Component
@Slf4j
public class WebInterceptor extends HandlerInterceptorAdapter {

    private final Logger accessLogger = LoggerFactory.getLogger("accessLogger");

    @Autowired
    private Environment env;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        setAccessLog(request);
    	
    	log.debug(getDepthUrl(request, 1));
    	
        request.setAttribute("webUrl", env.getProperty("web.url"));
        NoAuth noAuth = ((HandlerMethod) handler).getMethodAnnotation(NoAuth.class);
        if (noAuth != null || request.getRequestURI().contains("error") || request.getRequestURI().contains("monitor")) {
            return true;
        }
        return true;
    }


    // 엑세스 로그 찍기
    private void setAccessLog(HttpServletRequest request) {
        if (!request.getRequestURI().contains("/monitor/")) {
            accessLogger.info("clientIp = {} / uri = {} / idNo = {} / userAgent = {}");
        }
    }


    private String getDepthUrl(HttpServletRequest request, int depth) {
        String uri = request.getRequestURI();
        String[] depthUrls = uri.split("/", 5);
        if (depthUrls.length > depth) {
        	log.debug("{} Depth Url={} -> {}", new Object[]{depth, uri, depthUrls[depth]});
            return depthUrls[depth];
        } else {
        	log.debug("{} Depth Url={} -> Empty", depth, uri);
            return StringUtils.EMPTY;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }
    
}
