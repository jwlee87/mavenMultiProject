package study.mmp.common.exception.system;

import java.util.Arrays;

@SuppressWarnings("serial")
public class LeastOneRequiredParameterException extends RuntimeException {

    public LeastOneRequiredParameterException(String... paramterName) {
        super("required paramter at least one -> " + Arrays.asList(paramterName).toString());
    }
}
