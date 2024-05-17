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
public class TestingSystemTest1 {

  @LocalServerPort
	int port;

	@Before
	public void setUp() {
		RestAssured.port = port;
		RestAssured.baseURI = "http://localhost";
	}

	static int listID;

	@Test
	public void TEST_01_createlist() {
		// Send request and receive response
		Response response = RestAssured.given().
				header("Content-Type", "application/json").
				header("charset","utf-8").
				body("{    \"listName\": \"test\"}").
				when().
				post("/list/1");


		// Check status code
		int statusCode = response.getStatusCode();
		assertEquals(200, statusCode);

		String returnString = response.getBody().asString();
		try {
			JSONObject returnObj = new JSONObject(returnString);
			String id = returnObj.getString("listName");
			listID = returnObj.getInt("id");
			assertEquals("test", id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}



	@Test
	public void TEST_02_deletelist() {
		// Send request and receive response
		Response response = RestAssured.given().
				header("Content-Type", "application/json").
				header("charset","utf-8").
				body("").
				when().
				delete("/deleteList/" + listID);


		// Check status code
		int statusCode = response.getStatusCode();
		assertEquals(200, statusCode);

		
	}

	

	// }
}
