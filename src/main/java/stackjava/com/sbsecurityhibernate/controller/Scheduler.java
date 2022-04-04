package stackjava.com.sbsecurityhibernate.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import stackjava.com.sbsecurityhibernate.dao.UserDAO;
import stackjava.com.sbsecurityhibernate.entities.uploadFile;

@Component
public class Scheduler {
	@Autowired
	private UserDAO userDAO;
	@Scheduled(cron = "0 1 1 * * ?")
	   public void cronJobSch() {
		try {
			String home = System.getProperty("user.home");
			List<uploadFile> getAllUploadFileClear=userDAO.getAllUploadFileClear();
			List<Integer> ids = new ArrayList<Integer>();
			
			if(!getAllUploadFileClear.isEmpty()) {
				for (uploadFile uploadFile : getAllUploadFileClear) {
					String uploadDir = home + "/Downloads/" + uploadFile.getUsername() + "/"+uploadFile.getName();
					Path uploadPath = Paths.get(uploadDir);
					 Files.deleteIfExists(uploadPath);
					 ids.add(uploadFile.getId());
				}
			} 
			userDAO.UpdateClearUploadFileFromlistID(ids);
				
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
}
