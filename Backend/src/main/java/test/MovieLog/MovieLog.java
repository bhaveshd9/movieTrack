package test.MovieLog;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import test.Users.User;


@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "movieid" }) })
public class MovieLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int movieID;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;
    private Date date;
    private double rating;

    public MovieLog(int movie, User user) {
        this.movieID = movie;
        this.user = user;
        this.date = new Date();
    }

    public MovieLog(int movie, User user, double rating) {
        this.movieID = movie;
        this.user = user;
        this.date = new Date();
        this.rating = rating;
    }

    public MovieLog() {
        this.date = new Date();
    }

    public MovieLog(int movie) {
        this.movieID = movie;
        this.date = new Date();
    }

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public int getMovie(){
        return movieID;
    }

    public void setMovie(int movie){
        this.movieID = movie;
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

    public double getRating(){
        return rating;
    }

    public void setRating(double r){
        this.rating = r;
    }

    
}
