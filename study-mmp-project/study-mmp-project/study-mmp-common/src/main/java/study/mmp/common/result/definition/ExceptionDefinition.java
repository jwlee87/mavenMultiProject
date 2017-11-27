package study.mmp.common.result.definition;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class ExceptionDefinition {
	
	@Setter @Getter private Class<? extends Exception> exceptionClz;
	@Setter @Getter private int statusCode;
    
	@Setter @Getter private int code;
	@Setter @Getter private String message;
	@Setter @Getter private boolean logging;
	@Setter @Getter private Map<String, String> clientMessageMap;
	
}
