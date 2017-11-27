package study.mmp.web.common.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import study.mmp.common.result.JsonResult;
import study.mmp.common.result.JsonResults;
import study.mmp.common.util.CookieUtils;
import study.mmp.web.annotation.NoAuth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
public class MainController {
	
    @RequestMapping(value = "/changeCompany", method = RequestMethod.GET)
    public String afterlogin(HttpServletResponse response, HttpServletRequest request, String companyCd) {
        CookieUtils.setCookie(response, "companyCd", companyCd, -1, ".xxx.com");
        return "redirect:/";
    }

    @NoAuth
    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String main(HttpServletRequest request, Model model) {
        if (request.getAttribute("maskingEmail") != null) {
            return "redirect:/point/tradeList.do";
        }
        
        model.addAttribute("num", 123);
        return "main/main";
    }

    @NoAuth
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult main2(HttpServletRequest request) {
    	
        return JsonResults.success("오브젝트");
    }
}
