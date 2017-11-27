package study.mmp.common.result;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
class ErrorJsonResult implements JsonResult.Error {
	
	@JsonIgnore
	Type resultType =  Type.ERROR;
	
	int code = 200;
	
	String message = "";
	
	Map<String, Object> info;
	
	
	@JsonIgnore int statusCode = 200;
	
	@JsonIgnore Throwable exception;

	
	public ErrorJsonResult(int code, String message, int statusCode, Throwable exception) {
		this.code = code;
		this.message = message;
		this.statusCode = statusCode;
		this.exception = exception;
	}
	
	@Override
	public JsonResult.Fail addInfo(String fieldName, Object value) {
		info.put(fieldName, value);
		return this;
	}
}
