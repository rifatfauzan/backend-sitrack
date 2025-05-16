package be_sitruck.backend_sitruck.restservice;

import be_sitruck.backend_sitruck.model.*;
import be_sitruck.backend_sitruck.repository.*;
import be_sitruck.backend_sitruck.restdto.request.*;
import be_sitruck.backend_sitruck.restdto.response.*;
import be_sitruck.backend_sitruck.security.jwt.JwtUtils;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportTruckRestServiceImpl implements ReportTruckRestService {
    @Autowired
    private TruckDb truckDb;
    @Autowired
    private ReportTruckDb reportTruckDb;
    @Autowired
    private ReportTruckAssetDb reportTruckAssetDb;
    @Autowired
    private AssetDb assetDb;
    @Autowired
    private JwtUtils jwtUtils;

    //create
    @Override
    public ReportTruckResponseDTO createReportTruck(ReportTruckRequestDTO request) {
        String currentUser = jwtUtils.getCurrentUsername();

        String maxReportTruckId = reportTruckDb.findMaxReportTruckId();
        int nextNumber = 1;
        if (maxReportTruckId != null && maxReportTruckId.length() > 2) {
            String numberPart = maxReportTruckId.substring(maxReportTruckId.length() - 5);
            try {
                nextNumber = Integer.parseInt(numberPart) + 1;
            } catch (NumberFormatException e) {
                nextNumber = 1;
            }
        }
        String paddedNumber = String.format("%05d", nextNumber);
        String generatedReportTruckId = "VM" + paddedNumber;

        ReportTruck report = new ReportTruck();
        report.setReportTruckId(generatedReportTruckId);
        report.setDate(request.getDate());
        report.setStartRepair(request.getStartRepair());
        report.setFinishRepair(request.getFinishRepair());
        report.setVehicleId(request.getVehicleId());
        report.setDescription(request.getDescription());
        report.setCreatedBy(currentUser);

        Truck truck = truckDb.findByVehicleId(request.getVehicleId());
        if (truck == null) {
            throw new ValidationException("Truck dengan vehicleId " + request.getVehicleId() + " tidak ditemukan");
        }

        report.setVehiclePlateNo(truck.getVehiclePlateNo());
        report.setVehicleType(truck.getVehicleType());

        report.setAssets(new ArrayList<>());
        reportTruckDb.save(report);

        List<ReportTruckAsset> items = request.getAssets().stream().map(itemDTO -> {
            Asset asset = assetDb.findByAssetId(itemDTO.getAssetId());
            if (asset == null) {
                throw new ValidationException("Asset ID " + itemDTO.getAssetId() + " tidak ditemukan");
            }
            if (asset.getJumlahStok() < itemDTO.getQuantity()) {
                throw new ValidationException("Stok asset ID " + itemDTO.getAssetId() + " tidak cukup");
            }
            asset.setJumlahStok(asset.getJumlahStok() - itemDTO.getQuantity());
            assetDb.save(asset);

            ReportTruckAsset item = new ReportTruckAsset();
            item.setReportTruck(report);
            item.setAssetId(itemDTO.getAssetId());
            item.setQuantity(itemDTO.getQuantity());
            return item;
        }).collect(Collectors.toList());

        reportTruckAssetDb.saveAll(items);

        return new ReportTruckResponseDTO(
            generatedReportTruckId,
            "Laporan perawatan berhasil dibuat"
        );
    }

    // get all
    @Override
    public List<ReportTruckRequestDTO> getAllReportTrucks() {
        return reportTruckDb.findAllOrdered().stream()
            .map(this::convertToReportTruckDTO)
            .collect(Collectors.toList());
    }

    private ReportTruckRequestDTO convertToReportTruckDTO(ReportTruck report) {
        ReportTruckRequestDTO dto = new ReportTruckRequestDTO();
        dto.setReportTruckId(report.getReportTruckId());
        dto.setDate(report.getDate());
        dto.setStartRepair(report.getStartRepair());
        dto.setFinishRepair(report.getFinishRepair());
        dto.setVehicleId(report.getVehicleId());
        dto.setVehiclePlateNo(report.getVehiclePlateNo());
        dto.setVehicleType(report.getVehicleType());
        dto.setDescription(report.getDescription());
        dto.setCreatedBy(report.getCreatedBy());

        List<ReportTruckAssetDTO> assets = report.getAssets().stream().map(asset -> {
            ReportTruckAssetDTO a = new ReportTruckAssetDTO();
            a.setAssetId(asset.getAssetId());
            a.setQuantity(asset.getQuantity());
            return a;
        }).collect(Collectors.toList());

        dto.setAssets(assets);
        return dto;
    }

}
