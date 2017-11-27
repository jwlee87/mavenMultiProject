package study.mmp.web.configuration;


import javax.servlet.http.HttpServletRequest;
import com.opensymphony.module.sitemesh.Decorator;
import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.module.sitemesh.mapper.ConfigDecoratorMapper;


public class ContextRemoveConfigDecoratorMapper extends ConfigDecoratorMapper {
  

    public Decorator getDecorator(final HttpServletRequest request, final Page page) {

        String thisPath = request.getRequestURI();
        String contextPath = request.getContextPath();
  
        if (thisPath.startsWith(contextPath)) {
            thisPath = thisPath.substring(contextPath.length());
        }
        
        return super.getDecorator(request, page);
    }
}