package test.Image;

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import test.Lists.ListEntry;
import test.Users.User;

@Entity
@Table(name="user_pictures")
public class Image {
	

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String url;
    
    
    
    public Image(String username, String url){
        this.username = username;
        this.url = url;
    }

    public Image() {}
    
    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }
    
    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url = url;
    }
    
}

