package id.ac.ui.cs.advprog.eshop.repository;
import id.ac.ui.cs.advprog.eshop.model.Car;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.List;

@Repository
public class CarRepository implements InterfaceRepository<Car>{
    static int id = 0;
    private List<Car> carData = new ArrayList<>();
    public Car create(Car car){
        if (car.getCarId() == null){
            UUID uuid = UUID.randomUUID();
            car.setCarId(uuid.toString());
        }
        carData.add(car);
        return car;
    }

    public Iterator<Car> findAll(){
        return carData.iterator();
    }

    public Car findById(String id){
        for (Car car: carData){
            if (car.getCarId().equals(id)){
                return car;
            }
        }
        return null;
    }

    public Car edit(Car updatedCar){
        for (int i=0; i<carData.size(); i++){
            Car car = carData.get(i);
            if (car.getCarId().equals(updatedCar.getCarId())){
                car.setCarName(updatedCar.getCarName());
                car.setCarColor(updatedCar.getCarColor());
                car.setCarQuantity(updatedCar.getCarQuantity());
                return car;
            }
        }
        return null;
    }

    public boolean delete(String id) {
        if (findById(id) == null){
            return false;
        }
        carData.removeIf(car -> car.getCarId().equals(id));
        return true;
    }
}