package com.cs309.tutorial.tests;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
	
	ArrayList<TestData> data = new ArrayList<TestData>();
	
	@GetMapping("/getTest")
	public String getTest(@RequestParam(value = "username", defaultValue = "World") String message) {
		return String.format("Hello, %s! You sent a get request with a parameter!", message);
	}

	@GetMapping("/getAll")
	public ArrayList<TestData> getAll(){
		return data;
	}
	
	@PostMapping("/postTest1")
	public String postTest1(@RequestBody TestData test) {
		data.add(test);
		return "added " + test.getMessage();
	}
	
	@PostMapping("/postTest2")
	public String postTest2(@RequestBody TestData testData) {
		data.add(testData);
		return String.format("Hello, %s! You sent a post request with a requestbody!", testData.getMessage());
	}
	
	@DeleteMapping("/deleteTest")
	public String deleteTest(@RequestParam(value = "message", defaultValue = "") String message) {
		for (int i =0; i<data.size(); i++){
			if (data.get(i).getMessage().equals(message)){
				data.remove(i);
				return "deleted " + message;
			}
			 
		}
		return "not found";
	}
	
	@PutMapping("/putTest")
	public String putTest(@RequestParam(value = "message", defaultValue = "") String message, @RequestBody TestData testData) {
		for (int i =0; i<data.size(); i++){
			if (data.get(i).getMessage().equals(message)){
				data.set(i, testData);
				return "updated " + message + " to " + testData.getMessage();
			}
			 
		}
		return "not found";
	}

	@GetMapping("/oops")
    public String triggerException() {
        throw new RuntimeException("This is a test exception");
    }

}
