package study.mmp.common.web.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.CharacterEncodingFilter;


public class GlobalFilter extends CharacterEncodingFilter {

    public final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        super.doFilterInternal(request, response, filterChain);
    }
}
