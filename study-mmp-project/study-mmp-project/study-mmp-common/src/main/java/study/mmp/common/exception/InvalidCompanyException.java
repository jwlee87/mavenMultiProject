package study.mmp.common.exception;

/**
 */
@SuppressWarnings("serial")
public class InvalidCompanyException extends RuntimeException {
	
	 public InvalidCompanyException(String companyCd, String status) {
		 super("Company..." + companyCd + "/" + status);
	 }
}
