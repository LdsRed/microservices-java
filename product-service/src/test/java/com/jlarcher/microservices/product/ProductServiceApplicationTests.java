package com.jlarcher.microservices.product;

import io.restassured.RestAssured;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {


	@ServiceConnection //Springboot will automatically inject the URL details from properties file to the container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.7");
	@LocalServerPort //Whenever the application is running it will inject the port in which the application is running
	private Integer port;

	@BeforeEach
	void setup(){
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	static {
		mongoDBContainer.start();
	}

	@Test
	void mustCreateProduct() {
		String requestBody = """
				{
					"name": "Nubia Music",
					"description": "Nubia is a Phone from China",
					"price": 2400
				}
				""";
		RestAssured
				.given()
				.contentType("application/json")
				.body(requestBody)
				.when()
				.post("/api/product")
				.then()
				.statusCode(201)
				.body("id", Matchers.notNullValue())
				.body("name", Matchers.equalTo("Nubia Music"))
				.body("description", Matchers.equalTo("Nubia is a Phone from China"))
				.body("price", Matchers.equalTo(2400));

	}

}
