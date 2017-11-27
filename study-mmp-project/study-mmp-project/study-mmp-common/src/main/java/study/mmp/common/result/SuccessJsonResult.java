package study.mmp.common.result;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
class SuccessJsonResult implements JsonResult {
	
	@JsonIgnore
	Type resultType =  Type.SUCCESS;
	
	int code = 0;
	
	String message = "SUCCESS";
	
	@JsonIgnore int statusCode = 200;
	
	Object result;
}
