package test.MovieLog;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import test.MovieAPI.MovieRoot;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieLogResult {
    public MovieLog log;
    public MovieRoot movie;

    public MovieLogResult(MovieLog log, MovieRoot root){
        this.log = log;
        this.movie = root;
    }
}
