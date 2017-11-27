
package study.mmp.web.member.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import study.mmp.common.domain.company.dao.CommonCompanyDao;
import study.mmp.common.domain.company.model.Company;
import study.mmp.common.result.JsonResult;
import study.mmp.common.result.JsonResults;
import study.mmp.web.annotation.CompanyCd;
import study.mmp.web.member.param.MemberJoinParam;
import study.mmp.web.member.service.MemberJoinService;
import study.mmp.web.support.Pager;

@Controller
@RequestMapping(value = "/member/memberList")
public class MemberController {
	
	@Autowired MemberJoinService memberService;
	@Autowired CommonCompanyDao commonCompanyDao;
	
	@RequestMapping(value = "/joinAjax", method = RequestMethod.POST)
	@ResponseBody
    public JsonResult joinMemberAjax(@CompanyCd String companyCd, MemberJoinParam param) {
		param.setCompanyCd(companyCd);
		
		memberService.joinMember(param);
        return JsonResults.success();
    }
	
	
	@RequestMapping(value = "test", method = RequestMethod.GET)
	@ResponseBody
    public JsonResult test2(@CompanyCd String companyCd, MemberJoinParam param, Pager pager) {
		
		List<Company> list = memberService.getBrand(param, pager);
        return JsonResults.success(list);
    }
}
