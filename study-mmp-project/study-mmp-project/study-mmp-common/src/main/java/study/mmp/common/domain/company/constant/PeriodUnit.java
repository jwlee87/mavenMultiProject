package study.mmp.common.domain.company.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
public enum PeriodUnit {
    DAY("매일"),
    MONTH("매월"),
    YEAR("매년");
    
	@Getter private String description;
}
