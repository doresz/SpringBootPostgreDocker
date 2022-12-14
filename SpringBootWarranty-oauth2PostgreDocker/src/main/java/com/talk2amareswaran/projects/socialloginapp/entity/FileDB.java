package com.talk2amareswaran.projects.socialloginapp.entity;

import java.io.Serializable;
import java.util.Arrays;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;



    @Entity
	@Table(name = "files")
	public class FileDB implements Serializable {
	  /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	@Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;
	  
	  private Long warantyId;

	  private String name;

	  private String type;

	  @Lob
	  private byte[] data;

	  public FileDB() {
	  }

	  public FileDB(String name, String type, byte[] data,long warantyId) {
	    this.name = name;
	    this.type = type;
	    this.data = data;
	    this.warantyId = warantyId;
	    
	    
	  }
	  public FileDB(String name, String type, byte[] data) {
		    this.name = name;
		    this.type = type;
		    this.data = data;
		       
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

	  public String getType() {
	    return type;
	  }

	  public void setType(String type) {
	    this.type = type;
	  }

	  public byte[] getData() {
	    return data;
	  }

	  public void setData(byte[] data) {
	    this.data = data;
	  }

	public Long getWarantyId() {
		return warantyId;
	}

	public void setWarantyId(Long warantyId) {
		this.warantyId = warantyId;
	}

	@Override
	public String toString() {
		return "FileDB [id=" + id + ", warantyId=" + warantyId + ", name=" + name + ", type=" + type + ", data="
				+ Arrays.toString(data) + "]";
	}
}