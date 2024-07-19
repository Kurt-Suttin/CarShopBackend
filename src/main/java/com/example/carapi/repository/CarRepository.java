package com.example.carapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.carapi.model.Car;

public interface CarRepository extends JpaRepository<Car,Long>{

    List<Car> findBySoldFalse();

    List<Car> findByColor(String color);


}
