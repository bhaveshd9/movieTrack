package onetomany.Laptops;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LaptopController {

    @Autowired
    LaptopRepository laptopRepository;
    
    private String success = "{\"message\":\"successfully added\"}";
    private String failure = "{\"message\":\"failure : try again using correct input\"}";

     @GetMapping(path = "/laptops/{id}")
    Laptop getLaptopById(@PathVariable int id){
        return laptopRepository.findById(id);
    }
  
     @PutMapping(path = "/laptops/{id}")
    Laptop updateLaptop(@PathVariable int id, @RequestBody Laptop request){
        Laptop laptop = laptopRepository.findById(id);
        if(laptop == null)
            return null;
        laptopRepository.save(request);
        return laptopRepository.findById(id);
    } 
    
    @GetMapping(path = "/laptops")
    List<Laptop> getAllLaptops(){
        return laptopRepository.findAll();
    }

   @DeleteMapping(path = "/laptops/{id}")
    String deleteLaptop(@PathVariable int id){
        laptopRepository.deleteById(id);
        return success;
    }

   @PostMapping(path = "/laptops")
   String createLaptop(@RequestBody Laptop Laptop){
       if (Laptop == null)
           return failure;
       laptopRepository.save(Laptop);
       return success;
       
   }

    

    
}
