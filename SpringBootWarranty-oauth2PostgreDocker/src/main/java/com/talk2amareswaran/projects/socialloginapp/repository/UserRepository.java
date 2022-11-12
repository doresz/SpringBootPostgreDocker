package com.talk2amareswaran.projects.socialloginapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
//import org.springframework.stereotype.Repository;

import com.talk2amareswaran.projects.socialloginapp.entity.AppUser;



//Spring data JPA repository, the implementation will be
//generated at runtime by Spring Data JPA
//gyakorlatilag ez az interface t�rolja a User adatait JpaRepository-n kereszt�l

//@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
	@Query("SELECT u from AppUser u WHERE u.userName = ?1")
	AppUser findByEmail(String userName);
	@Query("SELECT u from AppUser u WHERE u.userName = ?1")
	Optional<AppUser> findByEmail2(String userName);

	@Query("SELECT u from AppUser u WHERE u.userName = ?1 and u.encrytedPassword=?2")
	AppUser findUser(String userName, String password);

	@Query("select u.userId from AppUser u where userId = ?1")
	int findUserId(AppUser u);

	@Modifying
	@Query("update AppUser u set u.encrytedPassword = :password where u.userName = :username")
	public void update(@Param("username") String username, @Param("password") String password);

	@Query("select u.userName from AppUser u where u.userName= ?1")
	public AppUser existEmail(String email);
	
    
	
}