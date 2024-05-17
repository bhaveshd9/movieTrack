package test.MovieLog;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


public interface MovieRepository extends JpaRepository<MovieLog, Long> {
    MovieLog findById(long id);

    List<MovieLog> findByUserId(long userId);

    @Transactional
    void deleteById(long id);
}
