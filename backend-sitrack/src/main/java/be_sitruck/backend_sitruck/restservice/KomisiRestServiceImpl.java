package be_sitruck.backend_sitruck.restservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be_sitruck.backend_sitruck.model.Komisi;
import be_sitruck.backend_sitruck.model.Spj;
import be_sitruck.backend_sitruck.model.Truck;
import be_sitruck.backend_sitruck.repository.KomisiDb;
import be_sitruck.backend_sitruck.repository.SpjDb;
import be_sitruck.backend_sitruck.repository.TruckDb;
import be_sitruck.backend_sitruck.restdto.request.CreateKomisiRequestDTO;
import be_sitruck.backend_sitruck.restdto.response.CreateKomisiResponseDTO;
import be_sitruck.backend_sitruck.security.jwt.JwtUtils;

@Service
public class KomisiRestServiceImpl implements KomisiRestService {

    @Autowired 
    private TruckDb truckDb;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private KomisiDb komisiDb;

    @Autowired
    private SpjDb spjDb;

    @Override
    public CreateKomisiResponseDTO addKomisi(CreateKomisiRequestDTO requestDTO) {
        Truck truck = truckDb.findById(requestDTO.getTruckId()).orElseThrow(() -> new RuntimeException("Truck not found"));
        
        Komisi komisi = new Komisi();
        String currentUser = jwtUtils.getCurrentUsername();
        komisi.setCommissionFee(requestDTO.getCommissionFee());
        komisi.setLocation(requestDTO.getLocation().toUpperCase());
        komisi.setTruckCommission(requestDTO.getTruckCommission());
        komisi.setTruck(truck);
        komisi.setKomisiId(generateKomisiId());
        komisi.setCreatedBy(currentUser);
        komisi.setCreatedDate(new Date());
        if (komisiDb.existsByTruck_VehicleIdAndLocation(requestDTO.getTruckId(), requestDTO.getLocation().toUpperCase())) {
            throw new IllegalArgumentException(
                "Komisi untuk kendaraan dengan tujuan lokasi ini sudah terbuat"
            );
        }
        //set truckcommision ke truck
        truck.setVehicleCommission(requestDTO.getTruckCommission());
        truckDb.save(truck);
        
        var newKomisi = komisiDb.save(komisi);
        return toDto(newKomisi);
    }

    @Override
    public CreateKomisiResponseDTO getKomisiById(String komisiId) {
       Komisi komisi = komisiDb.findByKomisiId(komisiId);
       if (komisi == null) {
           throw new RuntimeException("Komisi not found");
       }
       return toDto(komisi);
    }

    @Override
    public CreateKomisiResponseDTO updateKomisi(String komisiId, CreateKomisiRequestDTO requestDTO) {
        Komisi existingKomisi = komisiDb.findByKomisiId(komisiId);

        if (existingKomisi == null) {
            throw new IllegalArgumentException("Komisi tidak ditemukan");
        }

        Komisi other = komisiDb.findByTruck_VehicleIdAndLocation(requestDTO.getTruckId(), requestDTO.getLocation().toUpperCase());
        

        existingKomisi.setCommissionFee(requestDTO.getCommissionFee());
        existingKomisi.setLocation(requestDTO.getLocation().toUpperCase());
        existingKomisi.setTruckCommission(requestDTO.getTruckCommission());

        List<Spj> spjList = spjDb.findByVehicleAndCustomer_CityDestination(existingKomisi.getTruck(), existingKomisi.getLocation());
        for (Spj spj : spjList) {
            spj.setCommission(existingKomisi.getCommissionFee() + existingKomisi.getTruckCommission());
            spjDb.save(spj);
        }

        existingKomisi.setTruck(truckDb.findById(requestDTO.getTruckId()).orElseThrow(() -> new RuntimeException("Truck not found")));
        existingKomisi.setUpdatedBy(jwtUtils.getCurrentUsername());
        existingKomisi.setUpdatedDate(new Date());

        if (other != null && !other.getKomisiId().equals(komisiId)) {
            throw new IllegalArgumentException(
                "Komisi untuk kendaraan dengan tujuan lokasi ini sudah terbuat"
            );
        }

        // set truckcommission ke truck
        Truck truck = truckDb.findById(requestDTO.getTruckId())
            .orElseThrow(() -> new RuntimeException("Truck not found"));
        truck.setVehicleCommission(requestDTO.getTruckCommission());
        truckDb.save(truck);

        komisiDb.save(existingKomisi);
        return toDto(existingKomisi);
    }

    @Override
    public List<CreateKomisiResponseDTO> getAllKomisi() {
        List<Komisi> komisi = komisiDb.findAll();
        List<CreateKomisiResponseDTO> komisiResponseDTOs = new ArrayList<>();
        for (Komisi komisiItem : komisi) {
            komisiResponseDTOs.add(toDto(komisiItem));
        }
        return komisiResponseDTOs;
    }

    private CreateKomisiResponseDTO toDto (Komisi komisi){
        var komisiResponseDTO = new CreateKomisiResponseDTO();
        

        komisiResponseDTO.setKomisiId(komisi.getKomisiId());
        komisiResponseDTO.setCommissionFee(komisi.getCommissionFee());
        komisiResponseDTO.setLocation(komisi.getLocation().toUpperCase());
        komisiResponseDTO.setTruckCommission(komisi.getTruckCommission());
        komisiResponseDTO.setTruckId(komisi.getTruck().getVehicleId());
        komisiResponseDTO.setCreatedBy(komisi.getCreatedBy());
        komisiResponseDTO.setCreatedDate(komisi.getCreatedDate());
        komisiResponseDTO.setUpdatedBy(komisi.getUpdatedBy());
        komisiResponseDTO.setUpdatedDate(komisi.getUpdatedDate());

        return komisiResponseDTO;
        
    }


    private String generateKomisiId() {
        Komisi lastKomisi = komisiDb.findTopByKomisiIdStartingWithOrderByKomisiIdDesc("CM");
        int nextNumber = 1;

        if (lastKomisi != null) {
            String lastKomisiId = lastKomisi.getKomisiId();
            try {
                nextNumber = Integer.parseInt(lastKomisiId.substring(3)) + 1;
            } catch (NumberFormatException ignored) {}
        }

        return String.format("CM%06d", nextNumber);
    }
    
}
