package study.mmp.common.component.caller.model.rest;

import study.mmp.common.constant.external.MemberJoinChannel;
import study.mmp.common.constant.external.MemberStatus;
import lombok.Data;

@Data
public class ApiMember {

	private String id;

	private MemberJoinChannel joinChannel;
	private MemberStatus status;
}
