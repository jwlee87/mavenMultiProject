
package study.mmp.web.member.model;

import study.mmp.common.annotaion.ExcelField;
import lombok.Data;


@Data
public class MemberExcelRow {
	
	@ExcelField(order = 0, title = "등록일시")private String registerYmdt;
	@ExcelField(order = 1, title = "최종적립일시")private String lastPointSaveYmdt;
	@ExcelField(order = 2, title = "회원번호") private long memberNo;
	@ExcelField(order = 4, title = "회원명")private String memberNm;
}
