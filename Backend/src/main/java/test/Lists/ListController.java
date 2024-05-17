package test.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
public class ListController {
    @Autowired
    ListRepository listRepository;

    @Autowired
    UserRepository userRepository;

    @ApiOperation (value = "getUsersLists", notes = "Returns a list of movie lists that the user has created")
    @GetMapping(path = "lists/{userId}")
    List<MovieList> getUsersLists(@PathVariable long userId){
        return listRepository.findByUserId(userId);
    }

    @ApiOperation (value = "createList", notes = "Creates a movie list for a specified user")
    @PostMapping(path = "/list/{userId}")
    MovieList createList(@PathVariable long userId, @RequestBody MovieList movieList){
        if(movieList.getListEntries() == null){
            movieList.setListEntries(new ArrayList<ListEntry>());
        }
        User user = userRepository.findById(userId);
        movieList.setUser(user);
        listRepository.save(movieList);
        return movieList;
    }

    @ApiOperation (value = "addEntryToList", notes = "Adds a movie entry to the specified list")
    @PostMapping(path = "/addEntry/{listId}")
    MovieList addEntryToList(@PathVariable long listId, @RequestBody ListEntry newEntry){
        MovieList movieList = listRepository.findById(listId);
        List<ListEntry> entries = movieList.getListEntries();
        newEntry.setMovieList(movieList);

        if(newEntry.getListPosition() == null){
            newEntry.setListPosition(entries.size()+1);
        }

        entries.add(newEntry);
        movieList.setListEntries(entries);
        listRepository.save(movieList);
        return movieList;
    }

    @ApiOperation (value = "setListEntreis", notes = "Sets the entries of a specified list")
    @PostMapping(path = "/setEntries/{listId}")
    MovieList setListEntreis(@PathVariable long listId, @RequestBody Set<ListEntry> newEntries){
        MovieList movieList = listRepository.findById(listId);
        for (ListEntry entry : newEntries){
            entry.setMovieList(movieList);
        }
        movieList.setNewListEntries(newEntries);
        listRepository.save(movieList);
        return movieList;
    }

    @ApiOperation (value = "deleteEntryFromList", notes = "Deletes a specific entry from the list")
    @DeleteMapping(path = "/deleteEntry/{listId}/{listPosition}")
    MovieList deleteEntryFromList(@PathVariable long listId, @PathVariable int listPosition){
        MovieList movieList = listRepository.findById(listId);
        List<ListEntry> entries = movieList.getListEntries();
        entries.removeIf(e -> e.getListPosition() == listPosition);
        entries.forEach(e -> {if(e.getListPosition()>listPosition) {
            e.setListPosition(e.getListPosition() - 1);
        }});
        movieList.setListEntries(entries);
        listRepository.save(movieList);
        return movieList;
    }

    @ApiOperation (value = "renameList", notes = "Used to rename a list")
    @PostMapping(path = "/renameList/{listId}/{newName}")
    MovieList renameList(@PathVariable long listId, @PathVariable String newName){
        MovieList movieList = listRepository.findById(listId);
        movieList.setListName(newName);
        listRepository.save(movieList);
        return movieList;
    }

    @ApiOperation (value = "deleteList", notes = "Used to delete a list and all of its entries")
    @DeleteMapping(path = "deleteList/{listId}")
    String deleteList (@PathVariable long listId){
        listRepository.deleteById(listId);
        return "{\"message\":\"success\"}";
    }


    @ApiOperation (value = "getListById", notes = "Gets a specific list by id")
    @GetMapping(path = "list/{listId}")
    ListRoot getListById(@PathVariable long listId){
        MovieList movieList = listRepository.findById(listId);
        try{
            ArrayList<MovieListResult> moviesInList = new ArrayList<MovieListResult>();
    
            for (ListEntry log : movieList.getListEntries()){
                String url = "https://api.themoviedb.org/3/movie/" + log.getMovieID() + "?api_key=aa3ea2bd5789771e9db49c3991824a9c&language=en-US";
                RestTemplate restTemplate = new RestTemplate();
                String res = restTemplate.getForObject(url,String.class);
                ObjectMapper om = new ObjectMapper();
                MovieRoot root = om.readValue(res, MovieRoot.class);
                MovieListResult toAdd = new MovieListResult(log, root);
                moviesInList.add(toAdd);
            }
            ListRoot resp = new ListRoot(movieList, moviesInList);
            return resp;
            }
            catch (Exception e){
                return null;
            }
    }

}
