package onetoone;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import onetoone.Laptops.Laptop;
import onetoone.Laptops.LaptopRepository;
import onetoone.Users.User;
import onetoone.Users.UserRepository;



@SpringBootApplication
@EnableJpaRepositories
class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    // Create 3 users with their machines
    /**
     * 
     * @param userRepository repository for the User entity
     * @param laptopRepository repository for the Laptop entity
     * Creates a commandLine runner to enter dummy data into the database
     * As mentioned in User.java just associating the Laptop object with the User will save it into the database because of the CascadeType
     */
    @Bean
    CommandLineRunner initUser(UserRepository userRepository, LaptopRepository laptopRepository) {
        return args -> {
            User user1 = new User("Liam", "liam2000@somemail.com");
            User user2 = new User("Oliver", "oliver29@somemail.com");
            User user3 = new User("Josh", "josh03@somemail.com");
            Laptop laptop1 = new Laptop( 3.0, 8, 16, "Asus", 300);
            Laptop laptop2 = new Laptop( 3.6, 8, 32, "Dell", 800);
            Laptop laptop3 = new Laptop( 4.0, 16, 64, "Apple", 2300);  
            user1.setLaptop(laptop3);
            user2.setLaptop(laptop2);
            user3.setLaptop(laptop1);  
            userRepository.save(user2);
            userRepository.save(user3);          
            userRepository.save(user1);
            

        };
    }

}
