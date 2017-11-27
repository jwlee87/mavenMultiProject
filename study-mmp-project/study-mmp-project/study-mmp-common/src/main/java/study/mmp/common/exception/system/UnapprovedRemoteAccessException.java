package study.mmp.common.exception.system;

@SuppressWarnings("serial")
public class UnapprovedRemoteAccessException extends RuntimeException {
    
    private String remoteInfo;
    
    public UnapprovedRemoteAccessException(String remoteInfo) {  
        super("'remoteInfo=" + remoteInfo);
        this.remoteInfo = remoteInfo;
    }

    public String getRemoteInfo() {
        return remoteInfo;
    }

    public void setRemoteInfo(String remoteInfo) {
        this.remoteInfo = remoteInfo;
    }
}
