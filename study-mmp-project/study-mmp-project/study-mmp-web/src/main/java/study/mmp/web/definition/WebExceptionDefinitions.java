package study.mmp.web.definition;

import org.springframework.stereotype.Component;

import study.mmp.common.result.definition.ExceptionDefinitions;
import study.mmp.web.exception.*;
import static javax.servlet.http.HttpServletResponse.SC_OK;

@Component
public class WebExceptionDefinitions {

	static {

		ExceptionDefinitions.addExceptionDefinition(NoAuthException.class, SC_OK, 2016, "잔여 포인트가 부족합니다.", true, null);		
	}
}
