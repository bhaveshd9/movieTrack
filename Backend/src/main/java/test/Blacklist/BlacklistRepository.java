package test.Blacklist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface BlacklistRepository extends JpaRepository<Blacklist, Long>{
	
	Blacklist findById(long id);
	Blacklist findByMovieId(int movieId);
	@Transactional
    void deleteByMovieId(int movieId);

}
