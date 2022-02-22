package stackjava.com.sbsecurityhibernate.config;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.StringUtils;

import stackjava.com.sbsecurityhibernate.dao.UserDAO;
import stackjava.com.sbsecurityhibernate.entities.User;


public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	
	

	
	@Autowired
	private UserDAO userDAO;
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		
		//set session
		String userName = authentication.getName();
		
		User user=userDAO.loadUserByUsername(userName);
		
		request.getSession().setAttribute("user", user);
		String targetUrl = "/user/checkSale";
		handle(request, response, authentication,1l, targetUrl);
	
		
	}
protected void handle(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication,Long typeDashboard, String targetUrl) throws IOException {
		
		if (response.isCommitted()) {
			
			return;
		}

		redirectStrategy.sendRedirect(request, response, targetUrl);
	}
	


	

	

}
