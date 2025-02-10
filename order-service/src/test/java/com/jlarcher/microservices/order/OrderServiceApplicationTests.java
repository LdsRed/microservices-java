package com.jlarcher.microservices.order;

import com.jlarcher.microservices.order.stubs.InventoryClientStub;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.testcontainers.containers.MariaDBContainer;
import static org.hamcrest.MatcherAssert.assertThat;


//@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
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
		var placeOrderJson = """
				{
					"skuCode": "iphone_16",
					"price": 75000,
					"quantity": 2
				}
				""";

		InventoryClientStub.stubInventoryCall("iphone_16", 2);
		var responseBody = RestAssured
				.given()
				.contentType("application/json")
				.body(placeOrderJson)
				.when()
				.post("api/order")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().asString();

		assertThat(responseBody, Matchers.is("Order Created Successfully"));
	}

}
