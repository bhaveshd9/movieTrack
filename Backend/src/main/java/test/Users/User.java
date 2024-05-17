package test.Users;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = 
    { "username", "password" }), @UniqueConstraint(columnNames = 
    { "username"}) })
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;
    private String lastName;
    private String password;
    private String username;
    private int role;
    
    @ManyToMany(cascade={CascadeType.ALL})
    @JoinTable(name="user_friends",
	joinColumns={@JoinColumn(name="user_id")},
	inverseJoinColumns={@JoinColumn(name="friend_id")})
	@JsonIgnore
	private Set<User>friends = new HashSet<User>();
    
    @ManyToMany(mappedBy="friends")
	@JsonIgnore
	private Set<User> friendsOf = new HashSet<User>();

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name="requester_potentialFriends",
    joinColumns= {@JoinColumn(name="user_id")},
    inverseJoinColumns= {@JoinColumn(name="potential_friend_id")})
    @JsonIgnore
    private Set<User>potential_friends = new HashSet<User>();
    
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name="friend_requests",
    joinColumns= {@JoinColumn(name="user_id")},
    inverseJoinColumns= {@JoinColumn(name="potential_friend_id")})
    @JsonIgnore
    private Set<User> friendsRequest = new HashSet<User>();
    
   
    public User(String firstName, String lastName, String password, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.username = username;
        this.role = Role.USER.ordinal();
    }

    public User(String firstName, String lastName, String password, String username, int role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.username = username;
        this.role = role;
    }

    public User() {
    }


    public long getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String name){
        this.firstName = name;
    }

    public String getLastName(){
        return lastName;
    }

    public void setLastName(String name){
        this.lastName = name;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public int getRole(){
        return role;
    }

    public void setRole(int role){
        this.role = role;
    }
    
    public void setFriends(Set<User>friends) {
    	this.friends = friends;
    }
    public Set<User> getFriends() {
    	return friends;
    }
    public void setPotentialFriends(Set<User>potential_friends) {
    	this.potential_friends = potential_friends;
    }

    @JsonIgnore
    public Set<User> getPotentialFriends(){
    	return potential_friends;
    }
    public void setFriendsRequest(Set<User>friendsRequest) {
    	this.friendsRequest = friendsRequest;
    }

    @JsonIgnore
    public Set<User> getFriendsRequest(){
    	return friendsRequest;
    }
    
}
