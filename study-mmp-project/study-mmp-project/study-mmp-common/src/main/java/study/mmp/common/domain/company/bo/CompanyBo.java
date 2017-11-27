package study.mmp.common.domain.company.bo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import study.mmp.common.domain.company.dao.CommonCompanyDao;
import study.mmp.common.domain.company.model.Company;

@Service
public class CompanyBo {

	@Autowired CommonCompanyDao commonCompanyDao;
	
	public Company test() {
		return commonCompanyDao.selectCompanyByCompanyCd("CODE223");
	}
}
