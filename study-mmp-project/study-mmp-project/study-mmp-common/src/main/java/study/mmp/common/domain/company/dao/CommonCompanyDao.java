package study.mmp.common.domain.company.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import study.mmp.common.domain.company.model.Company;

@Repository
public class CommonCompanyDao {

	@Qualifier("mainSqlSession")
	@Autowired
	private SqlSessionTemplate membershipSql;
	 
	private static final String MAPPER_NAMESPACE = "mapper.common.domain.company.common_company_dao.";
	 
	@Cacheable(value = "company", key = "{\"company\", T(java.lang.String).valueOf(#companyCode)}")
	public Company selectCompanyByCompanyCd(String companyCd) {
		return membershipSql.selectOne(MAPPER_NAMESPACE + "selectCompanyByCompanyCd", companyCd);
	}

    public List<Company> selectCompanyList() {
        return membershipSql.selectList(MAPPER_NAMESPACE + "selectCompanyList");
    }
}
