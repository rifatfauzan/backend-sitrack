package be_sitruck.backend_sitruck.restservice;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import be_sitruck.backend_sitruck.model.SopirModel;
import be_sitruck.backend_sitruck.repository.SopirDb;
import be_sitruck.backend_sitruck.restdto.response.CreateSopirResponseDTO;
import be_sitruck.backend_sitruck.restdto.request.CreateSopirRequestDTO;
import be_sitruck.backend_sitruck.security.jwt.JwtUtils;

@Service
@Transactional
public class SopirRestServiceImpl implements SopirRestService {

    @Autowired
    private JwtUtils  jwtUtils;

    @Autowired
    SopirDb sopirDb;

    @Override
    public CreateSopirResponseDTO addSopir(CreateSopirRequestDTO sopirDTO) {
        String currentUser = jwtUtils.getCurrentUsername();
        SopirModel existingSopir = sopirDb.findByDriverKTPNo(sopirDTO.getDriver_KTP_No());

        if(existingSopir != null){
            throw new IllegalArgumentException("Sopir dengan NIK tersebut sudah ada");
        }

        SopirModel sopir = new SopirModel();
        var driverId = generateId(sopirDTO);

        sopir.setDriverId(driverId);
        sopir.setDriverName(sopirDTO.getDriverName());

        if(!sopirDTO.getDriver_KTP_No().matches("[0-9]+")){
            throw new IllegalArgumentException("Nomor KTP Driver (NIK) harus berupa angka");
        }
        sopir.setDriver_KTP_No(sopirDTO.getDriver_KTP_No());
        sopir.setDriver_KTP_Date(sopirDTO.getDriver_KTP_Date());

        if(!sopirDTO.getDriver_SIM_No().matches("[0-9]+")){
            throw new IllegalArgumentException("Nomor SIM Driver harus berupa angka");
        }
        sopir.setDriver_SIM_No(sopirDTO.getDriver_SIM_No());
        sopir.setDriver_SIM_Date(sopirDTO.getDriver_SIM_Date());

        if(!sopirDTO.getDriverContact().matches("[0-9]+") & !sopirDTO.getDriverContact().isBlank()){
            throw new IllegalArgumentException("Nomor Kontak Driver harus berupa angka");
        }
        sopir.setDriverContact(sopirDTO.getDriverContact());
        sopir.setDriverCo(sopirDTO.getDriverCo());

        if(!sopirDTO.getDriverCoContact().matches("[0-9]+") & !sopirDTO.getDriverCoContact().isBlank()){
            throw new IllegalArgumentException("Nomor Kontak Co-Driver harus berupa angka");
        }
        sopir.setDriverCoContact(sopirDTO.getDriverCoContact());

        if (sopirDTO.getSiteId() != null) {
            sopir.setSiteId(sopirDTO.getSiteId().toUpperCase());
        } else {
            sopir.setSiteId("JKT");
        }

        if (sopirDTO.getRecordStatus() != null) {
            sopir.setRecordStatus(sopirDTO.getRecordStatus());
        } else {
            sopir.setRecordStatus("A");
        }

        if (sopirDTO.getRowStatus() != null) {
            sopir.setRowStatus(sopirDTO.getRowStatus());
        } else {
            sopir.setRowStatus("A");
        }

        sopir.setDriverNumber(sopirDTO.getDriverNumber());
        sopir.setDriverRemarks(sopirDTO.getDriverRemarks());
        sopir.setDriverType(sopirDTO.getDriverType());
        sopir.setDriverJoinDate(sopirDTO.getDriverJoinDate());
        sopir.setCreatedDate(Date.from(Instant.now()));
        sopir.setCreatedBy(currentUser);
        sopir.setUpdatedBy(null);
        sopir.setUpdatedDate(null);

        var newSopir = sopirDb.save(sopir);
        return sopirToSopirResponseDTO(newSopir);

    }

