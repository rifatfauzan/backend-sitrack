package be_sitruck.backend_sitruck.restservice;

import java.util.List;

import be_sitruck.backend_sitruck.model.Truck;
import be_sitruck.backend_sitruck.restdto.request.CreateTruckRequestDTO;
import be_sitruck.backend_sitruck.restdto.request.UpdateTruckRequestDTO;
import be_sitruck.backend_sitruck.restdto.response.CreateTruckResponseDTO;
// import be_sitruck.backend_sitruck.model.Truck;
import be_sitruck.backend_sitruck.restdto.response.UpdateTruckResponseDTO;

public interface TruckRestService {
    CreateTruckResponseDTO createTruck(CreateTruckRequestDTO createTruckRequestDTO);
    UpdateTruckResponseDTO updateTruck(String vehicleId, UpdateTruckRequestDTO updateTruckRequestDTO);
    List<CreateTruckRequestDTO> getAllTruck();
    CreateTruckRequestDTO getTruckById(String truckId);
    void checkExpiringTrucks();
    long countTrucks();
}