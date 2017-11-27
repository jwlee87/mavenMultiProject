package study.mmp.web.member.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import study.mmp.common.domain.company.model.Company;
import study.mmp.web.member.param.MemberJoinParam;
import study.mmp.web.support.Pager;

import com.google.common.collect.Maps;

@Repository
public class MemberJoinDao {

	@Qualifier("sqlSession")
    @Autowired SqlSessionTemplate sql;
    
    static final String MAPPER_NAMESPACE = "mapper.web.member.member_join_dao.";

	
	public List<Company> selectCompanyList(MemberJoinParam param, Pager pager) {
		
        Map<String, Object> params = pager.getParamsMap();
		params.put("key", param.getMemberNm());
		params.put("no", param.getAge());
		return sql.selectList(MAPPER_NAMESPACE + "selectCompanyList", params);
	}
	
	public int selectCompanyCount(MemberJoinParam param) {
		return sql.selectOne(MAPPER_NAMESPACE + "selectCompanyList", param);
	}
	
	public int selectCompanyCount(String key, int no) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("key", key);
		params.put("no", no);
		return sql.selectOne(MAPPER_NAMESPACE + "selectCompanyList", params);
	}
}
