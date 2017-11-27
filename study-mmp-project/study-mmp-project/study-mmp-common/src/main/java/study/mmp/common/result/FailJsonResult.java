package study.mmp.common.result;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
class FailJsonResult implements JsonResult.Fail {
	
	@JsonIgnore
	Type resultType =  Type.FAIL;
	
	int code = 200;
	
	String message = "";
	
	Map<String, Object> info;
	
	
	@JsonIgnore int statusCode = 200;
	
	
	public FailJsonResult(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	@Override
	public JsonResult.Fail addInfo(String fieldName, Object value) {
		info.put(fieldName, value);
		return this;
	}
}
