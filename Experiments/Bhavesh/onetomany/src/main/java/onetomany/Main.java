package onetomany;

import java.util.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import onetomany.Laptops.Laptop;
import onetomany.Laptops.LaptopRepository;
import onetomany.Phones.Phone;
import onetomany.Phones.PhoneRepository;
import onetomany.Users.User;
import onetomany.Users.UserRepository;

@SpringBootApplication
class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    // Create 3 users with their machines and phones
    @Bean
    CommandLineRunner initUser(UserRepository userRepository, LaptopRepository laptopRepository, PhoneRepository phoneRepository) {
        return args -> {
            User user1 = new User("Dew", "noname@gmail.com", new Date());
            User user2 = new User("Prabin", "giri@somemail.com", new Date());
            User user3 = new User("Doug", "demuro@somemail.com", new Date());
            Laptop laptop1 = new Laptop( 2.0, 4, 8, "Acer", 300);
            Laptop laptop2 = new Laptop( 2.9, 8, 16, "Hp", 800);
            Laptop laptop3 = new Laptop( 4.5, 32, 32, "Apple", 2300); 
            for(int i=6; i<13; i++)
                phoneRepository.save(new Phone("Samsung", (int)Math.pow(1.3, i), Math.pow(1.1, i)*1000, "galaxy "+i, (int)Math.pow(1.3, i)*100));
            user1.setLaptop(laptop1);
            user2.setLaptop(laptop2);
            user3.setLaptop(laptop3);
            user1.addPhones(phoneRepository.findById(7));
            user1.addPhones(phoneRepository.findById(5));            
            user1.addPhones(phoneRepository.findById(3));
            user2.addPhones(phoneRepository.findById(1));
            user2.addPhones(phoneRepository.findById(2)); 
            user3.addPhones(phoneRepository.findById(4));
            user3.addPhones(phoneRepository.findById(6));
            userRepository.save(user3);
            userRepository.save(user2);
            userRepository.save(user1);
            
            
        };
    }

}