package study.mmp.common.util;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class CopyUtil {

    public static <T> List<T> copyList(List<?> sourceList, Class<T> tClass) throws IllegalAccessException, InstantiationException {
        List<T> targetList = new ArrayList<>();
        for (Object source : sourceList) {
            T target = tClass.newInstance();
            BeanUtils.copyProperties(source, target);
            targetList.add(target);
        }
        return targetList;
    }
}
