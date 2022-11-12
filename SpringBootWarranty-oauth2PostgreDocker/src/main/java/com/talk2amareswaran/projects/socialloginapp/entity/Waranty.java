package com.talk2amareswaran.projects.socialloginapp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="garancia")
public class Waranty {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
     
    @Column(name = "name", nullable = true,length = 45)//(unique = true) azt jelenti h ugyanazon name-ből csak egy lehet
    private String name;
     
    @Column(name = "location",nullable = true, length = 64)
    private String location;
     
    @Column(name = "expirationDate", nullable = true, length = 20)
    private String expirationDate;
     
    @Column(name = "expired", nullable = true, length = 20)
    private String expired;
    @Column(name = "foreign_key", nullable = true, length = 20)
    private String foreignKey;
  

	public Waranty(){

	}
	public Waranty(String expirationDate,String expired,String foreignKey,String location,String name){
		this.expirationDate = expirationDate;
		this.expired = expired;
		this.foreignKey = foreignKey;
		this.location = location;
		this.name = name;
	}
	public String getForeignKey() {
		return foreignKey;
	}

	public void setForeignKey(String foreignKey) {
		this.foreignKey = foreignKey;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getExpired() {
		return expired;
	}

	public void setExpired(String expired) {
		this.expired = expired;
	}
}
