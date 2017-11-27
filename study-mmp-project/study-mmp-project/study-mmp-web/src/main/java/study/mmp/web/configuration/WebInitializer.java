package study.mmp.web.configuration;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import study.mmp.common.constant.Constants;
import study.mmp.common.web.configuration.support.DefaultConfigStrategies;
import study.mmp.common.web.configuration.support.PropertyLoader;

import com.navercorp.lucy.security.xss.servletfilter.XssEscapeServletFilter;
import com.opensymphony.sitemesh.webapp.SiteMeshFilter;
 
 
 
@Slf4j
public class WebInitializer extends DefaultConfigStrategies {

	@Override
	protected Class<? extends WebMvcConfigurerAdapter> configureServletConfig() {
		return WebMvcConfig.class;
	}
	
	@Override
	protected String[] getServletMappings() {
		return new String[]{"*." + Constants.URL_HTML_EXTEND, "*." + Constants.URL_JSON_EXTEND, "*." + Constants.URL_ERROR_EXTEND};
	}
	
	@Override
	protected Class<?>[] rootContextConfigs() {
		return new Class<?>[] {RootConfig.class};
	}
	
	
	@Override
	protected void customizeServletContext(ServletContext servletContext) {
		
		PropertyLoader propertyLoader = new PropertyLoader("properties/module.properties");
		
		FilterRegistration.Dynamic frdMesh	= servletContext.addFilter("sitemesh", SiteMeshFilter.class);
		frdMesh.setInitParameter("pageEncoding", "UTF-8");
		frdMesh.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), true, "/*");
		
		FilterRegistration.Dynamic frdXss = servletContext.addFilter("xssEscapeServletFilter", XssEscapeServletFilter.class);
		frdXss.addMappingForUrlPatterns(null, true, "/*");

		String url = propertyLoader.getProp("xxx.url");
		log.debug(url);

	}
}