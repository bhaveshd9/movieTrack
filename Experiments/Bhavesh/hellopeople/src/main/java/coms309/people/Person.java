package coms309.people;


/**
 * Provides the Definition/Structure for the people row
 *
 * @author Vivek Bengre
 */

public class Person {

    private String firstName;

    private String lastName;

    private String address;

    private String phoneNumber;
    
    private String age;

    public Person(){
        
    }

    public Person(String firstName, String lastName, String address, String phoneNumber, String age){
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.age=age;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String telephone) {
        this.phoneNumber = telephone;
    }
    
    public String getAge() {
        return this.age;
    }

    public void setAge(String telephone, String age) {
        this.age = age;
    }


    @Override
    public String toString() {
        return firstName + " " 
               + lastName + " "
               + address + " "
               + phoneNumber+ " "
               +age;
    }
}
