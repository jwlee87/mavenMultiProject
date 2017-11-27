package study.mmp.common.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.web.multipart.MultipartFile;

public class FileUtils {
	
	enum AbleFileExt {
		xls,
		jpg
	}
	
	public static final String NAS_BATCH_FILE_PATH = "PATH~~";

    public static File makeFileToNAS(String companyCd, String fileName, AbleFileExt ext) throws IOException {
 
		return makeFile(companyCd, fileName, ext);
    }
    

    /**
     * NAS 경로에 파일을 저장
     */
    public static File moveFileToNAS(MultipartFile srcfile, String key, String fileName, AbleFileExt ext) throws IOException {
    	
    	File destFile = makeFile(key, fileName, ext);
    	org.apache.commons.io.FileUtils.copyInputStreamToFile(srcfile.getInputStream(), destFile);
		return destFile;
    }
    
    
    
    
    private static String getCompanyNasDirectory(String key) {
    	String dirPath = NAS_BATCH_FILE_PATH + key + "/";
    	if (!Files.exists(Paths.get(dirPath))) {
    		new File(dirPath).mkdirs();
    	}
    	return dirPath;
    }
    
    private static File makeFile(String key, String fileName, AbleFileExt ext) {
    	
    	String dirPath = getCompanyNasDirectory(key);
    	
    	String filePath = dirPath + fileName + "." + ext.name();
    	int tempCount = 1;
    	while (Files.exists(Paths.get(filePath))) {
			filePath = dirPath + fileName + "(" + (tempCount++) + ")"  + ext;
		}
    	
		File newFile = new File(filePath);
    	return newFile;
    }
}
