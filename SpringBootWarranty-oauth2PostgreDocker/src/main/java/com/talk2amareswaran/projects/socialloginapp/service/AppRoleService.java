package com.talk2amareswaran.projects.socialloginapp.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.talk2amareswaran.projects.socialloginapp.entity.AppRole;
import com.talk2amareswaran.projects.socialloginapp.entity.AppUser;
import com.talk2amareswaran.projects.socialloginapp.entity.UserRole;
import com.talk2amareswaran.projects.socialloginapp.repository.AppRoleRepository;
import com.talk2amareswaran.projects.socialloginapp.repository.UserRoleRepository;

@Service
public class AppRoleService {

    @Value("${spring.data.cassandra.username}")
    private String admin;
    @Autowired AppRoleRepository appRoleRepo;
    @Autowired UserRoleRepository userRoleRepo;

    public void createRoleFor(AppUser appUser){
        AppRole role = new AppRole();
        AppRole role2 = new AppRole();
        UserRole userRole = new UserRole();  
        List<AppRole> appRoleList = appRoleRepo.findAll();
        if(appRoleList.isEmpty()){
        role.setRoleName(AppRole.ROLE_ADMIN);
        AppRole admin2 = appRoleRepo.save(role);
        role2.setRoleName(AppRole.ROLE_USER);
        AppRole user = appRoleRepo.save(role2);
        if(appUser.getUserName().equals(admin)){
          userRole.setAppRole(admin2);
          userRole.setAppUser(appUser);
          userRoleRepo.save(userRole);
        }
        else{
          userRole.setAppRole(user);
          userRole.setAppUser(appUser);
          userRoleRepo.save(userRole);
        }
        }
        else{
        if(appUser.getUserName().equals(admin)){
        role = appRoleRepo.findAppRoleByName(AppRole.ROLE_ADMIN);  
      }           
        else{
        role = appRoleRepo.findAppRoleByName(AppRole.ROLE_USER); 
        }
      }           
        userRole.setAppRole(role);
        userRole.setAppUser(appUser);
        userRoleRepo.save(userRole);
}
}
        
    
            


