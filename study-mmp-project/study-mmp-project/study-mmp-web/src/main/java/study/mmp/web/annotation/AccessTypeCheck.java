package study.mmp.web.annotation;

import java.lang.annotation.*;

import study.mmp.web.constant.AccessType;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessTypeCheck {
 
  /**
	 * The name of the request attribute.
	 */
	AccessType value();
 
}