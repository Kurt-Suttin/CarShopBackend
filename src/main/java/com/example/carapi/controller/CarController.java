package com.example.carapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.carapi.model.Car;
import com.example.carapi.repository.CarRepository;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@CrossOrigin(origins = "*")
public class CarController {

    private static Logger log = LoggerFactory.getLogger(CarController.class);

    private final CarRepository carRepository;

    public CarController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }
//  GET MAPPINGS 
    @GetMapping
    public List<Car> getAllCars() {
        log.info("Getting All Cars.");
        return carRepository.findAll();
    }

    @GetMapping("/{id}")
    public Car getCarById(@PathVariable Long id) {
        return carRepository.findById(id).orElse(null);
    }

    @GetMapping("/unsold")
    public List<Car> getUnSoldCars() {
        return carRepository.findBySoldFalse();
    }
    @GetMapping("/color/{color}")
    public List<Car> getCarsByColor(@PathVariable String color) {
        return carRepository.findByColor(color);
    }

    // POST & PUT MAPPINGS 
    @PostMapping
    public Car createCar(@RequestBody Car car) {
        return carRepository.save(car);
    }


    @PutMapping("/{id}")
    public Car updateCar(@PathVariable Long id, @RequestBody Car carDetails) {
        Car car = carRepository.findById(id).orElse(null);
        if (car != null) {
            car.setColor(carDetails.getColor());
            car.setName(carDetails.getName());
            car.setModel(carDetails.getModel());
            car.setMake(carDetails.getMake());
            car.setYear(carDetails.getYear());
            car.setPrice(carDetails.getPrice());
            car.setImage(carDetails.getImage());
            return carRepository.save(car);
        }
        return null;
    }




    @PutMapping("/{carId}/sell")
    public ResponseEntity<Car> sellCar(@PathVariable Long carId) {
        Car car = carRepository.findById(carId).orElse(null);

        if (car == null) {
            return ResponseEntity.notFound().build();
        }

        if (!car.isSold()) { // Check if car is not already sold
            car.setSold(true); // Set Sold to true
            carRepository.save(car); // Save updated car entity
        }

        return ResponseEntity.ok(car);
    }

    @DeleteMapping("/{id}")
    public void deleteCar(@PathVariable Long id) {
        carRepository.deleteById(id);
    }
}
