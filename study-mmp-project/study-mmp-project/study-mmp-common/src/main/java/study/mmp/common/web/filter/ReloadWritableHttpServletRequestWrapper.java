package study.mmp.common.web.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.util.FileCopyUtils;

public class ReloadWritableHttpServletRequestWrapper extends HttpServletRequestWrapper {
    
    
    byte[] bodyData;
    
    public ReloadWritableHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
    }
    
    public void setBody(byte[] replaceBodyBytes) {
    	bodyData = replaceBodyBytes;
    }
    
    @Override
    public ServletInputStream getInputStream() throws IOException {

        if (bodyData == null) {
            InputStream is = super.getInputStream();
            bodyData = FileCopyUtils.copyToByteArray(is);
        }

        
        final ByteArrayInputStream bis = new ByteArrayInputStream(bodyData);
        
        return new ServletInputStream() {
            private int last = 0;
            
            @Override
            public int read() throws IOException {
                return last = bis.read();
            }

            @Override
            public int read(byte[] bytes) throws IOException {
                return last = bis.read(bytes);
            }
            
            @Override
            public boolean isFinished() {
                return last == -1;
            }
            
            @Override
            public boolean isReady() {
                return true;
            }
            
            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
    }
}
