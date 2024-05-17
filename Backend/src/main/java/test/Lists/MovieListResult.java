package test.Lists;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import test.MovieAPI.MovieRoot;


public class MovieListResult {
    public ListEntry listEntry;
    public MovieRoot movie;

    public MovieListResult(ListEntry listEntry, MovieRoot root){
        this.listEntry = listEntry;
        this.movie = root;
    }
}
