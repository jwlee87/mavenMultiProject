package study.mmp.common.constant.external;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 가입 채널 = 발급채널
 * 
 * 외부 연동 코드
 */
@AllArgsConstructor
public enum MemberJoinChannel {
	
	COMPANY_STORE("멤버십사 오프라인매장", "72"),
	COMPANY_WEB("멤버십사 웹", "73"),
	COMPANY_APP("멤버십사 앱", "74"),
	SYRUP("시럽", "75"),
	CLIP("클립", "76"),
	ADMIN("센터", "77");
	
	@Getter private String description;
	@Getter private String codeNo;
}
