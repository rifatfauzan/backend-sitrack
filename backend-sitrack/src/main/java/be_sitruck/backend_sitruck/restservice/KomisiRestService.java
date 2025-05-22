package be_sitruck.backend_sitruck.restservice;

import java.util.List;

import be_sitruck.backend_sitruck.restdto.request.CreateKomisiRequestDTO;
import be_sitruck.backend_sitruck.restdto.response.CreateKomisiResponseDTO;

public interface KomisiRestService {
    CreateKomisiResponseDTO addKomisi (CreateKomisiRequestDTO requestDTO);
    CreateKomisiResponseDTO getKomisiById (String komisiId);
    CreateKomisiResponseDTO updateKomisi (String komisiId, CreateKomisiRequestDTO requestDTO);
    List<CreateKomisiResponseDTO> getAllKomisi ();
    
}
