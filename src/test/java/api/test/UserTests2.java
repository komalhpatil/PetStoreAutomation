package api.test;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;


import api.endpoints.UserEndpoints2;
import api.payload.User;
import io.restassured.response.Response;

public class UserTests2 {
	
	Faker faker;
	User userPayload;
	
	public Logger logger;
	
	@BeforeClass
	public void setUpData() {
		faker = new Faker();
		userPayload = new User();
		
		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setUsername(faker.name().username());
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().emailAddress());
		userPayload.setPassword(faker.internet().password(5, 10));
		userPayload.setPhone(faker.phoneNumber().cellPhone());
		
	logger = LogManager.getLogger(this.getClass());
	}
	
	@Test(priority = 1)
	public void testPostUser() {
		
		logger.info("***********Creating user************");
		System.out.println("Generated User Payload: " + userPayload.toString());
		Response response = UserEndpoints2.createUser(userPayload);
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
		logger.info("***********User created successfully************");
	}
	
	@Test(priority = 2)
	public void testGetUserByName() {
		logger.info("***********Reading user details************");
		Response response = UserEndpoints2.readUser(this.userPayload.getUsername());
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
		logger.info("***********User details retrieved successfully************");
	}
	
	@Test(priority = 3)
	public void testUpdateUserByName() {
		logger.info("***********Updating user details************");
		//Update the user details
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().emailAddress());
		
		Response response = UserEndpoints2.updateUser(this.userPayload.getUsername(), this.userPayload);
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
		
		//Get the updated user details
		Response getResponse = UserEndpoints2.readUser(this.userPayload.getUsername());
		getResponse.then().log().all();
		Assert.assertEquals(getResponse.getStatusCode(), 200);
		logger.info("***********User details updated successfully************");
		
	}
	
	@Test(priority = 4)
	public void testDeleteUserByName() {
		logger.info("***********Deleting user************");
		Response response = UserEndpoints2.deleteUser(this.userPayload.getUsername());
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
		
		//Try to get the deleted user details
		Response getResponse = UserEndpoints2.readUser(this.userPayload.getUsername());
		getResponse.then().log().all();
		Assert.assertEquals(getResponse.getStatusCode(), 404);
		logger.info("***********User deleted successfully************");
		
	}
}
