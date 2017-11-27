package study.mmp.common.configuration;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.stereotype.Component;

import study.mmp.common.component.RequestCyper;
import study.mmp.common.configuration.MainDbConfig;
import study.mmp.common.util.LogUtils;

import com.google.common.collect.Sets;

@Configuration
@Slf4j
@Import({RootCommonConfig.class, MainDbConfig.class, MessageConfig.class, CacheConfig.class})
@ComponentScan(basePackages = {"study.mmp.common.result.definition"}, 
includeFilters = @Filter(value = {Component.class}), 
useDefaultFilters = false
)
public class RootWebConfig implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("WebRootConfig Initializing....");
	}

    @Bean
    public RequestCyper requestCyper() {
    	RequestCyper requestCyper = new RequestCyper();
    	requestCyper.setEncrytTargetParameterSet(Sets.newHashSet("phone", "email", "userid", "uicc", "card"));
    	
    	LogUtils.setRequestCyper(requestCyper);
    	return requestCyper;
    }
}
