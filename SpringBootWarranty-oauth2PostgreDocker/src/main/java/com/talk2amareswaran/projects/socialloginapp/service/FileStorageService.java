package com.talk2amareswaran.projects.socialloginapp.service;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;

import com.talk2amareswaran.projects.socialloginapp.entity.FileDB;
import com.talk2amareswaran.projects.socialloginapp.repository.FileRepository;
import com.talk2amareswaran.projects.socialloginapp.repository.WarantyRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;



@Service
public class FileStorageService {
  @Autowired
  private FileRepository fileDBRepository;
  @Autowired
  private WarantyRepository warantyRepo;

  public void store(@RequestParam("file")MultipartFile[] file,long id) throws IOException {	
	FileDB db = null;
  if (null != file && file.length > 0){
	for(MultipartFile e : file) {
		String fileName = StringUtils.cleanPath(e.getOriginalFilename());
		db = new FileDB(fileName, e.getContentType(), e.getBytes(),id);
		fileDBRepository.save(db);
	}
}

}
    
  
  public void updateStore(MultipartFile[] file,@RequestParam("id")Long id)throws IOException{
	  FileDB db = null;
		for(MultipartFile e : file) {
			String fileName = StringUtils.cleanPath(e.getOriginalFilename());
			db = new FileDB(fileName, e.getContentType(), e.getBytes(),id);
			fileDBRepository.save(db);
		}
  }
  

  public Optional<FileDB> getFile(Long id) {
      return fileDBRepository.findById(id);
              
  }
  public Optional<FileDB> getFileWarranty(Long id) {
      return fileDBRepository.getFileWarrantyId(id);
              
  }
  public List <FileDB> getAllActiveImages() {
		return fileDBRepository.findAll();
	}
  
  
  public Stream<FileDB> getAllFiles() {
    return fileDBRepository.findAll().stream();
  }


}