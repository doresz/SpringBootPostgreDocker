package com.talk2amareswaran.projects.socialloginapp.repository;

import com.talk2amareswaran.projects.socialloginapp.entity.UserRole;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    
    @Query("select u from UserRole u where user_id = ?1")
    public UserRole findUserRoleByAppUserId(Long id);
}