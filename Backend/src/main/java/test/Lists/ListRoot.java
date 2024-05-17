package test.Lists;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ListRoot {
    
    public ArrayList<MovieListResult> moviesInList;
    public MovieList list;

    public ListRoot(MovieList list, ArrayList<MovieListResult> movies){
        this.list = list;
        moviesInList = movies;
    }
}
