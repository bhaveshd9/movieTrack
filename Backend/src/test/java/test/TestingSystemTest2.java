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
public class TestingSystemTest2 {
	
	 @LocalServerPort
		int port;

		@Before
		public void setUp() {
			RestAssured.port = port;
			RestAssured.baseURI = "http://localhost";
		}
		
		@Test
		public void TEST_01_userRole() {
			Response response = RestAssured.given().
					header("Content-Type", "text/plain").
					header("charset","utf-8").
					body("").
					when().
					get("/role/user");

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
		public void TEST_02_adminRole() {

			Response response = RestAssured.given().
					header("Content-Type", "text/plain").
					header("charset","utf-8").
					body("").
					when().
					get("/role/admin");


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
		public void TEST_03_modRole() {
			// Send request and receive response
			Response response = RestAssured.given().
					header("Content-Type", "text/plain").
					header("charset","utf-8").
					body("").
					when().
					get("/role/mod");


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
		public void TEST_04_announcements() {
			// Send request and receive response
			Response response = RestAssured.given().
					header("Content-Type", "text/plain").
					header("charset","utf-8").
					body("").
					when().
					get("/ann/all");


			int statusCode = response.getStatusCode();
			assertEquals(200, statusCode);

	
			String returnString = response.getBody().asString();
			try {
				JSONArray returnObj = new JSONArray(returnString);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		static int userid = 69;
		@Test
		public void TEST_05_updateUserRole() {
			Response response = RestAssured.given().
					header("Content-Type", "application/json").
					header("charset","utf-8").
					body("{\r\n    \"role\": 0\r\n}").
					when().
					put("/userRole/" + userid);

			int statusCode = response.getStatusCode();
			assertEquals(200, statusCode);

			String returnString = response.getBody().asString();
			try {
				JSONObject returnObj = new JSONObject(returnString);
				String id = returnObj.getString("firstName");
				assertEquals("bradley", id);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		@Test
		public void TEST_06_listtest() {
			// Send request and receive response
			Response response = RestAssured.given().
					header("Content-Type", "text/plain").
					header("charset","utf-8").
					body("").
					when().
					get("/users/lists");


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
		public void TEST_07_logstest() {
			// Send request and receive response
			Response response = RestAssured.given().
					header("Content-Type", "text/plain").
					header("charset","utf-8").
					body("").
					when().
					get("/allLogs");


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
		public void TEST_08_userlogstest() {
			// Send request and receive response
			Response response = RestAssured.given().
					header("Content-Type", "text/plain").
					header("charset","utf-8").
					body("").
					when().
					get("/users/logs");


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
		public void TEST_09_updateUserPassword() {
			Response response = RestAssured.given().
					header("Content-Type", "application/json").
					header("charset","utf-8").
					body("{\r\n    \"password\": \"test\" \r\n}").
					when().
					put("/userPassword/" + userid);

			int statusCode = response.getStatusCode();
			assertEquals(200, statusCode);

			String returnString = response.getBody().asString();
			try {
				JSONObject returnObj = new JSONObject(returnString);
				String id = returnObj.getString("firstName");
				assertEquals("bradley", id);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		@Test
		public void TEST_10_feedtest() {
			Response response = RestAssured.given().
					header("Content-Type", "text/plain").
					header("charset","utf-8").
					body("").
					when().
					get("/feed/" + userid);

			int statusCode = response.getStatusCode();
			assertEquals(200, statusCode);

			String returnString = response.getBody().asString();
			try {
				JSONArray returnObj = new JSONArray(returnString);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		static int imageid;
		
		@Test
		public void TEST_11_deleteimage() {
			Response response = RestAssured.given().
					header("Content-Type", "application/json").
					header("charset","utf-8").
					body("").
					when().
					delete("/image/" + imageid);


			// Check status code
			int statusCode = response.getStatusCode();
			assertEquals(200, statusCode);
		}
		
		@Test
		public void TEST_12_friendrequesttest() {
			Response response = RestAssured.given().
					header("Content-Type", "text/plain").
					header("charset","utf-8").
					body("").
					when().
					get("/users/" + userid +"/friend_requests");

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
		public void TEST_13_friendslisttest() {
			Response response = RestAssured.given().
					header("Content-Type", "text/plain").
					header("charset","utf-8").
					body("").
					when().
					get("/users/" + userid +"/friends");

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
		public void TEST_14_potentialfriendtest() {
			Response response = RestAssured.given().
					header("Content-Type", "text/plain").
					header("charset","utf-8").
					body("").
					when().
					get("/users/" + userid +"/potential_friends");

			int statusCode = response.getStatusCode();
			assertEquals(200, statusCode);

			String returnString = response.getBody().asString();
			try {
				JSONArray returnObj = new JSONArray(returnString);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
}

