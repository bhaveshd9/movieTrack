package test.Lists;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ListRepository extends JpaRepository<MovieList, Long> {
    MovieList findById(long id);

    List<MovieList> findByUserId(long userId);

    @Transactional
    void deleteById(long id);
}
