package study.mmp.common.component.caller.model.rest;

import lombok.Data;

@Data
public class Header {

	private int resultCode;
	private String resultMessage;
	private boolean isSuccessful;
}
