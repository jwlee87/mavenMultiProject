package study.mmp.common.configuration;

import java.io.IOException;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.StringUtils;

import study.mmp.common.util.RunEnvironment;

@Configuration
@Slf4j
//@PropertySource("classpath:properties/common.api.properties")
//@PropertySource("classpath:properties/main.jdbc.properties")
public class PropertiesEnvConfig implements InitializingBean {

	@Autowired private ConfigurableEnvironment env;

	@Override
	public void afterPropertiesSet() throws Exception {
		initCommonProperty();
	 	log.info("initialize PropertySource to Environment");
	}
	
	static {
		setRunEnvironment();
	}
	
	private static void setRunEnvironment() {
		try {
			for (Resource resource : PropertiesConfig.getProjectResources()) {
			    Properties properties = PropertiesLoaderUtils.loadProperties(resource);
			    if (!StringUtils.isEmpty(properties.getProperty("run.phase"))) {
				 	log.info("RunEnvironment set phase = {}", properties.getProperty("run.phase"));
			    	RunEnvironment.setPhase(properties.getProperty("run.phase").toUpperCase());
			    }
			    
			    if (!StringUtils.isEmpty(properties.getProperty("domain"))) {
			    	log.info("RunEnvironment set domain = {}", properties.getProperty("domain"));
			        RunEnvironment.setDomain(properties.getProperty("domain"));
			    }
			    
			    if (!StringUtils.isEmpty(properties.getProperty("protocol"))) {
			    	log.info("RunEnvironment set protocol = {}", properties.getProperty("protocol"));
			    	RunEnvironment.setHttps(Boolean.valueOf(properties.getProperty("protocol")));
			    }
			}
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Property Environment에 주입
	 */
	private void initCommonProperty() throws IOException {
		
	    MutablePropertySources mpss = env.getPropertySources();
	 	for (Resource resource : PropertiesConfig.getProjectResources()) {
		    Properties properties = PropertiesLoaderUtils.loadProperties(resource);
	 		//log.info("Adding [{}] PropertySource with highest search precedence", resource.getFilename());
		    mpss.addFirst(new PropertiesPropertySource(getNameForResource(resource), properties));
	 	}
	}

	
	private static String getNameForResource(Resource resource) {
		String name = resource.getFilename();
		if (!StringUtils.hasText(name)) {
			name = resource.getClass().getSimpleName() + "@" + System.identityHashCode(resource);
		}
		return name;
	}
}
