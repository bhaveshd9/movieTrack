package test.MovieAPI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie {
    public int id;
    public String title;

    Movie(){}

    int getId(){
        return id;
    }

    void setId(int id){
        this.id =id;
    }

    String getTitle(){
        return title;
    }

    void setTitle(String title){
        this.title =title;
    }
}
