package test.Users;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import test.MovieLog.MovieRepository;

@RestController
@ApiOperation("User API")
public class AdminController extends ModeratorController {
	
	    @Autowired
	    UserRepository userRepository;

	    @Autowired
	    MovieRepository movieRepository;


	    private String success = "{\"message\":\"success\"}";
	    private String failure = "{\"message\":\"failure\"}";
	
	       
		@ApiOperation (value = "getAdmins", notes = "Gets all users with the admin role")
	    @GetMapping(path = "/role/admin")
	    List<User> getAdmins(){
	    	List<User> u =  this.getAllUsers();
	    	try{
	            ArrayList<User> adminRole = new ArrayList<User>();
	            for(int i = 0; i < u.size(); i++) {
	          		 if(u.get(i).getRole()== 2) {
	          			User user = null;
	          			user= u.get(i);
	          			adminRole.add(user);
	          		 }
	            }
	               return adminRole;            
	          }
	            catch (Exception e){
	                return null;
	            }
	    }
	    
		@ApiOperation (value = "updateUserRole", notes = "Update the role of a specified user")
	    @PutMapping("/userRole/{id}")
	    User updateUserRole(@PathVariable long id, @RequestBody User request){
	        User user = userRepository.findById(id); 
	        user.setRole(request.getRole());
	        userRepository.save(user);  
	        return user;
	    } 	    
	   
}