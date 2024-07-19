package com.example.carapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import com.example.carapi.model.Car;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CarControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private Long createdCarId; // to store the ID of the created car
    private Long updatedCarId; // to store the ID of the updated car
   
    @Test
    @DisplayName("Create Car Test")
    public void testCreateCar() {
        // Create a new Car object
        Car newCar = new Car();
        newCar.setColor("Yellow");
        newCar.setName("Test Car");
        newCar.setModel("Test Model");
        newCar.setMake("Test Make");
        newCar.setYear("2024");
        newCar.setPrice(25000.0);
        newCar.setImage("https://c8.alamy.com/comp/EEYDX5/crash-test-old-car-young-drivers-EEYDX5.jpg");

        // Send a POST request to create a new car
        ResponseEntity<Car> response = restTemplate.postForEntity("/api/cars", newCar, Car.class);

        // Assert the HTTP status code
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Assert the created car is returned
        Car createdCar = response.getBody();
        assertNotNull(createdCar);
        assertEquals("Yellow", createdCar.getColor());
        assertEquals("Test Car", createdCar.getName());

        // Store the ID of the created car for deletion
        createdCarId = createdCar.getId();
        System.out.println(createdCarId);
    }

   

    @Test
    @DisplayName("Create & Update Car Test")
    public void testUpdateCarDetails() {
        // Create a new Car object
        Car newCar = new Car();
        newCar.setColor("Blue");
        newCar.setName("Updated Car");
        newCar.setModel("Updated Model");
        newCar.setMake("Updated Make");
        newCar.setYear("2025");
        newCar.setPrice(30000.0);
        newCar.setImage("https://hips.hearstapps.com/hmg-prod/images/2019-honda-civic-sedan-1558453497.jpg");
        newCar.setSold(true);

        // Send a POST request to create a new car
        ResponseEntity<Car> createResponse = restTemplate.postForEntity("/api/cars", newCar, Car.class);
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        Car createdCar = createResponse.getBody();
        assertNotNull(createdCar);

        // Store the ID of the created car for deletion after update
        createdCarId = createdCar.getId();

        // Update car details
        createdCar.setPrice(28000.0); // Update price
        createdCar.setSold(true); // Mark car as sold

        // Send a PUT request to update the car details
        HttpEntity<Car> requestEntity = new HttpEntity<>(createdCar);
        ResponseEntity<Car> updateResponse = restTemplate.exchange(
                "/api/cars/" + createdCar.getId(),
                HttpMethod.PUT,
                requestEntity,
                Car.class);

        // Assert the HTTP status code
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());

        // Assert the updated car details
        Car updatedCar = updateResponse.getBody();
        assertNotNull(updatedCar);
        assertEquals(28000.0, updatedCar.getPrice());
        assertEquals(true, updatedCar.isSold());

        // Store the ID of the updated car for deletion
        updatedCarId = updatedCar.getId();
    }

    @Test
    @DisplayName("View all Cars Test")
    public void getAllCars() {
        // Send a GET request to retrieve all cars
        ResponseEntity<List<Car>> response = restTemplate.exchange(
                "/api/cars",
                org.springframework.http.HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Car>>() {});

        // Assert the HTTP status code
        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Car> cars = response.getBody();
        assertNotNull(cars);
    }

    @AfterEach
    public void cleanup() {
        // Delete the created car after each test method
        if (createdCarId != null) {
            restTemplate.delete("/api/cars/" + createdCarId);
            System.out.println("Deleted the created car with ID: " + createdCarId);
        }

        // Delete the updated car after each test method
        if (updatedCarId != null) {
            restTemplate.delete("/api/cars/" + updatedCarId);
            System.out.println("Deleted the updated car with ID: " + updatedCarId);
        }


    }

}
