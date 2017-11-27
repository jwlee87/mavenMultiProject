package study.mmp.common.constant.external;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 회원 상태
 * 
 * 회원 개발팀 공통 - 협의 후 변경
 */
@AllArgsConstructor
public enum MemberStatus {
	USE("정상"),
	STOP_REQ("탈퇴신청"),
	STOP("탈퇴");
	//WARN("WARN", "이용제한");
	
    @Getter private String description;
}
