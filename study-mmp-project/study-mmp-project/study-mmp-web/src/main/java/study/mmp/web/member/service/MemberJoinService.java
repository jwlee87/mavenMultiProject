package study.mmp.web.member.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import study.mmp.common.domain.company.dao.CommonCompanyDao;
import study.mmp.common.domain.company.model.Company;
import study.mmp.web.member.dao.MemberJoinDao;
import study.mmp.web.member.param.MemberJoinParam;
import study.mmp.web.member.param.MemberJoinResult;
import study.mmp.web.support.Pager;

import com.google.common.collect.Lists;


@Service
public class MemberJoinService {

	
	@Autowired private CommonCompanyDao commonCompanyDao;
	@Autowired private MemberJoinDao memberJoinDao;

	//회원 가입
	public MemberJoinResult joinMember(MemberJoinParam param) {
		return null;
	}
	
	
	public List<Company> getBrand(MemberJoinParam param, Pager pager) {

		pager.setTotalCount(memberJoinDao.selectCompanyCount(param.getMemberNm(), param.getAge()));
		if (pager.isEnoughListQuery()) {
			return memberJoinDao.selectCompanyList(param, pager);
		} else {
			return Lists.newArrayList();
		}
	}
	
	public List<Company> getBrand2(MemberJoinParam param, Pager pager) {

		pager.setTotalCount(memberJoinDao.selectCompanyCount(param));
		if (pager.isEnoughListQuery()) {
			return memberJoinDao.selectCompanyList(param, pager);
		} else {
			return Lists.newArrayList();
		}
	}
}
