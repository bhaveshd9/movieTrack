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

    
    @Bean
    CommandLineRunner initUser(UserRepository userRepository, LaptopRepository laptopRepository, PhoneRepository phoneRepository) {
        return args -> {
            User user1 = new User("John", "john@somemail.com", new Date());
            User user2 = new User("Jane", "jane@somemail.com", new Date());
            User user3 = new User("Justin", "justin@somemail.com", new Date());
            User user4 = new User("Jack", "jack1@somemail.com", new Date());
            User user5 = new User("Mike", "mike2@somemail.com", new Date());
            User user6 = new User("Adam", "eve@somemail.com", new Date());
            User user7 = new User("Nick", "nicki@somemail.com", new Date());
            User user8 = new User("Prabin", "prabie@somemail.com", new Date());
            Laptop laptop1 = new Laptop( 2.5, 4, 8, "Lenovo", 300);
            Laptop laptop2 = new Laptop( 4.1, 8, 16, "Hp", 800);
            Laptop laptop3 = new Laptop( 3.5, 32, 32, "Dell", 2300); 
            Laptop laptop4 = new Laptop( 2.0, 4, 8, "Acer", 300);
            Laptop laptop5 = new Laptop( 4.1, 8, 16, "Asus", 800);
            Laptop laptop6 = new Laptop( 3.5, 32, 32, "Apple", 2300); 
            Laptop laptop7 = new Laptop( 2.5, 16, 16, "Lenovo", 300);
            Laptop laptop8 = new Laptop( 4.0, 8, 16, "Hp", 800);
            for(int i=6; i<14; i++)
                phoneRepository.save(new Phone("Apple", (int)Math.pow(1.3, i), Math.pow(1.1, i)*1000, "iPhone "+i, (int)Math.pow(1.3, i)*100));
            user1.setLaptop(laptop1);
            user2.setLaptop(laptop2);
            user3.setLaptop(laptop3);
            user4.setLaptop(laptop4);
            user5.setLaptop(laptop5);
            user6.setLaptop(laptop6);
            user7.setLaptop(laptop7);
            user8.setLaptop(laptop8);
            user1.addPhones(phoneRepository.findById(1));
            user2.addPhones(phoneRepository.findById(2));            
            user3.addPhones(phoneRepository.findById(6));
            user4.addPhones(phoneRepository.findById(3));
            user5.addPhones(phoneRepository.findById(4)); 
            user6.addPhones(phoneRepository.findById(5));
            user7.addPhones(phoneRepository.findById(7));
            user8.addPhones(phoneRepository.findById(8));
            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);
            userRepository.save(user4);
            userRepository.save(user5);
            userRepository.save(user6);
            userRepository.save(user7);
            userRepository.save(user8);
        };
    }

}