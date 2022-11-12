package com.talk2amareswaran.projects.socialloginapp.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.talk2amareswaran.projects.socialloginapp.dao.AppRoleDAO;
import com.talk2amareswaran.projects.socialloginapp.dao.AppUserDAO;
import com.talk2amareswaran.projects.socialloginapp.entity.AppRole;
import com.talk2amareswaran.projects.socialloginapp.entity.AppUser;
import com.talk2amareswaran.projects.socialloginapp.form.AppUserForm;
import com.talk2amareswaran.projects.socialloginapp.utils.EncrytedPasswordUtils;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private AppUserDAO appUserDAO;

	@Autowired
	private AppRoleDAO appRoleDAO;

	@Autowired
	private HttpSession session;


	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		System.out.println("UserDetailsServiceImpl.loadUserByUsername=" + userName);
		AppUser appUser = this.appUserDAO.findAppUserByUserName(userName);
		session.setAttribute("userName",userName);

		if (appUser == null) {
			System.out.println("User not found! " + userName);
			throw new UsernameNotFoundException("User " + userName + " was not found in the database");
		}

		System.out.println("Found User: " + appUser);
	

		List<String> roleNames = this.appRoleDAO.getRoleNames(appUser.getUserId());

		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		if (roleNames != null) {
			for (String role : roleNames) {
				GrantedAuthority authority = new SimpleGrantedAuthority(role);
				grantList.add(authority);
			}
		}

		SocialUserDetailsImpl userDetails = new SocialUserDetailsImpl(appUser, roleNames);

		return userDetails;
	}
    
	public AppUser registerUser(AppUserForm appUserForm){
	  AppUser appUser = new AppUser();
	  appUser.setUserName(appUserForm.getUserName());
	  appUser.setFirstName(appUserForm.getFirstName());
	  appUser.setLastName(appUserForm.getLastName());
	  appUser.setEnabled(true);
	  String encrytedPassword = EncrytedPasswordUtils.encrytePassword(appUserForm.getPassword());
	  appUser.setEncrytedPassword("{bcrypt}"+encrytedPassword);

	  return appUser;
	}

}
