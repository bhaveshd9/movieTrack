package test.Users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import test.MovieLog.MovieLog;
import test.MovieLog.MovieRepository;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MovieRepository movieRepository;


    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/users")
    List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping(path = "/users/{id}")
    User getUserById( @PathVariable long id){
        return userRepository.findById(id);
    }

    @GetMapping(path = "logs/{userId}")
    List<MovieLog> getUsersMovieLogs(@PathVariable long userId){
        return movieRepository.findByUserId(userId);
    }

    @PostMapping(path = "/users")
    User createUser(@RequestBody User user){
        if (user == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request");
        userRepository.save(user);
        return user;
    }

    @PostMapping(path = "/log/{userId}")
    String createLog(@PathVariable long userId, @RequestBody MovieLog movieLog){
        User user = userRepository.findById(userId);
        movieLog.setUser(user);
        movieRepository.save(movieLog);
        return success;
    }

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

    @DeleteMapping(path = "/users/{id}")
    String deleteUser(@PathVariable long id){
        userRepository.deleteById(id);
        return success;
    }

    @DeleteMapping(path = "/log/{id}")
    String deleteLog(@PathVariable long id){
        movieRepository.deleteById(id);
        return success;
    }

    @GetMapping("/login/{username}/{password}")
    User loginUser(@PathVariable String username, @PathVariable String password){
        User user = userRepository.findByUsernameAndPassword(username, password);
        if (user != null){
            return user;
        }
        throw new ResponseStatusException(
            HttpStatus.NOT_FOUND, "User not found");
    }
}
