package study.mmp.common.component.caller.model.rest;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CommonResponse {

	private Header header;
	private ApiMember member;

	private List<ApiMember> memberList = new ArrayList<>();
}
