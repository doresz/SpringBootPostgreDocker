package com.talk2amareswaran.projects.socialloginapp.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.UserProfile;
import org.springframework.stereotype.Repository;

import com.talk2amareswaran.projects.socialloginapp.entity.AppRole;
import com.talk2amareswaran.projects.socialloginapp.entity.AppUser;
import com.talk2amareswaran.projects.socialloginapp.form.AppUserForm;
import com.talk2amareswaran.projects.socialloginapp.utils.EncrytedPasswordUtils;


@Repository
@Transactional
public class AppUserDAO {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private AppRoleDAO appRoleDAO;

	public AppUser findAppUserByUserId(Long userId) {
		try {
			String sql = "select e from " + AppUser.class.getName() + " e where e.userId = :userId ";
			Query query = entityManager.createQuery(sql, AppUser.class);
			query.setParameter("userId", userId);
			return (AppUser) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public AppUser findAppUserByUserName(String userName) {
        try {
				String sql = "select e from " + AppUser.class.getName() + " e " 
						+ " where e.userName = :userName ";
				Query query = entityManager.createQuery(sql, AppUser.class);
				query.setParameter("userName", userName);
				return (AppUser) query.getSingleResult();
			} catch (NoResultException e) {
				return null;
			}

    }
	
	/*public AppUser findByEmail(String email) {
        try {
            String sql = "select e from " + AppUser.class.getName() + " e " 
                    + " where e.email = :email ";
            Query query = entityManager.createQuery(sql, AppUser.class);
            query.setParameter("email", email);
            return (AppUser) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }*/
	
	 private String findAvailableUserName(String userName_prefix) {
	        AppUser account = this.findAppUserByUserName(userName_prefix);
	        if (account == null) {
	            return userName_prefix;
	        }
	        int i = 0;
	        while (true) {
	            String userName = userName_prefix + "_" + i++;
	            account = this.findAppUserByUserName(userName);
	            if (account == null) {
	                return userName;
	            }
	        }
	    }
	 
	// Auto create App User Account.
	    public AppUser createAppUser(Connection<?> connection) {
	        ConnectionKey key = connection.getKey();
	        System.out.println("key= (" + key.getProviderId() + "," + key.getProviderUserId() + ")");
	        UserProfile userProfile = connection.fetchUserProfile();
			String userName_prefix = userProfile.getFirstName().trim().toLowerCase()
			+ "_" + userProfile.getLastName().trim().toLowerCase();
	        String userName = this.findAvailableUserName(userName_prefix);
	        AppUser appUser = this.findAppUserByUserName(userName);
	        if (appUser != null) {
	            return appUser;
	        }
	        
	        String randomPassword = UUID.randomUUID().toString().substring(0, 5);
            System.out.println(randomPassword);
			System.out.println(userName);			
	        String encrytedPassword = EncrytedPasswordUtils.encrytePassword(randomPassword);
	        appUser = new AppUser();
	        appUser.setEnabled(true);
	        appUser.setEncrytedPassword("{bcrypt}"+encrytedPassword);
	        appUser.setUserName(userName);
	        //appUser.setEmail(email);
	        appUser.setFirstName(userProfile.getFirstName());
	        appUser.setLastName(userProfile.getLastName());
	        this.entityManager.persist(appUser);
	        // Create default Role
	        List<String> roleNames = new ArrayList<String>();
	        roleNames.add(AppRole.ROLE_USER);
			roleNames.add(AppRole.ROLE_ADMIN);
	        this.appRoleDAO.createRoleFor(appUser, roleNames);
	  
	        return appUser;
	    }
	    
	    public AppUser registerNewUserAccount(AppUserForm appUserForm, List<String> roleNames) {
	        AppUser appUser = new AppUser();
	        appUser.setUserName(appUserForm.getUserName());
	        appUser.setFirstName(appUserForm.getFirstName());
	        appUser.setLastName(appUserForm.getLastName());
	        appUser.setEnabled(true);
	        String encrytedPassword = EncrytedPasswordUtils.encrytePassword(appUserForm.getPassword());
	        appUser.setEncrytedPassword("{bcrypt}"+encrytedPassword);
	        this.entityManager.persist(appUser);
	        this.entityManager.flush();
			
	  
	        this.appRoleDAO.createRoleFor(appUser, roleNames);
	  
	        return appUser;
	    }

		public AppUser forgotPassword(AppUserForm appUserForm){
			AppUser user = findAppUserByUserName(appUserForm.getUserName());
			System.out.println(user.getUserName());
			String encrytedPassword = EncrytedPasswordUtils.encrytePassword(appUserForm.getPassword());
			user.setEncrytedPassword("{bcrypt}"+encrytedPassword);
			try{
			String sql = "update AppUser u set u.encrytedPassword = :enrytedpassword where u.userName = :userName";
			Query query = entityManager.createQuery(sql, AppUser.class);
            query.setParameter("encrytedpassword", user.getEncrytedPassword());
			query.setParameter("userName", user.getUserName());
			}catch(NoResultException e){
              return null;           
			}
			return user;

		}
}
