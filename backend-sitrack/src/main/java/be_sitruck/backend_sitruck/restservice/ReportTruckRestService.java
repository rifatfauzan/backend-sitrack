package be_sitruck.backend_sitruck.restservice;

import java.util.List;

// import be_sitruck.backend_sitruck.model.ReportTruck;
import be_sitruck.backend_sitruck.restdto.request.ReportTruckRequestDTO;
import be_sitruck.backend_sitruck.restdto.response.ReportTruckResponseDTO;

public interface ReportTruckRestService {
    ReportTruckResponseDTO createReportTruck(ReportTruckRequestDTO request);
    List<ReportTruckRequestDTO> getAllReportTrucks();
}