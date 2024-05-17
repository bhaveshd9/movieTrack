package test.Lists;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ListEntry implements Comparable<ListEntry> {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    private int movieID;
    private Integer listPosition;

    @ManyToOne
    @JoinColumn(name="list_id")
    @JsonIgnore
    private MovieList movieList;

    public ListEntry (){}

    public ListEntry (int movieID, int listPosition){
        this.movieID = movieID;
        this.listPosition = listPosition;
    }

    public ListEntry (int movieID){
        this.movieID = movieID;
        this.listPosition = null;
    }


    public int getMovieID(){
        return movieID;
    }

    public void setMovieID(int movieID){
        this.movieID = movieID;
    }

    public Integer getListPosition(){
        return listPosition;
    }

    public void setListPosition(int listPosition){
        this.listPosition = listPosition;
    }

    @JsonIgnore
    public MovieList getMovieList(){
        return movieList;
    }

    public void setMovieList(MovieList movieList){
        this.movieList = movieList;
    }

    @Override
    public int compareTo(ListEntry o) {
        return this.listPosition.compareTo(o.getListPosition());
    }
}
