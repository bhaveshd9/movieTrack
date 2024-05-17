package onetoone.Laptops;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;



public interface LaptopRepository extends JpaRepository<Laptop, Long> {
    Laptop findById(int id); // finding laptop by id 

    @Transactional
    void deleteById(int id); //deleting it by id
}
