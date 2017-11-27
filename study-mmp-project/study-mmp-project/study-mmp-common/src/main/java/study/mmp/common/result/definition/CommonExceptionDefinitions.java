package study.mmp.common.result.definition;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_NOT_ACCEPTABLE;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import study.mmp.common.exception.system.LeastOneRequiredParameterException;
import study.mmp.common.exception.system.UnapprovedRemoteAccessException;

@Component
public class CommonExceptionDefinitions {

	static {
		ExceptionDefinitions.addExceptionDefinition(MissingServletRequestParameterException.class, SC_BAD_REQUEST, 400, "{}", false, null);
		ExceptionDefinitions.addExceptionDefinition(ServletRequestBindingException.class, SC_BAD_REQUEST, 400, null, false, null);
		ExceptionDefinitions.addExceptionDefinition(TypeMismatchException.class, SC_BAD_REQUEST, 400, null, true, null);
		ExceptionDefinitions.addExceptionDefinition(HttpMessageNotReadableException.class, SC_BAD_REQUEST, 400, null, false, null);
		ExceptionDefinitions.addExceptionDefinition(MissingServletRequestPartException.class, SC_BAD_REQUEST, 400, null, true, null);
		ExceptionDefinitions.addExceptionDefinition(MethodArgumentNotValidException.class, SC_BAD_REQUEST, 400, "Not Valid MethodArgument", false, null);
		ExceptionDefinitions.addExceptionDefinition(LeastOneRequiredParameterException.class, SC_BAD_REQUEST, 400, null, false, null);
		ExceptionDefinitions.addExceptionDefinition(BindException.class, SC_BAD_REQUEST, 400, "Parameter Binding Error", false, null);
		ExceptionDefinitions.addExceptionDefinition(NoHandlerFoundException.class, SC_NOT_FOUND, 404, null, true, null);
		ExceptionDefinitions.addExceptionDefinition(HttpRequestMethodNotSupportedException.class, SC_NOT_FOUND, 404, null, true, null);
		ExceptionDefinitions.addExceptionDefinition(HttpMediaTypeNotAcceptableException.class, SC_NOT_ACCEPTABLE, 406, null, false, null);
		ExceptionDefinitions.addExceptionDefinition(HttpMediaTypeNotSupportedException.class, SC_UNSUPPORTED_MEDIA_TYPE, 415, null, true, null);
		ExceptionDefinitions.addExceptionDefinition(ConversionNotSupportedException.class, SC_INTERNAL_SERVER_ERROR, 500, null, true, null);
		ExceptionDefinitions.addExceptionDefinition(HttpMessageNotWritableException.class, SC_INTERNAL_SERVER_ERROR, 500, null, true, null);
		
		ExceptionDefinitions.addExceptionDefinition(UnapprovedRemoteAccessException.class, SC_FORBIDDEN, 1002, "허용되지 않은 접근입니다.", false, null);
	}
}
