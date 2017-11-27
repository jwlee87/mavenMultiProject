package study.mmp.common.configuration;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.ObjectArrays;


/**
 * Message
 */
@Configuration
@Slf4j
public class MessageConfig {

    private static final int PROPERTIES_REFRESH_SECONDS = 60;
    private static final String PROPERTIES_SEPERATOR = "_";
    

    /**
     * ReloadableResourceBundleMessageSource Bean
     */
    @Bean
    public ReloadableResourceBundleMessageSource messageSource() throws IOException {

        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        
        
        String[] tmpList = FluentIterable.
                from(Lists.newArrayList(patternResolver.getResources("classpath*:messages/**/*.xml"))).
                filter((Resource resource) -> { 
                        return !Objects.isNull(resource); 
                    }).
                transform((Resource resource) -> {
                		try {
							String[] pathName = resource.getURI().toString().split("/messages/");
                          	String p = "classpath:/messages/" + pathName[1];
                          	if (p.contains("_ko") || p.contains("_en")) {
                                String[] tmp = p.split(PROPERTIES_SEPERATOR);
                                String[] tmpUnits = ObjectArrays.newArray(tmp, tmp.length - 2);
                                System.arraycopy(tmp, 0, tmpUnits, 0, tmpUnits.length);
                                return Joiner.on(PROPERTIES_SEPERATOR).skipNulls().join(tmpUnits);
                          	} else {
                          		return p;
                          	}

						} catch (Exception e) {
							e.printStackTrace();
						}
                		return null;
                    }).
                toArray(String.class);
        
        log.info("message file={}", Arrays.toString(tmpList));

        messageSource.setBasenames(tmpList);
        messageSource.setDefaultEncoding("utf-8");
        messageSource.setCacheSeconds(PROPERTIES_REFRESH_SECONDS);

        return messageSource;
    }
    
    /**
     * MessageSourceAccessor Bean
     */
    @Bean
    public MessageSourceAccessor messageSourceAccessor() throws IOException {
        return new MessageSourceAccessor(messageSource());
    }
}
