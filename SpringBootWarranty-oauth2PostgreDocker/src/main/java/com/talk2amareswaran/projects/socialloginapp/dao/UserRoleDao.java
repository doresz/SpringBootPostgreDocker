package com.talk2amareswaran.projects.socialloginapp.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;

import com.talk2amareswaran.projects.socialloginapp.entity.AppRole;
import com.talk2amareswaran.projects.socialloginapp.entity.UserRole;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

@Repository
@Transactional
public class UserRoleDao {
    
    @Autowired
    private EntityManager entityManager;

    public UserRole findUserRole(Long userId){
        try {
            String sql = "Select e from " + UserRole.class.getName() + " e " 
                    + " where e.appUser.userId = :userId ";
 
            Query query = this.entityManager.createQuery(sql, UserRole.class);
            query.setParameter("userId", userId);
            return (UserRole) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    public UserRole findAppRoleByUserId(Long id){
        String sql = "Select ur.appRole.roleId from " + UserRole.class.getName() + " ur " 
            + " where ur.appUser.userId = :userId ";
    Query query = this.entityManager.createQuery(sql, String.class);
    query.setParameter("userId", id);
    return (UserRole)query.getSingleResult();
    }
}