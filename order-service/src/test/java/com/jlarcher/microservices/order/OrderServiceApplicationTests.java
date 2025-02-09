package com.jlarcher.microservices.order;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MariaDBContainer;

import static org.hamcrest.MatcherAssert.assertThat;


@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderServiceApplicationTests {

	@ServiceConnection
	static MariaDBContainer mariaDBContainer = new MariaDBContainer<>("mariadb:latest");
	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setup(){
		RestAssured.baseURI="http://localhost";
		RestAssured.port = port;
	}

	static {
		mariaDBContainer.start();
	}



	@Test
	void mustPlaceOrder() {
		String placeOrderJson = """
				{
					"skuCode": "Redragon Keyboard",
					"price": 75000,
					"quantity": 2
				}
				""";

				String responseBody = RestAssured
						.given()
						.contentType("application/json")
						.body(placeOrderJson)
						.when()
						.post("api/order")
						.then()
						.statusCode(201)
						.extract()
						.body().asString();

				assertThat(responseBody, Matchers.is("Order Created Successfully"));
	}

}
