package stackjava.com.sbsecurityhibernate.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import stackjava.com.sbsecurityhibernate.dao.UserDAO;
import stackjava.com.sbsecurityhibernate.entities.AccountMerch;
import stackjava.com.sbsecurityhibernate.entities.User;
import stackjava.com.sbsecurityhibernate.entities.UsersRoles;
import stackjava.com.sbsecurityhibernate.entities.uploadFile;

@RequestMapping("/admin")
@Controller
public class Admin {
	@Autowired
	private UserDAO userDAO;
	@RequestMapping("/alluser")
	public String user(HttpSession session,HttpServletRequest request,Model model) {
		try {
			User user= (User) session.getAttribute("user");
			List<User> lst=userDAO.getAllUserActive();
			model.addAttribute("lst", lst);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "dashboard/alluser";
	}
	
	
	
	 @RequestMapping("/delete")
    @ResponseBody
    public String deleteAccountMerch( HttpSession session, Model model, HttpServletRequest request) {
    	try {
    		String id=request.getParameter("id");
    		boolean check=userDAO.deleteuser(Integer.parseInt(id));
			if(check)
			{
				return "00";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return"01";
    	    
    	
    }
	 
	 
	 
	 @ResponseBody
	@RequestMapping("/adduser")
	public String adduser(@ModelAttribute User acc,HttpSession session,HttpServletRequest request,Model model) {
		try {
			BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
			User user= (User) session.getAttribute("user");
			acc.setPassword(bCryptPasswordEncoder.encode(acc.getPassword()));
			acc.setEnabled(1);
			//userDAO.saveOrUpdateUser(acc);
			User accRP=userDAO.saveOrUpdateUser(acc);
			if(accRP!=null && accRP.getId() != null)
			{
				
				userDAO.insertusersroles(accRP.getId(),2);
				
				
				
				return "00";
			}
			//List<User> lst=userDAO.getAllUserActive();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return"01";
	}
	
	 @ResponseBody
	@RequestMapping("/addRole")
	public String addRole(@ModelAttribute User acc,HttpSession session,HttpServletRequest request,Model model) {
		try {
			String id=request.getParameter("id");
			
				
				userDAO.insertusersroles(Integer.parseInt(id),2);
				
				
				
				return "00";
			
			//List<User> lst=userDAO.getAllUserActive();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return"01";
	}
	 @ResponseBody
		@RequestMapping("/clearData")
		public String clearData(HttpSession session,HttpServletRequest request,Model model) {
			try {
				String home = System.getProperty("user.home");
				List<uploadFile> getAllUploadFileClear=userDAO.getAllUploadFileClear();
				List<Integer> ids = new ArrayList<Integer>();
				
				if(!getAllUploadFileClear.isEmpty()) {
					for (uploadFile uploadFile : getAllUploadFileClear) {
						String uploadDir = home + "/Downloads/" + uploadFile.getUsername() + "/"+uploadFile.getName();
						Path uploadPath = Paths.get(uploadDir);
						 Files.deleteIfExists(uploadPath);
						 ids.add(uploadFile.getId());
					}
				} 
				userDAO.UpdateClearUploadFileFromlistID(ids);
					
					
					return "00";
				
				//List<User> lst=userDAO.getAllUserActive();
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			return"01";
		}
}
