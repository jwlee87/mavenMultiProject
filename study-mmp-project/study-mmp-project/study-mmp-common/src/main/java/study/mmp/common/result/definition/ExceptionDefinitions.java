package study.mmp.common.result.definition;

import static javax.servlet.http.HttpServletResponse.*;

import java.util.HashMap;
import java.util.Map;


public class ExceptionDefinitions {
	
	private static Map<Class<? extends Exception>, ExceptionDefinition> errors = new HashMap<>();

	
	private final static ExceptionDefinition DEFAULT_ERROR = new ExceptionDefinition(Exception.class, SC_INTERNAL_SERVER_ERROR, 500, "Unknown Server Error", true, null);

	public static void addExceptionDefinition(Class<? extends Exception> exceptionClz, int statusCode, int code, String message, boolean logging, Map<String, String> clientMessageMap) {
		errors.put(exceptionClz, new ExceptionDefinition(exceptionClz, statusCode, code, message, true, null));
		
	}

	
	public static ExceptionDefinition getExceptionDefinition(Exception ex) {

		return errors.getOrDefault(ex.getClass(), DEFAULT_ERROR);
	}

}
