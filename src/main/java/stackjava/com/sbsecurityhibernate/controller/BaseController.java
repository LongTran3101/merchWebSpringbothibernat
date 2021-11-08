package stackjava.com.sbsecurityhibernate.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import stackjava.com.sbsecurityhibernate.dao.UserDAO;
import stackjava.com.sbsecurityhibernate.entities.SaleMerch;

@Controller
public class BaseController {
	@Autowired
	private UserDAO userDAO;
	@RequestMapping(value = { "/", "/login" })
	public String login(@RequestParam(required = false) String message, final Model model) {
		if (message != null && !message.isEmpty()) {
			if (message.equals("logout")) {
				model.addAttribute("message", "Logout!");
			}
			if (message.equals("error")) {
				model.addAttribute("message", "Login Failed!");
			}
		}
		return "login";
	}
	
	@RequestMapping
	public String test2(){
	    return "login";
	}

	
	@ResponseBody
    @RequestMapping(value = "/saveCheckSale", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	private String test( @RequestBody String req,HttpServletRequest request, HttpServletResponse resp) {
    
    	try {
    		ObjectMapper objectMapper = new ObjectMapper();
    		SaleMerch mech=objectMapper.readValue(req, SaleMerch.class);
    		 Calendar now = Calendar.getInstance();
    	        now.set(Calendar.HOUR, 0);
    	        now.set(Calendar.MINUTE, 0);
    	        now.set(Calendar.SECOND, 0);
    	        now.set(Calendar.HOUR_OF_DAY, 0);
    		mech.setDay(now.getTime());
    		try {
    			try {
    				userDAO.deleteSaleMerch(mech.getUsername(), mech.getName(), now.getTime());
				} catch (Exception e) {
					// TODO: handle exception
				}
    			
    			
    			//userDAO.saveOrUpdate(mech);
			} catch (Exception e) {
				// TODO: handle exception
			}
    		SaleMerch rp=userDAO.saveOrUpdate(mech);
    		if(rp!=null && rp.getId()!=null)
    		{
    			 return "00";
    		}
			
    		//model.addAttribute("lst", lst);
           
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return"01";
    	    
    	
    }
	
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}

		return "redirect:/";
	}
	
	@ResponseBody
	@RequestMapping("/api")
	public String api() {
		return "api";
	}

	@RequestMapping("/403")
	public String accessDenied() {
		return "403";
	}
}
