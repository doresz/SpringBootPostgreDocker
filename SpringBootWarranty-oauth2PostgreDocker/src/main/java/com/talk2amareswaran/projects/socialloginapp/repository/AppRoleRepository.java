package com.talk2amareswaran.projects.socialloginapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.talk2amareswaran.projects.socialloginapp.entity.AppRole;
import org.springframework.data.jpa.repository.Query;

public interface AppRoleRepository extends JpaRepository<AppRole, Long> {
    
    @Query("select u from AppRole u where u.roleName = ?1")
    public AppRole findAppRoleByName(String roleName);
}