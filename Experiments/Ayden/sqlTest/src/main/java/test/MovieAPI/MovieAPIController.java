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

import test.MovieLog.MovieLog;
import test.MovieLog.MovieRepository;

@RestController
public class MovieAPIController {


    @GetMapping(path = "/getPopular")
    ArrayList<PopularResult> getPopular() throws JsonMappingException, JsonProcessingException{
        String url = "https://api.themoviedb.org/3/movie/popular?api_key=aa3ea2bd5789771e9db49c3991824a9c&language=en-US&page=1";

        RestTemplate restTemplate = new RestTemplate();

        String res = restTemplate.getForObject(url,String.class);

        ObjectMapper om = new ObjectMapper();
        PopularRoot root = om.readValue(res, PopularRoot.class);
       
        return root.results;
    }

    
}
