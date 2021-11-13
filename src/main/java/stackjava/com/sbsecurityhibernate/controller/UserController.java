package stackjava.com.sbsecurityhibernate.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import stackjava.com.sbsecurityhibernate.dao.CallAPi;
import stackjava.com.sbsecurityhibernate.dao.UserDAO;
import stackjava.com.sbsecurityhibernate.entities.AccountMerch;
import stackjava.com.sbsecurityhibernate.entities.ImageMerch;
import stackjava.com.sbsecurityhibernate.entities.SaleMerch;
import stackjava.com.sbsecurityhibernate.entities.User;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserDAO userDAO;
	@RequestMapping("/allAcc")
	public String user(HttpSession session,HttpServletRequest request,Model model) {
		try {
			User user= (User) session.getAttribute("user");
			List<AccountMerch> lst=userDAO.getAllUser(user.getUsername());
			model.addAttribute("lst", lst);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "dashboard/allAccount";
	}
	
	@RequestMapping("/checkSale")
	public String checkSaleMerch( HttpSession session, Model model, HttpServletRequest request) {
		String daySeach=request.getParameter("daySearch");
		String daySearchTo=request.getParameter("daySearchTo");
		User user= (User) session.getAttribute("user");

		model.addAttribute("daySeach", daySeach);
    	model.addAttribute("daySearchTo", daySearchTo);
    	if(daySeach!=null && daySeach!="")
    	{
    		
    		List<SaleMerch> lst=userDAO.getAllSaleMerch(daySeach, daySearchTo,user.getUsername());
    		model.addAttribute("lst", lst);
            return "dashboard/checkSaleMerch";
    	}
    	
		model.addAttribute("lst", null);
		return "dashboard/checkSaleMerch";
	}
	
	

    @RequestMapping("/saveAccountMerch")
    @ResponseBody
    public String saveAccountMerch(@ModelAttribute AccountMerch acc, HttpSession session, Model model, HttpServletRequest request) {
    	try {
    		AccountMerch accSave=acc;
    		User user= (User) session.getAttribute("user");		
    		accSave.setUsername(user.getUsername());
    		accSave.setDay(new Date());
    		accSave.setIp(acc.getIp().trim());
    		accSave.setPath(acc.getPath().trim());
    		AccountMerch accRP=userDAO.saveOrUpdate(accSave);
			if(accRP!=null && accRP.getId() != null)
			{
				return "00";
			}
    		//model.addAttribute("lst", lst);
            
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return"01";
    	    
    	
    }
    
    @RequestMapping("/deleteAccountMerch")
    @ResponseBody
    public String deleteAccountMerch( HttpSession session, Model model, HttpServletRequest request) {
    	try {
    		String id=request.getParameter("id");
    		boolean check=userDAO.deleteAcoutMerch(Integer.parseInt(id));
			if(check)
			{
				return "00";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return"01";
    	    
    	
    }
    
    @RequestMapping("/checkSaleById")
    @ResponseBody
    public String checkSaleById( HttpSession session, Model model, HttpServletRequest request) {
    	try {
    		String id=request.getParameter("id");
    		

    		AccountMerch merch=userDAO.getAccountMerchByID(Integer.parseInt(id));
    		

    		ObjectMapper objectMapper = new ObjectMapper();
    		String req = objectMapper.writeValueAsString(merch);
    		CallAPi callApi=new CallAPi();
    		try {
    			String rep =callApi.callAPIPost("http://"+merch.getIp()+":8080/checksalemerchtest", req);
        		//model.addAttribute("lst", lst);
        		if(rep!=null && rep.equalsIgnoreCase("00"))
    			{
    				return "00";
    			}
			} catch (Exception e) {
				return"02";
			}
    		
		} catch (Exception e) {
			e.printStackTrace();
			return"01";
			
		}
    	return"01";
    	    
    	
    }
    
    
    
    @RequestMapping("/showDS")
    public String showDS( HttpSession session, Model model, HttpServletRequest request) {
    	try {
    		String id=request.getParameter("id");
    		

    		SaleMerch merch=userDAO.getSaleMerchByID(Integer.parseInt(id));
    		List<ImageMerch> lst=userDAO.getImageMerchFromSaleMerch(merch);
    		model.addAttribute("lst", lst);
    		
    		
		} catch (Exception e) {
			e.printStackTrace();
			
		}
    	return"dashboard/fmDS :: channelEdit";
    	    
    	
    }
    
    
    
    @RequestMapping("/checkSaleAllAcc")
    @ResponseBody
    public String checkSaleAllAcc( HttpSession session, Model model, HttpServletRequest request) {
    	try {
    		User user= (User) session.getAttribute("user");		
    		List<AccountMerch> lst=userDAO.getAllUser(user.getUsername());
    		for (AccountMerch merch : lst) {
    			ObjectMapper objectMapper = new ObjectMapper();
        		String req = objectMapper.writeValueAsString(merch);
        		CallAPi callApi=new CallAPi();
        		try {
        			String rep =callApi.callAPIPost("http://"+merch.getIp()+":8080/checksalemerchtest", req);
            		
    			} catch (Exception e) {
    				continue;
    			}
			}

    		

    		
    		
		} catch (Exception e) {
			e.printStackTrace();
			return"01";
			
		}
    	return"00";
    	    
    	
    }
    
    
    
    @ResponseBody
    @RequestMapping(value = "/saveCheckSale", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	private String test( @RequestBody String req,HttpServletRequest request, HttpServletResponse resp) {
    
    	try {
    		ObjectMapper objectMapper = new ObjectMapper();
    		SaleMerch mech=objectMapper.readValue(req, SaleMerch.class);
    		mech.setDay(new Date());
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

    

    
    
    
	
}
