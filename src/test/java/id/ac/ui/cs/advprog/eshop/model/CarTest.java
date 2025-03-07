package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarTest {

    private Car car;

    @BeforeEach
    void setUp() {
        car = new Car();
    }

    @Test
    void testGetAndSetCarId() {
        String carId = "car-123";
        car.setCarId(carId);
        assertEquals(carId, car.getCarId());
    }

    @Test
    void testGetAndSetCarName() {
        String carName = "Rolls Royce";
        car.setCarName(carName);
        assertEquals(carName, car.getCarName());
    }

    @Test
    void testGetAndSetCarColor() {
        String carColor = "Black";
        car.setCarColor(carColor);
        assertEquals(carColor, car.getCarColor());
    }

    @Test
    void testGetAndSetCarQuantity() {
        int carQuantity = 5;
        car.setCarQuantity(carQuantity);
        assertEquals(carQuantity, car.getCarQuantity());
    }

    @Test
    void testInitialValuesAreNull() {
        assertNull(car.getCarId());
        assertNull(car.getCarName());
        assertNull(car.getCarColor());
        assertEquals(0, car.getCarQuantity());
    }

    @Test
    void testUpdateAllAttributes() {
        // Set initial values
        car.setCarId("car-123");
        car.setCarName("Toyota");
        car.setCarColor("Red");
        car.setCarQuantity(3);

        // Verify initial values
        assertEquals("car-123", car.getCarId());
        assertEquals("Toyota", car.getCarName());
        assertEquals("Red", car.getCarColor());
        assertEquals(3, car.getCarQuantity());

        // Update values
        car.setCarId("car-456");
        car.setCarName("Honda");
        car.setCarColor("Blue");
        car.setCarQuantity(7);

        // Verify updated values
        assertEquals("car-456", car.getCarId());
        assertEquals("Honda", car.getCarName());
        assertEquals("Blue", car.getCarColor());
        assertEquals(7, car.getCarQuantity());
    }
}