package test.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import test.Users.User;

@Entity
public class MovieList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    private String listName;

    @OneToMany(mappedBy = "movieList", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ListEntry> listEntries;

    public MovieList (){}

    public MovieList(User user, String listName){
        this.user = user;
        this.listName = listName;
        listEntries = new ArrayList<ListEntry>();
    }

    public MovieList(String listName){
        this.listName = listName;
        listEntries = new ArrayList<ListEntry>();
    }

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public String getListName(){
        return listName;
    }

    public void setListName(String listName){
        this.listName = listName;
    }

    public List<ListEntry> getListEntries(){
        if(listEntries != null){
            Collections.sort(listEntries);
        }
        return listEntries;
    }

    public void setListEntries(List<ListEntry> listEntries){
        this.listEntries = listEntries;
    }

    public void setNewListEntries(Set<ListEntry> listEntries){
        this.listEntries.clear();
        if (listEntries != null) {
            this.listEntries.addAll(listEntries);
        }
    }
}
