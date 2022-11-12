package com.talk2amareswaran.projects.socialloginapp.config;

import java.security.Principal;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

import com.talk2amareswaran.projects.socialloginapp.dao.AppRoleDAO;
import com.talk2amareswaran.projects.socialloginapp.dao.AppUserDAO;
import com.talk2amareswaran.projects.socialloginapp.entity.AppRole;
import com.talk2amareswaran.projects.socialloginapp.entity.AppUser;
import com.talk2amareswaran.projects.socialloginapp.entity.UserRole;
import com.talk2amareswaran.projects.socialloginapp.dao.UserRoleDao;
import com.talk2amareswaran.projects.socialloginapp.service.SocialUserDetailsImpl;
import net.bytebuddy.agent.builder.AgentBuilder.Listener;

@Component
public class AuthenticationSession implements AuthenticationSuccessHandler{
	
	    //@Autowired HttpSession session; //autowiring session
		@Value("${spring.data.cassandra.username}")
		private String admin;

		@Autowired
		AppUserDAO userDao;

		@Autowired
		AppRoleDAO roleDao;

		@Autowired
		UserRoleDao userRoleDao;

	    private static final Logger logger = LoggerFactory.getLogger(AuthenticationSession.class);
	    
	    @Override
	    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
	            Authentication authentication) throws IOException, ServletException {
	        // TODO Auto-generated method stub
	        //String userName = "";
	    	String userName = "";
	        if(authentication.getPrincipal() instanceof Principal) {
	             userName = ((Principal)authentication.getPrincipal()).getName();

	        }
		
			else {
				 /*AppUser user = userDao.findAppUserByUserName(userName);
				 //UserRole userRole = userRoleDao.findUserRole(user.getUserId());
				 UserRole userRole = userRoleDao.findAppRoleByUserId(user.getUserId());
				 AppRole appRole = roleDao.findAppRoleById(userRole.getAppRole().getRoleId());
				 if(appRole.getRoleName().equals("ROLE_ADMIN")){
					response.sendRedirect("/adminAddNewGet");
				 } 
				 else if(appRole.getRoleName().equals("ROLE_USER")){
					response.sendRedirect("/addNew");     	
				 }*/
	            userName = ((SocialUserDetailsImpl)authentication.getPrincipal()).getUsername();
				if(userName.equals(admin)){
					response.sendRedirect("/adminAddNewGet");
				}
				else{
	            response.sendRedirect("/addNew");
				}

	    }
    }
}
