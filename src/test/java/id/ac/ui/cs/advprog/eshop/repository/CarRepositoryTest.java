package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CarRepositoryTest {

    private CarRepository carRepository;
    private Car car1;
    private Car car2;

    @BeforeEach
    void setUp() {
        carRepository = new CarRepository();

        // Create first test car
        car1 = new Car();
        car1.setCarName("Toyota Avanza");
        car1.setCarColor("Silver");
        car1.setCarQuantity(10);

        // Create second test car
        car2 = new Car();
        car2.setCarName("Honda Civic");
        car2.setCarColor("Black");
        car2.setCarQuantity(5);
    }

    @Test
    void testCreateCarWithoutId() {
        Car savedCar = carRepository.create(car1);

        // Verify car is saved
        assertNotNull(savedCar);
        assertNotNull(savedCar.getCarId());
        assertEquals(car1.getCarName(), savedCar.getCarName());
        assertEquals(car1.getCarColor(), savedCar.getCarColor());
        assertEquals(car1.getCarQuantity(), savedCar.getCarQuantity());

        // Verify car can be found in repository
        Iterator<Car> carIterator = carRepository.findAll();
        assertTrue(carIterator.hasNext());
        Car foundCar = carIterator.next();
        assertEquals(savedCar.getCarId(), foundCar.getCarId());
    }

    @Test
    void testCreateCarWithId() {
        // Set a predetermined ID
        String predeterminedId = UUID.randomUUID().toString();
        car1.setCarId(predeterminedId);

        Car savedCar = carRepository.create(car1);

        // Verify the ID is preserved
        assertEquals(predeterminedId, savedCar.getCarId());

        // Verify car is in repository
        Car foundCar = carRepository.findById(predeterminedId);
        assertNotNull(foundCar);
        assertEquals(predeterminedId, foundCar.getCarId());
    }

    @Test
    void testFindAll() {
        // Add cars to repository
        carRepository.create(car1);
        carRepository.create(car2);

        // Test finding all cars
        Iterator<Car> carIterator = carRepository.findAll();

        // Verify both cars are found
        assertTrue(carIterator.hasNext());
        Car firstCar = carIterator.next();
        assertNotNull(firstCar);

        assertTrue(carIterator.hasNext());
        Car secondCar = carIterator.next();
        assertNotNull(secondCar);

        // Verify no more cars
        assertFalse(carIterator.hasNext());
    }

    @Test
    void testFindById() {
        // Add car to repository
        Car savedCar = carRepository.create(car1);
        String carId = savedCar.getCarId();

        // Find car by ID
        Car foundCar = carRepository.findById(carId);

        // Verify car is found and matches
        assertNotNull(foundCar);
        assertEquals(carId, foundCar.getCarId());
        assertEquals(car1.getCarName(), foundCar.getCarName());
        assertEquals(car1.getCarColor(), foundCar.getCarColor());
        assertEquals(car1.getCarQuantity(), foundCar.getCarQuantity());
    }

    @Test
    void testFindByIdNotFound() {
        // Test finding by non-existent ID
        Car foundCar = carRepository.findById("non-existent-id");

        // Verify no car is found
        assertNull(foundCar);
    }

    @Test
    void testEditCar() {
        // Add car to repository
        Car savedCar = carRepository.create(car1);
        String carId = savedCar.getCarId();

        // Create updated car with same ID
        Car updatedCar = new Car();
        updatedCar.setCarId(carId);
        updatedCar.setCarName("Toyota Avanza Updated");
        updatedCar.setCarColor("Red");
        updatedCar.setCarQuantity(15);

        // Edit car
        Car resultCar = carRepository.edit(updatedCar);

        // Verify car is updated
        assertNotNull(resultCar);
        assertEquals(carId, resultCar.getCarId());
        assertEquals(updatedCar.getCarName(), resultCar.getCarName());
        assertEquals(updatedCar.getCarColor(), resultCar.getCarColor());
        assertEquals(updatedCar.getCarQuantity(), resultCar.getCarQuantity());

        // Verify updated car can be found in repository
        Car foundCar = carRepository.findById(carId);
        assertNotNull(foundCar);
        assertEquals(updatedCar.getCarName(), foundCar.getCarName());
        assertEquals(updatedCar.getCarColor(), foundCar.getCarColor());
        assertEquals(updatedCar.getCarQuantity(), foundCar.getCarQuantity());
    }

    @Test
    void testEditCarNotFound() {
        // Create car with non-existent ID
        Car nonExistentCar = new Car();
        nonExistentCar.setCarId("non-existent-id");
        nonExistentCar.setCarName("Non-existent Car");
        nonExistentCar.setCarColor("Invisible");
        nonExistentCar.setCarQuantity(0);

        // Try to edit non-existent car
        Car resultCar = carRepository.edit(nonExistentCar);

        // Verify no car is returned
        assertNull(resultCar);
    }

    @Test
    void testDeleteCar() {
        // Add car to repository
        Car savedCar = carRepository.create(car1);
        String carId = savedCar.getCarId();

        // Verify car exists before deletion
        Car foundCar = carRepository.findById(carId);
        assertNotNull(foundCar);

        // Delete car
        boolean deleteResult = carRepository.delete(carId);

        // Verify deletion was successful
        assertTrue(deleteResult);

        // Verify car no longer exists
        Car deletedCar = carRepository.findById(carId);
        assertNull(deletedCar);
    }

    @Test
    void testDeleteCarNotFound() {
        // Try to delete non-existent car
        boolean deleteResult = carRepository.delete("non-existent-id");

        // Verify deletion failed
        assertFalse(deleteResult);
    }

    @Test
    void testMultipleOperations() {
        // Add multiple cars
        Car savedCar1 = carRepository.create(car1);
        Car savedCar2 = carRepository.create(car2);

        // Verify both cars exist
        assertEquals(2, countCars(carRepository.findAll()));

        // Delete first car
        boolean deleteResult = carRepository.delete(savedCar1.getCarId());
        assertTrue(deleteResult);

        // Verify only second car remains
        assertEquals(1, countCars(carRepository.findAll()));
        assertNull(carRepository.findById(savedCar1.getCarId()));
        assertNotNull(carRepository.findById(savedCar2.getCarId()));

        // Edit second car
        Car updatedCar2 = new Car();
        updatedCar2.setCarId(savedCar2.getCarId());
        updatedCar2.setCarName("Updated Name");
        updatedCar2.setCarColor("Updated Color");
        updatedCar2.setCarQuantity(20);

        Car resultCar = carRepository.edit(updatedCar2);
        assertNotNull(resultCar);

        // Verify car was updated
        Car foundCar = carRepository.findById(savedCar2.getCarId());
        assertEquals("Updated Name", foundCar.getCarName());
        assertEquals("Updated Color", foundCar.getCarColor());
        assertEquals(20, foundCar.getCarQuantity());
    }

    // Helper method to count cars in iterator
    private int countCars(Iterator<Car> carIterator) {
        int count = 0;
        while (carIterator.hasNext()) {
            carIterator.next();
            count++;
        }
        return count;
    }

    @Test
    void testEditCarWithEmptyRepository() {
        // Setup a car with ID but don't add to repository
        Car carToEdit = new Car();
        carToEdit.setCarId("test-id-123");
        carToEdit.setCarName("Test Car");
        carToEdit.setCarColor("Red");
        carToEdit.setCarQuantity(3);

        // Try to edit the car
        Car result = carRepository.edit(carToEdit);

        // Verify null is returned (car not found)
        assertNull(result);
    }

    @Test
    void testEditCarWithMultipleCarsInRepository() {
        // Add multiple cars to repository
        Car savedCar1 = carRepository.create(car1);
        Car savedCar2 = carRepository.create(car2);

        // Create a car with the same ID as the first car
        Car carToEdit = new Car();
        carToEdit.setCarId(savedCar1.getCarId());
        carToEdit.setCarName("Updated Toyota");
        carToEdit.setCarColor("Blue");
        carToEdit.setCarQuantity(8);

        // Edit the car
        Car result = carRepository.edit(carToEdit);

        // Verify the first car was updated
        assertNotNull(result);
        assertEquals(savedCar1.getCarId(), result.getCarId());
        assertEquals("Updated Toyota", result.getCarName());
        assertEquals("Blue", result.getCarColor());
        assertEquals(8, result.getCarQuantity());

        // Verify the second car was not modified
        Car foundCar2 = carRepository.findById(savedCar2.getCarId());
        assertEquals("Honda Civic", foundCar2.getCarName());
        assertEquals("Black", foundCar2.getCarColor());
        assertEquals(5, foundCar2.getCarQuantity());
    }

    @Test
    void testEditFirstCarInRepository() {
        // Add multiple cars to repository
        Car savedCar1 = carRepository.create(car1);
        Car savedCar2 = carRepository.create(car2);

        // Create updated car with ID of first car
        Car updatedCar = new Car();
        updatedCar.setCarId(savedCar1.getCarId());
        updatedCar.setCarName("First Car Updated");
        updatedCar.setCarColor("Yellow");
        updatedCar.setCarQuantity(20);

        // Edit the car
        Car result = carRepository.edit(updatedCar);

        // Verify update successful
        assertNotNull(result);
        assertEquals(savedCar1.getCarId(), result.getCarId());
        assertEquals("First Car Updated", result.getCarName());
    }

    @Test
    void testEditLastCarInRepository() {
        // Add multiple cars to repository
        Car savedCar1 = carRepository.create(car1);
        Car savedCar2 = carRepository.create(car2);
        Car savedCar3 = new Car();
        savedCar3.setCarName("Last Car");
        savedCar3.setCarColor("Purple");
        savedCar3.setCarQuantity(7);
        carRepository.create(savedCar3);

        // Create updated car with ID of last car
        Car updatedCar = new Car();
        updatedCar.setCarId(savedCar3.getCarId());
        updatedCar.setCarName("Last Car Updated");
        updatedCar.setCarColor("Green");
        updatedCar.setCarQuantity(15);

        // Edit the car
        Car result = carRepository.edit(updatedCar);

        // Verify update successful
        assertNotNull(result);
        assertEquals(savedCar3.getCarId(), result.getCarId());
        assertEquals("Last Car Updated", result.getCarName());
    }

    @Test
    void testEditMiddleCarInRepository() {
        // Add multiple cars to repository
        Car savedCar1 = carRepository.create(car1);
        Car savedCar2 = carRepository.create(car2);
        Car savedCar3 = new Car();
        savedCar3.setCarName("Last Car");
        savedCar3.setCarColor("Purple");
        savedCar3.setCarQuantity(7);
        carRepository.create(savedCar3);

        // Create updated car with ID of middle car
        Car updatedCar = new Car();
        updatedCar.setCarId(savedCar2.getCarId());
        updatedCar.setCarName("Middle Car Updated");
        updatedCar.setCarColor("Orange");
        updatedCar.setCarQuantity(12);

        // Edit the car
        Car result = carRepository.edit(updatedCar);

        // Verify update successful
        assertNotNull(result);
        assertEquals(savedCar2.getCarId(), result.getCarId());
        assertEquals("Middle Car Updated", result.getCarName());

        // Verify other cars unchanged
        Car foundCar1 = carRepository.findById(savedCar1.getCarId());
        assertEquals("Toyota Avanza", foundCar1.getCarName());

        Car foundCar3 = carRepository.findById(savedCar3.getCarId());
        assertEquals("Last Car", foundCar3.getCarName());
    }

    @Test
    void testLoopThroughAllCarsWithoutFindingMatch() {
        // Add cars to repository
        Car savedCar1 = carRepository.create(car1);
        Car savedCar2 = carRepository.create(car2);

        // Create a car with non-existent ID
        Car nonExistentCar = new Car();
        nonExistentCar.setCarId("non-existent-id");
        nonExistentCar.setCarName("Ghost Car");
        nonExistentCar.setCarColor("Invisible");
        nonExistentCar.setCarQuantity(0);

        // Try to edit the non-existent car
        Car result = carRepository.edit(nonExistentCar);

        // Verify null is returned
        assertNull(result);

        // Verify repository cars are unchanged
        Car foundCar1 = carRepository.findById(savedCar1.getCarId());
        assertEquals("Toyota Avanza", foundCar1.getCarName());

        Car foundCar2 = carRepository.findById(savedCar2.getCarId());
        assertEquals("Honda Civic", foundCar2.getCarName());
    }

    @Test
    void testEditCompareWithEachCarInRepository() {
        // Add multiple cars to repository
        Car savedCar1 = carRepository.create(car1);
        Car savedCar2 = carRepository.create(car2);

        // Create a car with a different ID that will force comparison with each car
        Car nonMatchingCar = new Car();
        nonMatchingCar.setCarId("different-id");
        nonMatchingCar.setCarName("Different Car");
        nonMatchingCar.setCarColor("White");
        nonMatchingCar.setCarQuantity(3);

        // This will ensure the condition car.getCarId().equals(updatedCar.getCarId()) is checked
        // for each car in the repository but always returns false
        Car result = carRepository.edit(nonMatchingCar);

        // Should return null since no matching car was found
        assertNull(result);

        // Verify all cars in repository are unchanged
        Car foundCar1 = carRepository.findById(savedCar1.getCarId());
        assertEquals("Toyota Avanza", foundCar1.getCarName());

        Car foundCar2 = carRepository.findById(savedCar2.getCarId());
        assertEquals("Honda Civic", foundCar2.getCarName());
    }

    @Test
    void testEditWithMatchingIdButDifferentObject() {
        // Add car to repository
        Car savedCar = carRepository.create(car1);
        String carId = savedCar.getCarId();

        // Create a completely different car object but with same ID
        Car differentCar = new Car();
        differentCar.setCarId(carId); // Same ID
        differentCar.setCarName("Different Name");
        differentCar.setCarColor("Different Color");
        differentCar.setCarQuantity(99);

        // Edit should find by ID equality, not object reference
        Car result = carRepository.edit(differentCar);

        // Verify car was updated
        assertNotNull(result);
        assertEquals(carId, result.getCarId());
        assertEquals("Different Name", result.getCarName());
        assertEquals("Different Color", result.getCarColor());
        assertEquals(99, result.getCarQuantity());
    }
}