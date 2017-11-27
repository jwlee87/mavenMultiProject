package study.mmp.web.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum AccessType {
	READ("조회"),
	MOD("조회/수정");

	@Getter private String description;
}

