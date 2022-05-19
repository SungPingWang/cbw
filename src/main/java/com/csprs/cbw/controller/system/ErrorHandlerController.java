package com.csprs.cbw.controller.system;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorHandlerController implements ErrorController {

	@RequestMapping("/403")
	public String accessDenied() {
	    return "error/403.html";
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}

	@GetMapping()
    public String handleError(HttpServletRequest request) {
        String errorPage = "error"; // default
        //javax.servlet.error.message
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
         
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
             
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                errorPage = "error/404";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                errorPage = "error/500";
                 
            }
        }
         
        return errorPage;
    }
	
	@GetMapping("/login_failed/{cause}")
    public String login_error(@PathVariable(name = "cause") String error, Model model) {
		
		model.addAttribute("errorMsg", "登入驗證錯誤，請重新進行登入");
		
        return "login";
    }
	
}
