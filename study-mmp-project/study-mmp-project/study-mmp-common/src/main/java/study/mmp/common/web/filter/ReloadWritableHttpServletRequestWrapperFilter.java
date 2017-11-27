package study.mmp.common.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import study.mmp.common.util.RequestUtils;

public class ReloadWritableHttpServletRequestWrapperFilter  implements Filter {
    
	public final static String IS_RELOAD_WRAPPER_FILTER = "IS_RELOAD_WRAPPER_FILTER";
	   
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        
        if (RequestUtils.isApplicationJson(httpServletRequest) || RequestUtils.isApplicationWwwForm(httpServletRequest)) {
            httpServletRequest.setAttribute(IS_RELOAD_WRAPPER_FILTER, "TRUE");
            
        	ReloadWritableHttpServletRequestWrapper requestWrapper = new ReloadWritableHttpServletRequestWrapper(httpServletRequest);
            chain.doFilter(requestWrapper, response);
        } else {
            chain.doFilter(request, response);
        }
    }
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        
    }
    
    @Override
    public void destroy() {
        
    }
}