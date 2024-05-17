package test.MovieLog;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import test.Users.User;


@Entity
public class MovieLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String movie;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;
    private Date date;
    private int rating;

    public MovieLog(String movie, User user) {
        this.movie = movie;
        this.user = user;
        this.date = new Date();
    }

    public MovieLog(String movie, User user, int rating) {
        this.movie = movie;
        this.user = user;
        this.date = new Date();
        this.rating = rating;
    }

    public MovieLog() {
        this.date = new Date();
    }

    public MovieLog(String movie) {
        this.movie = movie;
        this.date = new Date();
    }

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getMovie(){
        return movie;
    }

    public void setMovie(String movie){
        this.movie = movie;
    }

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public void setDate(Date date){
        this.date = date;
    }

    public Date getDate(){
        return date;
    }

    public int getRating(){
        return rating;
    }

    public void setRating(int r){
        this.rating = r;
    }

    
}
