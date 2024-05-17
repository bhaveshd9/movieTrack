package test.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


public interface UserRepository extends JpaRepository<User, Long> {
    User findById(long id);
    User findByUsername(String username);

    @Transactional
    void deleteById(long id);

    User findByUsernameAndPassword(String username, String password);
}
