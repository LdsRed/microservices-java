package com.jl.microservices.inventoryservice;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MariaDBContainer;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InventoryServiceApplicationTests {

    @ServiceConnection
    static MariaDBContainer mariaDBContainer = new MariaDBContainer("mariadb:latest");
    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setup(){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }
    static {
        mariaDBContainer.start();
    }

    @Test
    void testProductInStock() {

        var productInStock = RestAssured
                        .given()
                .when()
                .get("/api/inventory?skuCode=iphone_16&quantity=100")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response().as(Boolean.class);
        Assertions.assertTrue(productInStock);
    }

    @Test
    void productNotInStock(){
        var negativeResponseScenario = RestAssured
                .given()
                .when()
                .get("/api/inventory?skuCode=iphone_16&quantity=200")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response().as(Boolean.class);
        Assertions.assertFalse(negativeResponseScenario);
    }

}
