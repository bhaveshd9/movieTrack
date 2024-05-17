package test.Image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import test.Users.User;
import test.Users.UserRepository;
@RestController
public class ImageController {
	
	 private String success = "{\"message\":\"success\"}";
	private String failure = "{\"message\":\"failure\"}";
	
	@Autowired
    UserRepository userRepository;
	@Autowired
    ImageRepository imageRepository;
	
	 @GetMapping(path = "/image/{username}")
	    Image getImageByUsername( @PathVariable String username){
	        return imageRepository.findImageByUsername(username);
	    }
	 
	 @PostMapping(path= "/image/{username}/upload")
	 String uploadImage(@PathVariable String username,@RequestBody Image image) {
		 if(userRepository.findByUsername(username)!= null) {
		     Image ima= new Image();
			 ima.setUsername(username);
			 ima.setUrl(image.getUrl());
			 imageRepository.save(ima);
			  return success;
		 }
		 return failure;
	 
	 }
	 
	 @PutMapping("/image/{id}")
	   String updateImage(@PathVariable long id, @RequestBody Image image){
	        Image ima = imageRepository.findById(id); 
	        ima.setUrl(image.getUrl()); 
	        imageRepository.save(ima);
	        return success;
	    }  

		
     @DeleteMapping(path = "/image/{id}")
	    String deleteImage(@PathVariable long id){
    	 if(imageRepository.findById(id)!= null) {
	        imageRepository.deleteById(id);
	        return success;
	    }
    	 return failure;
     }


}
