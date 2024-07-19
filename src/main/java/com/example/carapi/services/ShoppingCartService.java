package com.example.carapi.services;

import com.example.carapi.model.Car;
import com.example.carapi.model.ShoppingCart;
import com.example.carapi.repository.CarRepository;
import com.example.carapi.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShoppingCartService {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private CarRepository carRepository;

    public ShoppingCart addCarToCart(Long cartId, Long carId) {
        Optional<ShoppingCart> cartOptional = shoppingCartRepository.findById(cartId);
        Optional<Car> carOptional = carRepository.findById(carId);

        if (cartOptional.isPresent() && carOptional.isPresent()) {
            ShoppingCart cart = cartOptional.get();
            Car car = carOptional.get();
            cart.getCars().add(car);
            return shoppingCartRepository.save(cart);
        }
        throw new RuntimeException("Cart or Car not found");
    }

    public ShoppingCart removeCarFromCart(Long cartId, Long carId) {
        Optional<ShoppingCart> cartOptional = shoppingCartRepository.findById(cartId);
        Optional<Car> carOptional = carRepository.findById(carId);

        if (cartOptional.isPresent() && carOptional.isPresent()) {
            ShoppingCart cart = cartOptional.get();
            Car car = carOptional.get();
            cart.getCars().remove(car);
            return shoppingCartRepository.save(cart);
        }
        throw new RuntimeException("Cart or Car not found");
    }
}
