package test.Image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import test.Users.User;

public interface ImageRepository extends JpaRepository<Image, Long>{
	
	Image findImageByUsername(String username);
	Image findById(long id);
	@Transactional
    void deleteById(long id);

}
