package be_sitruck.backend_sitruck.restservice;

import java.util.List;
import java.util.UUID;

import be_sitruck.backend_sitruck.model.SopirModel;
import be_sitruck.backend_sitruck.restdto.request.CreateSopirRequestDTO;
import be_sitruck.backend_sitruck.restdto.response.CreateSopirResponseDTO;

public interface SopirRestService {
    CreateSopirResponseDTO addSopir (CreateSopirRequestDTO sopirDTO);
    List<CreateSopirResponseDTO> viewAllSopir();
    CreateSopirResponseDTO viewSopirById(String driverId);
    CreateSopirResponseDTO updateSopir(String driverId, CreateSopirRequestDTO sopirDTO);
    String generateId (CreateSopirRequestDTO sopirDTO);
    void checkExpiringDriver();
    long countSopir();
}
