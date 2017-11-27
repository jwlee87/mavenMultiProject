package study.mmp.web.support;

import java.io.IOException;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import study.mmp.common.util.EnumUtils;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;

/**
 * Enum SelectBox Taglib
 *
 */
public class EnumSelectBox extends SimpleTagSupport {
	
	@Getter @Setter private String enumPath;
	@Getter @Setter private String selectBoxId = "";
	@Getter @Setter private String selectBoxName = "";
	@Getter @Setter private String selectedValue;
	@Getter @Setter private String emptyValueName;
	@Getter @Setter private String style; 
	@Getter @Setter private String outputType; 
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@SuppressWarnings("unchecked")
	@Override
	public void doTag() throws JspException, IOException {
				
		Map<String, String> enumMap = Maps.newHashMap();
		
		try {

			@SuppressWarnings("rawtypes")
			Class<? extends Enum> clz = (Class<? extends Enum>) Class.forName(enumPath);
			
			for (Object targetEnum : org.apache.commons.lang3.EnumUtils.getEnumList(clz)) {
				enumMap.put(targetEnum.toString(), EnumUtils.getDescriptionByName(clz, targetEnum.toString()));
			}

			if ("json".equals(outputType)) {
				getJspContext().getOut().write(outputHtmlJson(enumMap));
			} else {
				getJspContext().getOut().write(outputHtmlSelect(enumMap).toString());
			}
		} catch (ClassNotFoundException e) {
			logger.error("Check a class path and name");
		} catch (ClassCastException e) {
			logger.error("It must be Enum type and implemented EnumCode");
		}
	}
	
	
	/*
	 * JSON으로 list 정보 반환
	 * 
	 */
	private String outputHtmlJson(Map<String, String> enumMap) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(enumMap);
	}
	
	
	/*
	 * 셀렉트박스로 반환
	 */
	private StringBuffer outputHtmlSelect(Map<String, String> enumMap) {
		StringBuffer outputHtml = new StringBuffer();

		outputHtml.append("<select name='").append(selectBoxName).append("'");
		
		if (StringUtils.isNotEmpty(selectBoxId)) {
			outputHtml.append("id='").append(selectBoxId).append("' ");
		}
		
		if (StringUtils.isNotEmpty(style)) {
			outputHtml.append(" style='").append(style).append("'");
		}
		
		outputHtml.append("> ");

		if (StringUtils.isNotEmpty(emptyValueName)) {
			outputHtml.append("<option value='' ");
			if (StringUtils.isEmpty(selectedValue)) {
				outputHtml.append("selected");
			}
			
			outputHtml.append(">").append(emptyValueName).append("</option>");
		}
		
		for (Map.Entry<String, String> targetEnum : enumMap.entrySet()) {
			
			outputHtml.append("<option value='" + targetEnum.getKey() + "' ");
			if (targetEnum.getKey().equals(selectedValue)) {
				outputHtml.append("selected");
			}
			outputHtml.append(">" + targetEnum.getValue() + "</option>");
		} 
		return outputHtml.append("</select>");
	}
}
