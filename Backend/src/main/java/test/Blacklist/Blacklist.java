package test.Blacklist;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="blacklist_movies")
public class Blacklist {
	

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int movieId;
   
    public Blacklist() {}
    
    public long getmovieId(){
        return movieId;
    }

    public void setmovieId(int movieId){
        this.movieId = movieId;
    }
}