    private CreateSopirResponseDTO sopirToSopirResponseDTO(SopirModel sopir){
        var sopirResponseDTO = new CreateSopirResponseDTO();

        sopirResponseDTO.setDriverId(sopir.getDriverId());
        sopirResponseDTO.setDriverName(sopir.getDriverName());
        sopirResponseDTO.setDriver_KTP_No(sopir.getDriver_KTP_No());
        sopirResponseDTO.setDriver_KTP_Date(sopir.getDriver_KTP_Date());
        sopirResponseDTO.setDriver_SIM_No(sopir.getDriver_SIM_No());
        sopirResponseDTO.setDriver_SIM_Date(sopir.getDriver_SIM_Date());
        sopirResponseDTO.setDriverContact(sopir.getDriverContact());
        sopirResponseDTO.setDriverCo(sopir.getDriverCo());
        sopirResponseDTO.setDriverCoContact(sopir.getDriverCoContact());
        sopirResponseDTO.setSiteId(sopir.getSiteId());
        sopirResponseDTO.setDriverNumber(sopir.getDriverNumber());
        sopirResponseDTO.setDriverRemarks(sopir.getDriverRemarks());
        sopirResponseDTO.setRecordStatus(sopir.getRecordStatus());
        sopirResponseDTO.setDriverType(sopir.getDriverType());
        sopirResponseDTO.setDriverJoinDate(sopir.getDriverJoinDate());
        sopirResponseDTO.setCreatedBy(sopir.getCreatedBy());
        sopirResponseDTO.setCreatedDate(sopir.getCreatedDate());
        sopirResponseDTO.setUpdatedBy(sopir.getUpdatedBy());
        sopirResponseDTO.setUpdatedDate(sopir.getUpdatedDate());
        sopirResponseDTO.setRowStatus(sopir.getRowStatus());

        return sopirResponseDTO;
    }


    @Override
    public List<CreateSopirResponseDTO> viewAllSopir() {
        List<SopirModel> sopirModel = sopirDb.findAll();
        List<CreateSopirResponseDTO> sopirResponseDTOList = new ArrayList<>();
        
        for(SopirModel sopir : sopirModel){
            sopirResponseDTOList.add(sopirToSopirResponseDTO(sopir));
        }

        return sopirResponseDTOList;
    }

    @Override
    public CreateSopirResponseDTO viewSopirById(String driverId) {
        SopirModel sopir = sopirDb.findById(driverId).get();

        if(sopir == null){
            return null;
        }
        
        return sopirToSopirResponseDTO(sopir);

    }

    @Override
    public CreateSopirResponseDTO updateSopir(String driverId, CreateSopirRequestDTO sopirDTO) {
        
       SopirModel existingSopir = sopirDb.findById(driverId).orElse(null);
       Date today = new Date();

        if(existingSopir == null){
              throw new IllegalArgumentException("Sopir tidak ditemukan");
        }
        existingSopir.setDriverName(sopirDTO.getDriverName());
        existingSopir.setDriver_KTP_No(sopirDTO.getDriver_KTP_No());
        existingSopir.setDriver_KTP_Date(sopirDTO.getDriver_KTP_Date());

        if(!sopirDTO.getDriver_SIM_No().matches("[0-9]+")){
            throw new IllegalArgumentException("Nomor SIM Driver harus berupa angka");
        }
        existingSopir.setDriver_SIM_No(sopirDTO.getDriver_SIM_No());

        if (sopirDTO.getDriver_SIM_Date().before(today)) {
            throw new IllegalArgumentException("Anda tidak bisa menginput tanggal SIM yang sudah lewat atau belum diperpanjang");
        }
        existingSopir.setDriver_SIM_Date(sopirDTO.getDriver_SIM_Date());

        if(!sopirDTO.getDriverContact().matches("[0-9]+") && !sopirDTO.getDriverContact().isBlank()){  
            throw new IllegalArgumentException("Nomor Kontak Driver harus berupa angka");
        }
        existingSopir.setDriverContact(sopirDTO.getDriverContact());
        existingSopir.setDriverCo(sopirDTO.getDriverCo());

        if(!sopirDTO.getDriverCoContact().matches("[0-9]+") && !sopirDTO.getDriverCoContact().isBlank()){
            throw new IllegalArgumentException("Nomor Kontak Co-Driver harus berupa angka");
        }
        existingSopir.setDriverCoContact(sopirDTO.getDriverCoContact());
        existingSopir.setSiteId(sopirDTO.getSiteId());

        existingSopir.setDriverNumber(sopirDTO.getDriverNumber());
        existingSopir.setDriverRemarks(sopirDTO.getDriverRemarks());
        existingSopir.setRecordStatus(sopirDTO.getRecordStatus());
        existingSopir.setDriverType(sopirDTO.getDriverType());
        existingSopir.setDriverJoinDate(sopirDTO.getDriverJoinDate());
        existingSopir.setUpdatedDate(Date.from(Instant.now()));
        existingSopir.setUpdatedBy(jwtUtils.getCurrentUsername());
        existingSopir.setRowStatus(sopirDTO.getRowStatus());

        sopirDb.save(existingSopir);
        return sopirToSopirResponseDTO(existingSopir);

    }

    @Override
    public String generateId(CreateSopirRequestDTO sopirDTO) {
       String namaDriver = "DR";
       long totalSopir = sopirDb.count();
       String increment = String.format("%06d", totalSopir + 1);
        return namaDriver + increment;
    }



    
    
    
}
