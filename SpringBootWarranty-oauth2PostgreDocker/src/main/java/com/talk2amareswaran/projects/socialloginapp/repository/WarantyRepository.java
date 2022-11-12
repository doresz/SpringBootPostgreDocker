package com.talk2amareswaran.projects.socialloginapp.repository;

import java.util.List;
import java.util.Optional;

import com.talk2amareswaran.projects.socialloginapp.entity.Waranty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



public interface WarantyRepository extends JpaRepository<Waranty,Long> {
	
	
@Query("select u from Waranty u where name = ?1")
Waranty findByName(String name);
@Query("select u from Waranty u where expiration_date<CURDATE() and foreign_key = ?1")
List<Waranty> findExpired(String foreign_key);
@Query("select u from Waranty u where expiration_date>=CURDATE() and foreign_key = ?1")
List<Waranty> findValidWarantys(String foreign_key);
/*@Query(
    "select u from Waranty u where TO_DATE(expiration_date,'YYYY-MM-DD')<=CURRENT_DATE() and foreign_key = ?1"
  )
  List<Waranty> findExpired(String foreign_key);

  @Query(
    "select u from Waranty u where TO_DATE(expiration_date,'YYYY-MM-DD')>CURRENT_DATE() and foreign_key = ?1"
  )
  List<Waranty> findValidWarantys(String foreign_key);*/
@Query(value = "select id from garancia order by id desc limit 1",nativeQuery = true)
public long findLastElement();
@Query("select u from Waranty u where foreign_key = ?1" )
List<Waranty> foreignKey(String foreignKey);
@Query("select u from Waranty u where location = ?1")
Optional<Waranty> findByLocation(String location);
@Query("select u from Waranty u where foreign_key = ?1")
public List<Waranty> findAllWarrantys(String foreign_key);
@Modifying
@Query("update Waranty u set u.name=:name where id = :id")
public void updateName(@Param("name")String name,Long id);
@Modifying
@Query("update Waranty u set u.location=:location where id = :id")
public void updateLocation(@Param("location")String location,Long id);
@Modifying
@Query("update Waranty u set u.expirationDate=:expirationdate where id = :id")
public void updateExpirationdate(@Param("expirationdate")String expirationdate,Long id);
}
