package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runners.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestingSystemTest {

  @LocalServerPort
	int port;

	@Before
	public void setUp() {
		RestAssured.port = port;
		RestAssured.baseURI = "http://localhost";
	}

	@Test
	public void TEST_01_usertest() {
		// Send request and receive response
		Response response = RestAssured.given().
				header("Content-Type", "text/plain").
				header("charset","utf-8").
				body("").
				when().
				get("/users/1");


		// Check status code
		int statusCode = response.getStatusCode();
		assertEquals(200, statusCode);

		// Check response body for correct response
		String returnString = response.getBody().asString();
		try {
			JSONObject returnObj = new JSONObject(returnString);
			int id = returnObj.getInt("id");
			assertEquals(1, id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void TEST_02_userstest() {
		// Send request and receive response
		Response response = RestAssured.given().
				header("Content-Type", "text/plain").
				header("charset","utf-8").
				body("").
				when().
				get("/users");


		// Check status code
		int statusCode = response.getStatusCode();
		assertEquals(200, statusCode);

		String returnString = response.getBody().asString();
		try {
			JSONArray returnObj = new JSONArray(returnString);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	static int userid;
	static String username = "testuser1234" + System.currentTimeMillis();

	@Test
	public void TEST_03_createuser() {
		// Send request and receive response
		Response response = RestAssured.given().
				header("Content-Type", "application/json").
				header("charset","utf-8").
				body("{\r\n    \"firstName\": \"Ayden\",\r\n    \"lastName\": \"Albertsen\",\r\n    \"password\": \"test\",\r\n    \"username\": \"" + username + "\",\r\n    \"role\": 0\r\n}").
				when().
				post("/users");


		// Check status code
		int statusCode = response.getStatusCode();
		assertEquals(200, statusCode);

		String returnString = response.getBody().asString();
		try {
			JSONObject returnObj = new JSONObject(returnString);
			String id = returnObj.getString("firstName");
			userid = returnObj.getInt("id");
			assertEquals("Ayden", id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void TEST_04_updateUser() {
		// Send request and receive response
		Response response = RestAssured.given().
				header("Content-Type", "application/json").
				header("charset","utf-8").
				body("{\r\n    \"firstName\": \"Ayden1\",\r\n    \"lastName\": \"Albertsen\",\r\n    \"password\": \"test\",\r\n    \"username\": \"" + username + "\",\r\n    \"role\": 0\r\n}").
				when().
				put("/users/" + userid);


		// Check status code
		int statusCode = response.getStatusCode();
		assertEquals(200, statusCode);

		String returnString = response.getBody().asString();
		try {
			JSONObject returnObj = new JSONObject(returnString);
			String id = returnObj.getString("firstName");
			assertEquals("Ayden1", id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void TEST_05_login() {
		// Send request and receive response
		Response response = RestAssured.given().
				header("Content-Type", "application/json").
				header("charset","utf-8").
				body("").
				when().
				get("/login/testuser1234/test");


		// Check status code
		int statusCode = response.getStatusCode();
		assertEquals(200, statusCode);

		String returnString = response.getBody().asString();
		try {
			JSONObject returnObj = new JSONObject(returnString);
			String id = returnObj.getString("firstName");
			assertEquals("Ayden1", id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

	@Test
	public void TEST_06_searchuser() {
		// Send request and receive response
		Response response = RestAssured.given().
				header("Content-Type", "application/json").
				header("charset","utf-8").
				body("").
				when().
				get("/searchUser/" + username);


		// Check status code
		int statusCode = response.getStatusCode();
		assertEquals(200, statusCode);

		String returnString = response.getBody().asString();
		try {
			JSONArray returnObj = new JSONArray(returnString);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}


	@Test
	public void TEST_06_deleteuser() {
		// Send request and receive response
		Response response = RestAssured.given().
				header("Content-Type", "application/json").
				header("charset","utf-8").
				body("").
				when().
				delete("/users/" + userid);


		// Check status code
		int statusCode = response.getStatusCode();
		assertEquals(200, statusCode);

		
	}

	

	// }
}
