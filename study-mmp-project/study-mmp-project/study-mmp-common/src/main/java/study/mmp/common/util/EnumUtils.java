package study.mmp.common.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * description 메소드는 만들어야 합니다.
 * 
 * TO-DO
 */
public class EnumUtils {

	public static <E extends Enum<E>> String getDescriptionByName(final Class<E> enumClass, final String enumName) {	
		return getDescriptionByName(enumClass, enumName, "undefined");
	}
	

	public static <E extends Enum<E>> String getDescriptionByName(final Class<E> enumClass, final String enumName, String defaultDesc) {	
		
		if (StringUtils.isEmpty(enumName)) {
			//여긴 에러
		}
		
		E enumObj = Enum.valueOf(enumClass, enumName);
		for (Method method : enumObj.getClass().getMethods()) {

			if (!method.isAccessible()) {
				method.setAccessible(true);
			}
			if (StringUtils.containsIgnoreCase(method.getName(), "getDesc")) {
				
				try {
					Object obj = method.invoke(enumObj);
					if (obj != null) {
						String desc = (String) obj;
						return StringUtils.isEmpty(desc) ? defaultDesc : desc;
					} else {
						return "메소드 실행후에도 없다?";
					}
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
					return "여긴 에러...";
				}
			}
		}
		return "설명 메소드 없다";
	}
	
	
	public static <E extends Enum<E>> List<String> getDescriptionList(final Class<E> enumClass) {	
		List<String> list = new ArrayList<>();
		
		for (E e : org.apache.commons.lang3.EnumUtils.getEnumList(enumClass)) {
			list.add(getDescriptionByName(enumClass, e.toString().toString()));
		}
		return list;
	}
	
	
	public static <E extends Enum<E>> E getNameByDescription(final Class<E> enumClass, final String description) {
		
		if (StringUtils.isEmpty(description)) {
			//여긴 에러
		}
		
		for (E e : org.apache.commons.lang3.EnumUtils.getEnumList(enumClass)) {
			String desc = getDescriptionByName(enumClass, e.toString().toString());
			if (description.equals(desc)) {
				return e;
			}
		}
		
		throw new RuntimeException("알수 없음");
	}
}
