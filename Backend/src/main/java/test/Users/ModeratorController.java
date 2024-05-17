package test.Users;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiOperation;
import test.Lists.MovieList;
import test.MovieAPI.MovieAPIController;
import test.MovieAPI.MovieRoot;
import test.MovieAPI.PopularRoot;
import test.MovieLog.MovieLog;
import test.MovieLog.MovieLogResult;
import test.MovieLog.MovieRepository;
import test.Lists.ListRepository;

public class ModeratorController extends UserController {

	 @Autowired
	    UserRepository userRepository;

	    @Autowired
	    MovieRepository movieRepository;
	    
	    @Autowired
	    ListRepository listRepository;

	    @Autowired
	    private PasswordEncoder passwordEncoder;


	    private String success = "{\"message\":\"success\"}";
	    private String failure = "{\"message\":\"failure\"}";
	
	    
		@ApiOperation (value = "getUserRole", notes = "Gets all users with the user role")
		@GetMapping(path = "/role/user")
	    List<User> getUserRole(){
	    	List<User> u =  this.getAllUsers();
	    	try{
	            ArrayList<User> userRole = new ArrayList<User>();
	            for(int i = 0; i < u.size(); i++) {
	          		 if(u.get(i).getRole()== 0) {
	          			User user = null;
	          			user= u.get(i);
	          			userRole.add(user);
	          		 }
	            }
	               return userRole;            
	          }
	            catch (Exception e){
	                return null;
	            }
	    }
	    
		@ApiOperation (value = "getModerators", notes = "Gets all users with the mod role")
	    @GetMapping(path = "/role/mod")
	    List<User> getModerators(){
	    	List<User> u =  this.getAllUsers();
	    	try{
	            ArrayList<User> modRole = new ArrayList<User>();
	            for(int i = 0; i < u.size(); i++) {
	          		 if(u.get(i).getRole()== 1) {
	          			User user = null;
	          			user= u.get(i);
	          			modRole.add(user);
	          		 }
	            }
	               return modRole;            
	          }
	            catch (Exception e){
	                return null;
	            }
	    }
		
		@GetMapping(path = "/users/lists")
	    List<MovieList> getAllLists(){
			return listRepository.findAll();	
	    }
		
		@GetMapping(path="/allLogs")
	    public List<MovieLog> showAllLogs(){
			return movieRepository.findAll();
	    }
		
		@GetMapping(path="/users/logs")
		public List<Feed> showUsersLogs(){
			List<User> m = this.getAllUsers();
		      ArrayList<Long> ids = new ArrayList<>();
		      for (User f: m){
		        ids.add(f.getId());
		      }
		      List<MovieLog> logs = movieRepository.findTop40ByUserIdInOrderByDateDesc(ids);
			  List<Feed> feed = new ArrayList<Feed>();
			  for (MovieLog log: logs){
				for (User u : m){
					if (log.getUser().getId() == u.getId()){
						Feed f = new Feed();
						f.user = u;
						f.log = log;
						feed.add(f);
						break;
					}
				}
			  }
			  return feed;			
		}
	    
		@ApiOperation (value = "updateUserPass", notes = "Used to update a users password")
	    @PutMapping("/userPassword/{id}")
	    User updateUserPass(@PathVariable long id, @RequestBody User request){
	        User user = userRepository.findById(id); 
	        user.setPassword(request.getPassword());
	        userRepository.save(user);  
	        return user;
	    } 
	    
	    
}
