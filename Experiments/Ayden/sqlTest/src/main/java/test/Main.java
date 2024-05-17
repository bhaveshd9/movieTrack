package test;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import test.Users.User;
import test.Users.UserRepository;


@SpringBootApplication
@EnableJpaRepositories
class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner initUser(UserRepository userRepository) {
        return args -> {
            //User user1 = new User("Admin", "", "Admin", "Admin");
            // User user2 = new User("Test", "test@somemail.com");
            // User user3 = new User("Test2", "Test2@somemail.com");           
            //userRepository.save(user1);
            // userRepository.save(user2);
            // userRepository.save(user3);

        };
    }

}
