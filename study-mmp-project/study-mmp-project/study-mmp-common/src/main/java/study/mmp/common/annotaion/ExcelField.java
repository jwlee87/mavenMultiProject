package study.mmp.common.annotaion;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelField {
	
	String title();
	int order();
	boolean isForce() default false;
}
