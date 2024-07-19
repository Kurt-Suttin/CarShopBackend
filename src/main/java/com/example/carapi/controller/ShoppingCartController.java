package com.example.carapi.controller;

import com.example.carapi.model.Car;
import com.example.carapi.model.ShoppingCart;
import com.example.carapi.repository.CarRepository;
import com.example.carapi.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private CarRepository carRepository;

    @PostMapping("/{cartId}/add/{carId}")
    public ResponseEntity<ShoppingCart> addCarToCart(@PathVariable Long cartId, @PathVariable Long carId) {
        Optional<ShoppingCart> cartOptional = shoppingCartRepository.findById(cartId);
        Optional<Car> carOptional = carRepository.findById(carId);

        if (!cartOptional.isPresent() || !carOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        ShoppingCart cart = cartOptional.get();
        Car car = carOptional.get();
        cart.getCars().add(car);
        shoppingCartRepository.save(cart);

        return ResponseEntity.ok(cart);
    }

    @PutMapping("/cars/{carId}")
    public ResponseEntity<Car> updateCar(@PathVariable Long carId, @RequestBody Car updatedCar) {
        Optional<Car> carOptional = carRepository.findById(carId);

        if (!carOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Car existingCar = carOptional.get();
        existingCar.setSold(updatedCar.isSold()); // Update the sold status (or any other fields)

        carRepository.save(existingCar);

        return ResponseEntity.ok(existingCar);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<ShoppingCart> getShoppingCart(@PathVariable Long cartId) {
        Optional<ShoppingCart> cartOptional = shoppingCartRepository.findById(cartId);

        if (!cartOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        ShoppingCart cart = cartOptional.get();
        return ResponseEntity.ok(cart);
    }




    @DeleteMapping("/{cartId}/remove/{carId}")
    public ResponseEntity<ShoppingCart> removeCarFromCart(@PathVariable Long cartId, @PathVariable Long carId) {
        Optional<ShoppingCart> cartOptional = shoppingCartRepository.findById(cartId);
        Optional<Car> carOptional = carRepository.findById(carId);

        if (!cartOptional.isPresent() || !carOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        ShoppingCart cart = cartOptional.get();
        Car car = carOptional.get();
        if (!cart.getCars().remove(car)) {
            return ResponseEntity.badRequest().body(cart);  // Car was not in the cart
        }
        shoppingCartRepository.save(cart);

        return ResponseEntity.ok(cart);
    }





}
