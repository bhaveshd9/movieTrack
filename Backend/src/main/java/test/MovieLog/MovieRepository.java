package test.MovieLog;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


public interface MovieRepository extends JpaRepository<MovieLog, Long> {
    MovieLog findById(long id);

    List<MovieLog> findByUserId(long userId);

    List<MovieLog> findByUserIdOrderByDateDesc(long userId);

    List<MovieLog> findByUserIdOrderByRatingDesc(long userId);

    List<MovieLog> findTop40ByUserIdInOrderByDateDesc(List<Long> userId);

    MovieLog findFirstByUserIdAndMovieIDOrderByDateDesc(long userID, int movieID);

    @Transactional
    void deleteById(long id);
}
