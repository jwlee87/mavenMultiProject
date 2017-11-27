package study.mmp.web.configuration;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Configuration
//@ImportResource(value = "classpath:project-url.xml")
@Slf4j
@ComponentScan(basePackages = {"study.mmp.web.**.dao", "study.mmp.web.**.service", "study.mmp.web.common.definition"},
        includeFilters = @ComponentScan.Filter(value = {Service.class, Repository.class}),
        useDefaultFilters = false)
public class RootConfig {
	
	@Bean
	public String projectName() {
		log.info("SampleWebRootConfig 호출");
		return "멤버십 웹 샘플 - 이거는 테스트죠";
	}
}
