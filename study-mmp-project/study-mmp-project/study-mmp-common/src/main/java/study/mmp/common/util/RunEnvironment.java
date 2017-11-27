package study.mmp.common.util;

import lombok.Getter;
import lombok.Setter;

/**
 * 실행 환경 및
 * static 접근이 필요할 때 사용
 */
public class RunEnvironment {
    
    @Getter @Setter private static String domain = "";
    
    @Getter private static Phase phase = null;
    
    @Getter @Setter private static boolean https = false;
    
    public String getFullDomain() {
        StringBuilder sb = new StringBuilder();
        sb.append(https ? "https//" : "http://");
        sb.append(phase.preUrlPhase);
        sb.append(domain);
        return sb.toString();
    }
    
    public static void setPhase(String runPhase) {
        phase = Phase.valueOf(runPhase);
    }
    
    
    public enum Phase {
        
        LOCAL("local-", true),
        ALPHA("alpha-", true),
        BETA("beta-", false),
        RELEASE("", false);
        
        @Getter private String preUrlPhase;
        @Getter private boolean dev;
        
        private Phase(String preUrlPhase, boolean dev) {
            this.preUrlPhase = preUrlPhase;
            this.dev = dev;
        }
    }
}
