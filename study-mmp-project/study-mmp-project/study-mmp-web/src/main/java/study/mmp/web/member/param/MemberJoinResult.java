package study.mmp.web.member.param;

import study.mmp.web.constant.AccessType;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MemberJoinResult {
	
    private String methodNo;
    private AccessType accessType;
    
    @JsonProperty(value = "CARD_NO")
    private String cardNo;

}
