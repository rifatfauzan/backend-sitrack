package be_sitruck.backend_sitruck.restservice;

import java.util.List;
import java.util.Map;

import be_sitruck.backend_sitruck.restdto.request.ApproveSpjRequestDTO;
import be_sitruck.backend_sitruck.restdto.request.CreateSpjRequestDTO;
import be_sitruck.backend_sitruck.restdto.response.SpjResponseDTO;

public interface SpjRestService {
    List<SpjResponseDTO> getAllSpj();
    List<SpjResponseDTO> getAllVehicleOut();
    List<SpjResponseDTO> getAllVehicleIn();
    SpjResponseDTO getSpjById(String id);
    SpjResponseDTO addSpj(CreateSpjRequestDTO spj);
    public Map<String, Integer> getAvailableChassisAndContainers(String orderId);
    SpjResponseDTO approveSpj(ApproveSpjRequestDTO approveRequestDTO);
    void markSpjAsDone(String id);
    // SpjResponseDTO updateSpj(String id, UpdateSpjRequestDTO spj);
}
