package test.MovieLog;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiOperation;
import test.MovieAPI.MovieRoot;
import test.Users.User;
import test.Users.UserRepository;

@RestController
public class MovieLogController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    MovieRepository movieRepository;

    private String success = "{\"message\":\"success\"}";

	@ApiOperation (value = "getUsersMovieLogs", notes = "Gets all the logs that a user has made")
    @GetMapping(path = "logs/{userId}")
    ArrayList<MovieLogResult> getUsersMovieLogs(@PathVariable long userId){
        try{
        ArrayList<MovieLogResult> finalLogs = new ArrayList<MovieLogResult>();
        List<MovieLog> logs = movieRepository.findByUserId(userId);

        for (MovieLog log : logs){
            String url = "https://api.themoviedb.org/3/movie/" + log.getMovie() + "?api_key=aa3ea2bd5789771e9db49c3991824a9c&language=en-US";
            RestTemplate restTemplate = new RestTemplate();
            String res = restTemplate.getForObject(url,String.class);
            ObjectMapper om = new ObjectMapper();
            MovieRoot root = om.readValue(res, MovieRoot.class);
            MovieLogResult movieResult = new MovieLogResult(log, root);
            finalLogs.add(movieResult);
        }
        return finalLogs;
        }
        catch (Exception e){
            return null;
        }
    }

	@ApiOperation (value = "getUsersMovieLogsByDate", notes = "Gets all the logs that a user has made sorted by date")
    @GetMapping(path = "logsByDate/{userId}")
    ArrayList<MovieLogResult> getUsersMovieLogsByDate(@PathVariable long userId){
        try{
            ArrayList<MovieLogResult> finalLogs = new ArrayList<MovieLogResult>();
            List<MovieLog> logs = movieRepository.findByUserIdOrderByDateDesc(userId);
    
            for (MovieLog log : logs){
                String url = "https://api.themoviedb.org/3/movie/" + log.getMovie() + "?api_key=aa3ea2bd5789771e9db49c3991824a9c&language=en-US";
                RestTemplate restTemplate = new RestTemplate();
                String res = restTemplate.getForObject(url,String.class);
                ObjectMapper om = new ObjectMapper();
                MovieRoot root = om.readValue(res, MovieRoot.class);
                MovieLogResult movieResult = new MovieLogResult(log, root);
                finalLogs.add(movieResult);
            }
            return finalLogs;
            }
            catch (Exception e){
                return null;
            }
    }

	@ApiOperation (value = "getUsersMovieLogsByRating", notes = "Gets all the logs that a user has made sorted by rating")
    @GetMapping(path = "logsByRating/{userId}")
    ArrayList<MovieLogResult> getUsersMovieLogsByRating(@PathVariable long userId){
        try{
            ArrayList<MovieLogResult> finalLogs = new ArrayList<MovieLogResult>();
            List<MovieLog> logs = movieRepository.findByUserIdOrderByRatingDesc(userId);
    
            for (MovieLog log : logs){
                String url = "https://api.themoviedb.org/3/movie/" + log.getMovie() + "?api_key=aa3ea2bd5789771e9db49c3991824a9c&language=en-US";
                RestTemplate restTemplate = new RestTemplate();
                String res = restTemplate.getForObject(url,String.class);
                ObjectMapper om = new ObjectMapper();
                MovieRoot root = om.readValue(res, MovieRoot.class);
                MovieLogResult movieResult = new MovieLogResult(log, root);
                finalLogs.add(movieResult);
            }
            return finalLogs;
            }
            catch (Exception e){
                return null;
            }
    }

	@ApiOperation (value = "createLog", notes = "Creates a log for a specified user")
    @PostMapping(path = "/log/{userId}")
    String createLog(@PathVariable long userId, @RequestBody MovieLog movieLog){
        User user = userRepository.findById(userId);
        movieLog.setUser(user);
        movieRepository.save(movieLog);
        return success;
    }

	@ApiOperation (value = "getLogIfExists", notes = "Gets a log for a specific movie if it exists for that user")
    @GetMapping(path = "/hasLogged/{movieID}/{userID}")
    MovieLog getLogIfExists(@PathVariable int movieID, @PathVariable long userID){
        MovieLog log = movieRepository.findFirstByUserIdAndMovieIDOrderByDateDesc(userID, movieID);
        return log;
    }

    @ApiOperation (value = "deleteUser", notes = "Deletes a movie log with the specified id")
    @DeleteMapping(path = "/log/{id}")
    String deleteLog(@PathVariable long id){
        movieRepository.deleteById(id);
        return success;
    }
}
