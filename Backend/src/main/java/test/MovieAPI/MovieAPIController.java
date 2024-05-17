package test.MovieAPI;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import test.Blacklist.*;
import test.MovieLog.MovieLog;
import test.MovieLog.MovieRepository;

@RestController
public class MovieAPIController {
	
    
    @ApiOperation (value = "getPopular", notes = "Gets a list of movies that are currently popular")
    @GetMapping(path = "/getPopular")
    ArrayList<PopularResult> getPopular() throws JsonMappingException, JsonProcessingException{
        String url = "https://api.themoviedb.org/3/movie/popular?api_key=aa3ea2bd5789771e9db49c3991824a9c&language=en-US&page=1";

        RestTemplate restTemplate = new RestTemplate();

        String res = restTemplate.getForObject(url,String.class);

        ObjectMapper om = new ObjectMapper();
        PopularRoot root = om.readValue(res, PopularRoot.class);
       
        return root.results;
    }

    @ApiOperation (value = "getUpcoming", notes = "Gets a list of movies that release in the near future")
    @GetMapping(path = "/getUpcoming")
    ArrayList<PopularResult> getUpcoming() throws JsonMappingException, JsonProcessingException{
        String url = "https://api.themoviedb.org/3/movie/upcoming?api_key=aa3ea2bd5789771e9db49c3991824a9c&language=en-US&page=1";

        RestTemplate restTemplate = new RestTemplate();

        String res = restTemplate.getForObject(url,String.class);

        ObjectMapper om = new ObjectMapper();
        PopularRoot root = om.readValue(res, PopularRoot.class);
       
        return root.results;
    }

    @ApiOperation (value = "getMovie", notes = "Gets a specific movie specified by the id")
    @GetMapping(path = "/getMovie/{id}")
    MovieRoot getMovie(@PathVariable int id) throws JsonMappingException, JsonProcessingException{
        String url = "https://api.themoviedb.org/3/movie/" + id + "?api_key=aa3ea2bd5789771e9db49c3991824a9c&language=en-US&append_to_response=credits";

        RestTemplate restTemplate = new RestTemplate();

        String res = restTemplate.getForObject(url,String.class);

        ObjectMapper om = new ObjectMapper();
        MovieRoot root = om.readValue(res, MovieRoot.class);
       
        return root;
    }

    @ApiOperation (value = "searchMovie", notes = "Returns a list of movies that match the given query")
    @GetMapping(path = "/searchMovie/{term}")
    PopularRoot searchMovie(@PathVariable String term) throws JsonMappingException, JsonProcessingException{
        String url = "https://api.themoviedb.org/3/search/movie?api_key=aa3ea2bd5789771e9db49c3991824a9c&language=en-US&query=" + term + "&page=1&include_adult=false";

        RestTemplate restTemplate = new RestTemplate();

        String res = restTemplate.getForObject(url,String.class);

        ObjectMapper om = new ObjectMapper();
        PopularRoot root = om.readValue(res, PopularRoot.class);

        return root;
    }

    @ApiOperation (value = "searchMoviePage", notes = "Returns a list of movies that match the given query, with pagination")
    @GetMapping(path = "/searchMovie/{term}/{page}")
    PopularRoot searchMoviePage(@PathVariable String term, @PathVariable int page) throws JsonMappingException, JsonProcessingException{
        String url = "https://api.themoviedb.org/3/search/movie?api_key=aa3ea2bd5789771e9db49c3991824a9c&language=en-US&query=" + term + "&page=" + page + "&include_adult=false";

        RestTemplate restTemplate = new RestTemplate();

        String res = restTemplate.getForObject(url,String.class);

        ObjectMapper om = new ObjectMapper();
        PopularRoot root = om.readValue(res, PopularRoot.class);
       
        return root;
    }

    @ApiOperation (value = "getSimilarMovies", notes = "Returns a list of movies that are similar to the given movie")
    @GetMapping(path = "/getSimilar/{id}")
    ArrayList<PopularResult> getSimilarMovies(@PathVariable int id) throws JsonMappingException, JsonProcessingException{
        String url = "https://api.themoviedb.org/3/movie/" + id + "/similar?api_key=aa3ea2bd5789771e9db49c3991824a9c&language=en-US&append_to_response=credits";

        RestTemplate restTemplate = new RestTemplate();

        String res = restTemplate.getForObject(url,String.class);

        ObjectMapper om = new ObjectMapper();
        PopularRoot root = om.readValue(res, PopularRoot.class);
       
        return root.results;
    }

}
