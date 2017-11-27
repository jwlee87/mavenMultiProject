package study.mmp.common.configuration;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import study.mmp.common.component.http.ApiHelper;
import study.mmp.common.util.RunEnvironment;


@Configuration
@Slf4j
@Import({PropertiesConfig.class, PropertiesEnvConfig.class, MailSenderConfig.class})
@ComponentScan(basePackages = {"study.mmp.common.component"}, 
	includeFilters = @Filter(value = {Component.class}), 
	useDefaultFilters = false
)
public class RootCommonConfig {

	@Autowired PropertiesEnvConfig check;
	
    @Bean(destroyMethod = "close")
    public ApiHelper apiHelper(Environment env) {
    	
    	boolean isDev = RunEnvironment.getPhase().isDev();
    	log.debug("테스트 Env " + isDev);
        return new ApiHelper();
    }
}
