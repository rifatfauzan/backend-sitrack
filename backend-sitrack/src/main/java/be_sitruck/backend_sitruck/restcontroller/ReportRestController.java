package be_sitruck.backend_sitruck.restcontroller;

import be_sitruck.backend_sitruck.model.*;
import be_sitruck.backend_sitruck.restdto.request.ReportFilterRequestDTO;
import be_sitruck.backend_sitruck.restservice.ReportRestService;
import be_sitruck.backend_sitruck.util.ExcelExporter;
import be_sitruck.backend_sitruck.util.PDFExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = {"https://sitrack.up.railway.app", "http://localhost:*", "http://127.0.0.1:*"}, allowCredentials = "true")
public class ReportRestController {

    @Autowired
    private ReportRestService reportRestService;

    @Autowired
    private PDFExporter pdfExporter;

    @Autowired
    private ExcelExporter excelExporter;

    @PostMapping("/generate")
    public ResponseEntity<?> generateReport(@RequestBody ReportFilterRequestDTO filter) {
        try {
            Object reportData = reportRestService.generateReport(filter);
            return ResponseEntity.ok(reportData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/export/pdf")
    public ResponseEntity<?> exportToPDF(@RequestBody ReportFilterRequestDTO filter) {
        try {
            Object reportData = reportRestService.generateReport(filter);
            byte[] pdfBytes;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fromDateStr = filter.getFromDate() != null ? sdf.format(filter.getFromDate()) : "";
            String endDateStr = filter.getEndDate() != null ? sdf.format(filter.getEndDate()) : "";

            switch (filter.getReportType()) {
                case "ALL_CUSTOMERS":
                    pdfBytes = pdfExporter.exportCustomersToPDF((List<Customer>) reportData);
                    break;
                case "ALL_VEHICLES":
                    pdfBytes = pdfExporter.exportVehiclesToPDF((List<Truck>) reportData);
                    break;
                case "ALL_CHASSIS":
                    pdfBytes = pdfExporter.exportChassisToPDF((List<Chassis>) reportData);
                    break;
                case "ALL_DRIVERS":
                    pdfBytes = pdfExporter.exportDriversToPDF((List<SopirModel>) reportData);
                    break;
                case "ALL_ORDERS":
                    pdfBytes = pdfExporter.exportOrdersToPDF((List<Order>) reportData, fromDateStr, endDateStr);
                    break;
                case "ALL_SPJ":
                    pdfBytes = pdfExporter.exportSpjToPDF((List<Spj>) reportData, fromDateStr, endDateStr);
                    break;
                default:
                    return ResponseEntity.badRequest().body("Invalid report type");
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filter.getReportType().toLowerCase() + "_report.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/export/excel")
    public ResponseEntity<?> exportToExcel(@RequestBody ReportFilterRequestDTO filter) {
        try {
            Object reportData = reportRestService.generateReport(filter);
            byte[] excelBytes;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fromDateStr = filter.getFromDate() != null ? sdf.format(filter.getFromDate()) : "";
            String endDateStr = filter.getEndDate() != null ? sdf.format(filter.getEndDate()) : "";
            String logoPath = "src/main/resources/static/GIB.png";
            String period = (!fromDateStr.isEmpty() && !endDateStr.isEmpty()) ? ("Order period: " + fromDateStr + " until " + endDateStr) : "";
            switch (filter.getReportType()) {
                case "ALL_CUSTOMERS":
                    excelBytes = excelExporter.exportCustomersToExcel((List<Customer>) reportData, "Customer Report", "", logoPath);
                    break;
                case "ALL_VEHICLES":
                    excelBytes = excelExporter.exportVehiclesToExcel((List<Truck>) reportData, "Vehicle Report", "", logoPath);
                    break;
                case "ALL_CHASSIS":
                    excelBytes = excelExporter.exportChassisToExcel((List<Chassis>) reportData, "Chassis Report", "", logoPath);
                    break;
                case "ALL_DRIVERS":
                    excelBytes = excelExporter.exportDriversToExcel((List<SopirModel>) reportData, "Driver Report", "", logoPath);
                    break;
                case "ALL_ORDERS":
                    excelBytes = excelExporter.exportOrdersToExcel((List<Order>) reportData, "Order Report", period, logoPath);
                    break;
                case "ALL_SPJ":
                    excelBytes = excelExporter.exportSpjToExcel((List<Spj>) reportData, "SPJ Report", period, logoPath);
                    break;
                default:
                    return ResponseEntity.badRequest().body("Invalid report type");
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", filter.getReportType().toLowerCase() + "_report.xlsx");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelBytes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 