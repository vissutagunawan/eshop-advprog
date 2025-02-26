package id.ac.ui.cs.advprog.eshop.service;
import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.repository.InterfaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class CarServiceImpl implements CarService{
    @Autowired
    private InterfaceRepository<Car> carRepository;

    @Override
    public Car create(Car car){
        // TODO Auto-generated method stub
        carRepository.create(car);
        return car;
    }

    @Override
    public List<Car> findAll(){
        Iterator<Car> carIterator = carRepository.findAll();
        List<Car> allCar = new ArrayList<>();
        carIterator.forEachRemaining(allCar::add);
        return allCar;
    }

    @Override
    public Car findById(String carId) {
        Car car = carRepository.findById(carId);
        return car;
    }

    @Override
    public Car edit(Car updatedCar) {
        // TODO Auto-generated method stub
        if (findById(updatedCar.getCarId()) == null) {
            return null;
        }
        carRepository.edit(updatedCar);
        return updatedCar;
    }

    @Override
    public boolean deleteCarById(String carId) {
        // TODO Auto-generated method stub
        Car car = carRepository.findById(carId);
        if (car != null) {
            carRepository.delete(carId);
            return true;
        }
        return false;
    }
}
