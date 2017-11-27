package study.mmp.web.member.param;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

/**
 * 회원 가입시 입력 받는 데이터
 */
@Data
public class MemberJoinParam {
	
	@NotNull
	private String companyCd;
	
	@NotEmpty(message = "이름은 필수 입니다.")
	private String memberNm;
	private int age;
}
