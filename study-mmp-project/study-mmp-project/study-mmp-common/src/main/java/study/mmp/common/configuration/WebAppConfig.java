package study.mmp.common.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * Servlet Application 영역의 Bean 설정
 */
@Configuration
@Slf4j
@ComponentScan(basePackages = {"study.mmp.common.web.controller"}, 
includeFilters = @Filter(value = {Controller.class, ControllerAdvice.class}), 
useDefaultFilters = false)
public class WebAppConfig implements InitializingBean {
	
	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("common controller set");
	}
}
