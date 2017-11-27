package study.mmp.common.component.caller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import study.mmp.common.component.caller.model.rest.ApiMember;
import study.mmp.common.component.caller.model.rest.CommonResponse;
import study.mmp.common.component.caller.model.rest.Header;
import study.mmp.common.component.http.ApiHelper;
import study.mmp.common.component.http.ApiResultException;
import study.mmp.common.constant.external.MemberJoinChannel;
import study.mmp.common.constant.external.MemberStatus;

/**
 * 
 */
@Component
public class RestApi {
    
	@Autowired ApiHelper apiHelper;
	
    @Value("${rest.api.url:}") private String domain;

    public ApiMember joinMember(String companyCd, MemberJoinChannel joinChannel, String id) {
    	ApiMember member = new ApiMember();
    	member.setId(id);
    	member.setStatus(MemberStatus.USE);
    	member.setJoinChannel(joinChannel);

    	Map<String, Object> param = new HashMap<>();
    	param.put("member", member);

    	ResponseEntity<CommonResponse> res = apiHelper.restTemplate().postForEntity(domain + "/v1.0/services/{serviceId}/members", param, CommonResponse.class, companyCd);
    	CommonResponse commonRes = res.getBody();
    	
    	Header head = commonRes.getHeader();
    	if (head.getResultCode() == 9998) {
            throw new ApiResultException(Integer.toString(head.getResultCode()), head.getResultMessage());
    	}
    	
    	return commonRes.getMember();
    }
    

	public ApiMember getMemberInfo(String companyCd, long memberNo) {
		ResponseEntity<CommonResponse> res = apiHelper.restTemplate().getForEntity(domain + "/v1.0/services/{serviceId}/members/{id}", CommonResponse.class, companyCd, String.valueOf(memberNo));
		CommonResponse body = res.getBody();
		return body.getMember();
	}

	public void outMember(String companyCd, long memberNo) {
		apiHelper.restTemplate().delete(domain + "/v1.0/services/{serviceId}/members/{id}", companyCd, String.valueOf(memberNo));
	}
}
