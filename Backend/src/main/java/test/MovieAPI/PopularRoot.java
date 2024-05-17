package test.MovieAPI;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PopularRoot{
    public int page;
    public ArrayList<PopularResult> results;
    public int total_pages;
    public int total_results;
}
