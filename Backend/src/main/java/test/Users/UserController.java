package test.Users;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiOperation;
import test.MovieAPI.MovieAPIController;
import test.MovieAPI.PopularRoot;
import test.MovieLog.MovieLog;
import test.MovieLog.MovieLogResult;
import test.MovieLog.MovieRepository;

public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";
    
	@ApiOperation (value = "getAllUsers", notes = "Returns a list of all the users")
    @GetMapping(path = "/users")
    List<User> getAllUsers(){
        return userRepository.findAll();
    }

	@ApiOperation (value = "getUserById", notes = "Get a specific user by id")
    @GetMapping(path = "/users/{id}")
    User getUserById( @PathVariable long id){
        return userRepository.findById(id);
    }

	@ApiOperation (value = "createUser", notes = "Creates a new user")
    @PostMapping(path = "/users")
    User createUser(@RequestBody User user){
        if (user == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request");
        userRepository.save(user);
        return user;
    }

	@ApiOperation (value = "updateUser", notes = "Updates a user with the specified id")
    @PutMapping("/users/{id}")
    User updateUser(@PathVariable long id, @RequestBody User request){
        User user = userRepository.findById(id); 
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setRole(request.getRole());
        userRepository.save(user);  
        return user;
    }  

	@ApiOperation (value = "deleteUser", notes = "Deletes a user with the specified id")
    @DeleteMapping(path = "/users/{id}")
    String deleteUser(@PathVariable long id){
        userRepository.deleteById(id);
        return success;
    }

	
	@ApiOperation (value = "loginUser", notes = "Used to login a user, returns the user object if the credentials are correct")
    @GetMapping("/login/{username}/{password}")
    User loginUser(@PathVariable String username, @PathVariable String password){
        User user = userRepository.findByUsername(username);
        if (user != null){
            Boolean loggedIn = passwordEncoder.matches(password, user.getPassword());
            if (loggedIn){
                return user;
            }
        }
        throw new ResponseStatusException(
            HttpStatus.NOT_FOUND, "User not found");
    }
    
	@ApiOperation (value = "searchUser", notes = "Used to search for a user by username")
    @GetMapping(path = "/searchUser/{term}")
    ArrayList<User> searchUser(@PathVariable String term){
    	List<User> u =  this.getAllUsers();
		term = term.toLowerCase();
    	try{
            ArrayList<User> usersFound = new ArrayList<User>();
            for(int i = 0; i < u.size(); i++) {
          		 if(u.get(i).getUsername().toLowerCase().contains(term)) {
          			User user = null;
          			user= u.get(i);
          			usersFound.add(user);
          		 }
            }
               return usersFound;            
          }
            catch (Exception e){
                return null;
            }
    }
    
	@ApiOperation (value = "sendFriendsRequest", notes = "Sends a friend request to a specific user")
    @PostMapping(path = "/users/{id}/request")
    String sendFriendsRequest(@PathVariable int id, @RequestBody User user) {
  	  User requester = userRepository.findById(id);
  	  User potential_friend = checkUserExist(user.getUsername());
  	  if(requester.getPotentialFriends().contains(potential_friend)){
  		  return failure;
  	  }
  	  if(requester != null && potential_friend != null) {
  		  Set<User> requesters = potential_friend.getPotentialFriends();
  		  requesters.add(requester);
  		  potential_friend.setPotentialFriends(requesters);
  		  userRepository.save(potential_friend);

		  Set<User> friendRequests = requester.getFriendsRequest();
		  friendRequests.add(potential_friend);
		  requester.setFriendsRequest(friendRequests);
		  userRepository.save(requester);

  		  return success;
  	  }
  	  else {
  		  return failure;
  	  }
    }

    
	@ApiOperation (value = "approveFriendsRequest", notes = "Approves/Accepts a friend request")
    @PostMapping(path = "/users/{id}/approveRequest/{req}")
    String approveFriendsRequest(@PathVariable int id, @PathVariable int req, @RequestBody User user) {
  	  User responser = userRepository.findById(id);
  	  User potential_friend = checkUserExist(user.getUsername());
  	  if(responser == null || potential_friend == null) {
  		  return failure;
  	  }
  	  else {
  	  Set<User> requesters = responser.getPotentialFriends();
	  Set<User> responders = potential_friend.getFriendsRequest();

  	  if(req == 0) {
  		 
  		 if(!requesters.contains(potential_friend)) {
  			 return failure;
  		 }
  		 else {
  			 requesters.remove(potential_friend);
  			 responser.setPotentialFriends(requesters);
  			 userRepository.save(responser);

			 responders.remove(responser);
			 potential_friend.setFriendsRequest(responders);
			 userRepository.save(potential_friend);
  			 return success;
  		 }
  	  }
  	  else if(req == 1){
  		  requesters.remove(potential_friend);
  		  responser.setPotentialFriends(requesters);
		  responders.remove(responser);
		  potential_friend.setFriendsRequest(responders);
  		  Set<User>friends = responser.getFriends();
  		  friends.add(potential_friend);
  		  responser.setFriends(friends);
  		  userRepository.save(responser);
  		  friends = potential_friend.getFriends();
  		  friends.add(responser);
  		  potential_friend.setFriends(friends);
  		  userRepository.save(potential_friend);
  		  return success;
  		  
  	  }
  	  else {
  		  return failure;
  	  }
  	  }
    }


	@ApiOperation (value = "disconnect", notes = "Removes a friend from your friend list")
    @PostMapping(path="/users/{id}/disconnect")
    String disconnect(@PathVariable int id, @RequestBody User req) {
  	  User requester = userRepository.findById(id);
  	  User outdated_friend = checkUserExist(req.getUsername());
  	  if(requester == null || outdated_friend == null) {
  		  return failure;
  	  }
  	  else {
  		  Set<User>friends = requester.getFriends();
  		 if(!friends.contains(outdated_friend)) {
  			 return failure;
  		 }
  		 else {
  			 friends.remove(outdated_friend);
  			 requester.setFriends(friends);
  			 userRepository.save(requester);
  			 friends = outdated_friend.getFriends();
  			 friends.remove(requester);
  			 outdated_friend.setFriends(friends);
  			 userRepository.save(outdated_friend);
  			 return success;
  		 }
  	  }
    }

	@ApiOperation (value = "showFriends", notes = "Shows a users friend list")
    @GetMapping(path="/users/{id}/friends")
    public Set<User> showFriends(@PathVariable int id){
  	  User user = userRepository.findById(id);
  	  return user.getFriends();
    }
    
	@ApiOperation (value = "showPotentialFriends", notes = "Shows the friend requests that a user has sent")
    @GetMapping(path="/users/{id}/potential_friends")
    public Set<User>showPotentialFriends(@PathVariable int id){
  	  User user = userRepository.findById(id);
  	  return user.getPotentialFriends();
    }

	@ApiOperation (value = "showFriendsRequests", notes = "Shows the friend requests that a user has received")
	@GetMapping(path="/users/{id}/friend_requests")
    public Set<User> showFriendsRequests(@PathVariable int id){
  	  User user = userRepository.findById(id);
  	  return user.getFriendsRequest();
    }

	@GetMapping(path="/feed/{userId}")
    public List<Feed> showFriendsLogs(@PathVariable long userId){
      User user = userRepository.findById(userId);
      Set<User> friends = user.getFriends();
      ArrayList<Long> ids = new ArrayList<>();
      for (User f: friends){
        ids.add(f.getId());
      }
      List<MovieLog> logs = movieRepository.findTop40ByUserIdInOrderByDateDesc(ids);
	  List<Feed> feed = new ArrayList<Feed>();
	  for (MovieLog log: logs){
		for (User u : friends){
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

    
    public User checkUserExist(String username) {
   	 List<User> u =  this.getAllUsers(); 
   	 User user = null;  
   	 for(int i = 0; i < u.size(); i++) {
   		 if(u.get(i).getUsername().equals(username)) {
   			 user= u.get(i); 
   			 break; 
   		 }
   	 }
   	 return user; 
     }
}
