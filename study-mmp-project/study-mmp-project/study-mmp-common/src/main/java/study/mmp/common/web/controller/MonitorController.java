/*
 */
package study.mmp.common.web.controller;

import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 */
@Controller
@RequestMapping("/monitor/")
public class MonitorController {
    
    public static List<String> callVipServerIp = new CopyOnWriteArrayList<String>();
    
	private static boolean isAlive = true;
	private static final String LOCAL_HOST_IP = "127.0.0.1";
	private static final String RESULT_SUCCESS = "OK";
	private static final String RESULT_FAIL = "FAIL";
	
	@RequestMapping(value = "l7check", method = RequestMethod.GET)
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter writer = response.getWriter();

        if (isAlive) {
            writer.write(RESULT_SUCCESS);
            writer.flush();
            writer.close();
        } else {
            writer.write(RESULT_FAIL);
            callVipServerIp.add(request.getRemoteAddr());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
	}
	
	/**
	 */
	@RequestMapping(value = "enableL7", method = RequestMethod.GET)
	public ModelAndView enableL7(HttpServletRequest request, HttpServletResponse response) {
		if (isLocalAccess(request)) {
			setIsAlive(true);
		}
		return null;
	}

	@RequestMapping(value = "disableL7", method = RequestMethod.GET)
	public ModelAndView disableL7(HttpServletRequest request, HttpServletResponse response) {
		if (isLocalAccess(request)) {
			setIsAlive(false);
		}
		return null;
	}

	private boolean isLocalAccess(HttpServletRequest request) {
		return LOCAL_HOST_IP.equals(request.getRemoteAddr());
	}

	private void setIsAlive(boolean status) {
		MonitorController.isAlive = status;
	}
}
