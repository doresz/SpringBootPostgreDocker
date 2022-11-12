package com.talk2amareswaran.projects.socialloginapp.repository;

import java.util.List;
import java.util.Optional;

import com.talk2amareswaran.projects.socialloginapp.entity.FileDB;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface FileRepository extends JpaRepository<FileDB,Long> {
@Query("select u from FileDB u where waranty_id is null")
List<FileDB>idNull();
@Query("select u from FileDB u where waranty_id = ?1")
Optional<FileDB> getFileWarrantyId(long warranty);
@Query("select u from FileDB u where waranty_id = ?1")
List<FileDB>getImage(long warantyId);
@Query("select u from FileDB u where name = ?1")
Optional<FileDB>findByName(String name);
@Query("select u from FileDB u where waranty_id = ?1")
List<FileDB> findFileByWarantyId(Long warantyId);
@Query("select u from FileDB u where waranty_id = ?1")
List<FileDB> findFileDB(Long warantyId);
}
