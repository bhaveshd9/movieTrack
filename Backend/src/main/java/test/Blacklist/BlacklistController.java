package test.Blacklist;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import test.Blacklist.*;


@RestController
public class BlacklistController {
	
	 private String success = "{\"message\":\"success\"}";
     private String failure = "{\"message\":\"failure\"}";
     
     @Autowired
     BlacklistRepository blacklistRepository;
     
     @GetMapping(path = "/blacklist")
     List<Blacklist> getAllBlacklistMovies(){
         return blacklistRepository.findAll();
     }
	 
	 @PostMapping(path= "/blacklist/{movieId}")
	 String addBlacklistMovie(@PathVariable int movieId) {
		 if(blacklistRepository.findByMovieId(movieId)== null) {
		     Blacklist bl= new Blacklist();
			 bl.setmovieId(movieId);
			 blacklistRepository.save(bl);
			  return success;
		 }
		 return failure;
	 
	 }

		
      @DeleteMapping(path = "/blacklist/{movieId}")
	    String deleteMovieFromBlacklist(@PathVariable int movieId){
    	  if(blacklistRepository.findByMovieId(movieId)== null) {
	        blacklistRepository.deleteByMovieId(movieId);
	        return success;
	    }
    	  return failure;
      }

}
