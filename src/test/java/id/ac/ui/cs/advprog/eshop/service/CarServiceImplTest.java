package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarServiceImplTest {

    @InjectMocks
    private CarServiceImpl carService;

    @Mock
    private CarRepository carRepository;

    private Car car;
    private List<Car> carList;

    @BeforeEach
    void setUp() {
        // Create a test car
        car = new Car();
        car.setCarId("car-123");
        car.setCarName("Toyota Corolla");
        car.setCarColor("Silver");
        car.setCarQuantity(5);

        // Create a list of cars for testing
        carList = new ArrayList<>();
        carList.add(car);

        Car car2 = new Car();
        car2.setCarId("car-456");
        car2.setCarName("Honda Civic");
        car2.setCarColor("Black");
        car2.setCarQuantity(3);
        carList.add(car2);
    }

    @Test
    void testCreate() {
        // Mock the repository create method
        when(carRepository.create(any(Car.class))).thenReturn(car);

        // Call the service method
        Car createdCar = carService.create(car);

        // Assert the car is returned correctly
        assertNotNull(createdCar);
        assertEquals(car.getCarId(), createdCar.getCarId());
        assertEquals(car.getCarName(), createdCar.getCarName());

        // Verify repository method was called
        verify(carRepository, times(1)).create(car);
    }

    @Test
    void testFindAll() {
        // Mock the repository findAll method
        Iterator<Car> iterator = carList.iterator();
        when(carRepository.findAll()).thenReturn(iterator);

        // Call the service method
        List<Car> result = carService.findAll();

        // Assert the list is returned correctly
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(car.getCarId(), result.get(0).getCarId());

        // Verify repository method was called
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        // Mock the repository findById method
        when(carRepository.findById("car-123")).thenReturn(car);

        // Call the service method
        Car foundCar = carService.findById("car-123");

        // Assert the car is returned correctly
        assertNotNull(foundCar);
        assertEquals(car.getCarId(), foundCar.getCarId());

        // Verify repository method was called
        verify(carRepository, times(1)).findById("car-123");
    }

    @Test
    void testFindByIdNonExistent() {
        // Mock the repository findById method for non-existent car
        when(carRepository.findById("non-existent")).thenReturn(null);

        // Call the service method
        Car foundCar = carService.findById("non-existent");

        // Assert null is returned
        assertNull(foundCar);

        // Verify repository method was called
        verify(carRepository, times(1)).findById("non-existent");
    }

    @Test
    void testEditSuccess() {
        // Create an updated car
        Car updatedCar = new Car();
        updatedCar.setCarId("car-123");
        updatedCar.setCarName("Toyota Corolla Updated");
        updatedCar.setCarColor("Blue");
        updatedCar.setCarQuantity(10);

        // Mock the repository methods
        when(carRepository.findById("car-123")).thenReturn(car);
        when(carRepository.edit(any(Car.class))).thenReturn(updatedCar);

        // Call the service method
        Car result = carService.edit(updatedCar);

        // Assert the car is returned correctly
        assertNotNull(result);
        assertEquals(updatedCar.getCarId(), result.getCarId());
        assertEquals(updatedCar.getCarName(), result.getCarName());

        // Verify repository methods were called
        verify(carRepository, times(1)).findById("car-123");
        verify(carRepository, times(1)).edit(updatedCar);
    }

    @Test
    void testEditNonExistentCar() {
        // Create a car with a non-existent ID
        Car updatedCar = new Car();
        updatedCar.setCarId("non-existent");
        updatedCar.setCarName("Non-existent Car");

        // Mock the repository findById method
        when(carRepository.findById("non-existent")).thenReturn(null);

        // Call the service method
        Car result = carService.edit(updatedCar);

        // Assert null is returned
        assertNull(result);

        // Verify findById was called but edit was not
        verify(carRepository, times(1)).findById("non-existent");
        verify(carRepository, never()).edit(any(Car.class));
    }

    @Test
    void testDeleteCarByIdSuccess() {
        // Mock the repository methods
        when(carRepository.findById("car-123")).thenReturn(car);
        when(carRepository.delete("car-123")).thenReturn(true);

        // Call the service method
        boolean result = carService.deleteCarById("car-123");

        // Assert the result is true
        assertTrue(result);

        // Verify repository methods were called
        verify(carRepository, times(1)).findById("car-123");
        verify(carRepository, times(1)).delete("car-123");
    }

    @Test
    void testDeleteCarByIdNonExistent() {
        // Mock the repository findById method for non-existent car
        when(carRepository.findById("non-existent")).thenReturn(null);

        // Call the service method
        boolean result = carService.deleteCarById("non-existent");

        // Assert the result is false
        assertFalse(result);

        // Verify findById was called but delete was not
        verify(carRepository, times(1)).findById("non-existent");
        verify(carRepository, never()).delete(anyString());
    }
}