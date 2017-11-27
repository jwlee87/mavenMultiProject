package study.mmp.common.web.configuration.support;

import java.util.Arrays;

import javax.servlet.FilterRegistration;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import lombok.extern.slf4j.Slf4j;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import study.mmp.common.configuration.RootWebConfig;
import study.mmp.common.configuration.WebAppConfig;
import study.mmp.common.web.filter.ReloadWritableHttpServletRequestWrapperFilter;

/**
 * 
 *
 */
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public abstract class DefaultConfigStrategies extends AbstractAnnotationConfigDispatcherServletInitializer {

	
	@Override
	final protected Class<?>[] getRootConfigClasses() {
		Class<?>[] webRootConifg = {RootWebConfig.class};
		Class<?>[] rootConfig = rootContextConfigs();
		
		Class<?>[] returnConfig = null;
		if (!ObjectUtils.isEmpty(rootConfig)) {
			Class<?>[] newArray = Arrays.copyOf(webRootConifg, rootConfig.length + rootConfig.length);
			System.arraycopy(rootConfig, 0, newArray, webRootConifg.length, rootConfig.length);
			returnConfig = newArray;
		} else {
			returnConfig = webRootConifg;
		}
		log.info("root config-> {}", Arrays.toString(returnConfig));
		return returnConfig;
	}
	

	@Override
	final protected Class<?>[] getServletConfigClasses() {
		Class<?> servletConfig = configureServletConfig();
		Class<?>[] servletConfigs = new  Class<?>[2];
		servletConfigs[0] = servletConfig;
		servletConfigs[1] = WebAppConfig.class;
		log.info("servlet config-> {}", Arrays.toString(servletConfigs));
		return servletConfigs;
	}
	
	@Override
	protected String[] getServletMappings() {
		return new String[]{"/"};
	}
	
	@Override
	final public void onStartup(ServletContext servletContext) throws ServletException {
        log.info("server on Startup, {}", servletContext.getServletContextName());
        
        configureServletContext(servletContext);
		super.onStartup(servletContext);
	}
 

    private void configureServletContext(ServletContext servletContext) {
    	
    	//servletContext.addListener(ShutdownListener.class);
    	
    	FilterRegistration.Dynamic frdEncoding = servletContext.addFilter("encodingFilter", CharacterEncodingFilter.class);
    	frdEncoding.setInitParameter("encoding", "UTF-8");
    	frdEncoding.addMappingForUrlPatterns(null, true, "/*");
    	
    	FilterRegistration.Dynamic frdHttpMethod = servletContext.addFilter("reloadRequestFilter", ReloadWritableHttpServletRequestWrapperFilter.class); 
    	frdHttpMethod.addMappingForUrlPatterns(null, true, "/*");
     
    	servletContext.getSessionCookieConfig().setHttpOnly(true);

        customizeServletContext(servletContext);
    } 
 
    @Override
	final protected void customizeRegistration(Dynamic registration) {
    	registration.setInitParameter("throwExceptionIfNoHandlerFound", "true");
    	
    	/*TO-DO
    	HttpConstraintElement constraint = new HttpConstraintElement(EmptyRoleSemantic.DENY);
    	
    	
        List<HttpMethodConstraintElement> methodConstraints = new ArrayList<>();
        methodConstraints.add(new HttpMethodConstraintElement("GET"));
        methodConstraints.add(new HttpMethodConstraintElement("POST"));
        methodConstraints.add(new HttpMethodConstraintElement("OPTIONS"));
        
    	ServletSecurityElement sse = new ServletSecurityElement(constraint, methodConstraints);
    	registration.setServletSecurity(sse);
    	*/
    	
    	MultipartConfigElement mce = new MultipartConfigElement(null, 10485760L, 10485760L, 0);
    	registration.setMultipartConfig(mce);
    	customizeDispatcherSevlet(registration);
	}


    
    
    
    /**
     * Servlet 설정
     */
    abstract protected Class<? extends WebMvcConfigurerAdapter> configureServletConfig();
    
    
    /**
     * Root ApplicationContext에 추가할 config
     */
    protected Class<?>[] rootContextConfigs() {
    	return null;
    }
    
    
    /**
     * ServletContext(Web.xml)에 커스터마이징이 필요할 경우 오버라이드 하세요
     */
    protected void customizeServletContext(ServletContext servletContext) { }
    
    /**
     * dispatcher servlet 확장 시 사용
     */
    protected void customizeDispatcherSevlet(Dynamic registration) { }

}
