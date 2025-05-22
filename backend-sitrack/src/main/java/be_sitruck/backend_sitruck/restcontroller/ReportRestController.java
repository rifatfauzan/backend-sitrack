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
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.stream.Collectors;

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
            Map<String, Object> response = new HashMap<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            
            switch (filter.getReportType()) {
                case "ALL_CUSTOMERS":
                    List<Customer> customers = (List<Customer>) reportData;
                    List<Map<String, String>> formattedCustomers = customers.stream()
                        .map(customer -> {
                            Map<String, String> row = new HashMap<>();
                            row.put("Customer ID", customer.getId());
                            row.put("Customer Name", customer.getName());
                            row.put("Site ID", customer.getSiteId());
                            row.put("Address", customer.getAddress());
                            row.put("Destination", customer.getCityDestination());
                            return row;
                        })
                        .collect(Collectors.toList());
                    response.put("data", formattedCustomers);
                    response.put("columns", Arrays.asList("Customer ID", "Customer Name", "Site ID", "Address", "Destination"));
                    break;

                case "ALL_COMMISSIONS":
                    List<Komisi> komisiList = (List<Komisi>) reportData;
                    List<Map<String, String>> formattedKomisi = komisiList.stream()
                        .map(komisi -> {
                            Map<String, String> row = new HashMap<>();
                            row.put("Commission ID", komisi.getKomisiId());
                            row.put("Truck ID", komisi.getTruck().getVehicleId());
                            row.put("Vehicle Name", komisi.getTruck().getVehicleBrand());
                            row.put("Location", komisi.getLocation());
                            row.put("Truck Commission Fee", String.valueOf(komisi.getTruckCommission()));
                            row.put ("Commission Fee", String.valueOf(komisi.getCommissionFee()));

                            return row;
                        })
                        .collect(Collectors.toList());
                    response.put("data", formattedKomisi);
                    response.put("columns", Arrays.asList("Commission ID", "Truck ID", "Vehicle Name", "Location", "Truck Commission Fee", "Commission Fee"));
                    break;
                    
                case "ALL_VEHICLES":
                    List<Truck> vehicles = (List<Truck>) reportData;
                    List<Map<String, String>> formattedVehicles = vehicles.stream()
                        .map(vehicle -> {
                            Map<String, String> row = new HashMap<>();
                            row.put("Vehicle ID", vehicle.getVehicleId());
                            row.put("Vehicle Brand", vehicle.getVehicleBrand());
                            row.put("Year", vehicle.getVehicleYear());
                            row.put("Type", vehicle.getVehicleType());
                            row.put("Plate No.", vehicle.getVehiclePlateNo());
                            row.put("KIR No.", vehicle.getVehicleKIRNo());
                            row.put("STNK Expiration", sdf.format(vehicle.getVehicleSTNKDate()));
                            row.put("KIR Expiration", sdf.format(vehicle.getVehicleKIRDate()));
                            return row;
                        })
                        .collect(Collectors.toList());
                    response.put("data", formattedVehicles);
                    response.put("columns", Arrays.asList("Vehicle ID", "Vehicle Brand", "Year", "Type", "Plate No.", "KIR No.", "STNK Expiration", "KIR Expiration"));
                    break;
                    
                case "ALL_CHASSIS":
                    List<Chassis> chassisList = (List<Chassis>) reportData;
                    List<Map<String, String>> formattedChassis = chassisList.stream()
                        .map(chassis -> {
                            Map<String, String> row = new HashMap<>();
                            row.put("Chassis ID", chassis.getChassisId());
                            row.put("Year", chassis.getChassisYear());
                            row.put("Size", String.valueOf(chassis.getChassisSize()));
                            row.put("Type", chassis.getChassisType());
                            row.put("Chassis No.", chassis.getChassisNumber());
                            row.put("KIR No.", chassis.getChassisKIRNo());
                            row.put("KIR Expiration", sdf.format(chassis.getChassisKIRDate()));
                            return row;
                        })
                        .collect(Collectors.toList());
                    response.put("data", formattedChassis);
                    response.put("columns", Arrays.asList("Chassis ID", "Year", "Size", "Type", "Chassis No.", "KIR No.", "KIR Expiration"));
                    break;
                    
                case "ALL_DRIVERS":
                    List<SopirModel> drivers = (List<SopirModel>) reportData;
                    List<Map<String, String>> formattedDrivers = drivers.stream()
                        .map(driver -> {
                            Map<String, String> row = new HashMap<>();
                            row.put("Driver ID", driver.getDriverId());
                            row.put("Driver Name", driver.getDriverName());
                            row.put("KTP No.", driver.getDriver_KTP_No());
                            row.put("SIM No.", driver.getDriver_SIM_No());
                            row.put("Join Date", sdf.format(driver.getDriverJoinDate()));
                            return row;
                        })
                        .collect(Collectors.toList());
                    response.put("data", formattedDrivers);
                    response.put("columns", Arrays.asList("Driver ID", "Driver Name", "KTP No.", "SIM No.", "Join Date"));
                    break;
                    
                case "ALL_ORDERS":
                    List<Order> orders = (List<Order>) reportData;
                    List<Map<String, String>> formattedOrders = orders.stream()
                        .map(order -> {
                            Map<String, String> row = new HashMap<>();
                            row.put("Order ID", order.getOrderId());
                            row.put("Customer ID", order.getCustomer().getId());
                            row.put("Customer Name", order.getCustomer().getName());
                            row.put("Order Date", sdf.format(order.getOrderDate()));
                            row.put("Move Type", order.getMoveType());
                            row.put("Down Payment", String.valueOf(order.getDownPayment()));
                            row.put("20' Chassis Qty", String.valueOf(order.getQtyChassis20()));
                            row.put("40' Chassis Qty", String.valueOf(order.getQtyChassis40()));
                            return row;
                        })
                        .collect(Collectors.toList());
                    response.put("data", formattedOrders);
                    response.put("columns", Arrays.asList("Order ID", "Customer ID", "Customer Name", "Order Date", "Move Type", "Down Payment", "20' Chassis Qty", "40' Chassis Qty"));
                    break;
                    
                case "ALL_SPJ":
                    List<Spj> spjList = (List<Spj>) reportData;
                    List<Map<String, String>> formattedSpj = spjList.stream()
                        .map(spj -> {
                            Map<String, String> row = new HashMap<>();
                            row.put("SPJ ID", spj.getId());
                            row.put("Order ID", spj.getOrder().getOrderId());
                            row.put("Customer ID", spj.getCustomer().getId());
                            row.put("Customer Name", spj.getCustomer().getName());
                            row.put("Driver Name", spj.getDriver().getDriverName());
                            row.put("Vehicle ID", spj.getVehicle().getVehicleId());
                            row.put("Chassis ID", spj.getChassis().getChassisId());
                            row.put("Chassis Size", String.valueOf(spj.getChassisSize()));
                            row.put("Chassis Type", spj.getChassis().getChassisType());
                            row.put("Container Qty", String.valueOf(spj.getContainerQty()));
                            row.put("Container Type", spj.getContainerType());
                            row.put("Commission", String.valueOf(spj.getCommission()));
                            row.put("Other Commission", String.valueOf(spj.getOthersCommission()));
                            row.put("Date Out", sdf.format(spj.getDateOut()));
                            row.put("Date In", sdf.format(spj.getDateIn()));
                            return row;
                        })
                        .collect(Collectors.toList());
                    response.put("data", formattedSpj);
                    response.put("columns", Arrays.asList("SPJ ID", "Order ID", "Customer ID", "Customer Name", "Driver Name", "Vehicle ID", "Chassis ID", "Chassis Size", "Chassis Type", "Container Qty", "Container Type", "Commission", "Other Commission", "Date Out", "Date In"));
                    break;
                    
                default:
                    return ResponseEntity.badRequest().body("Invalid report type");
            }
            
            return ResponseEntity.ok(response);
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
                case "ALL_COMMISSIONS":
                    pdfBytes = pdfExporter.exportKomisiToPDF((List<Komisi>) reportData);
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
                case "ALL_COMMISSIONS":
                    excelBytes = excelExporter.exportCommissionsToExcel((List<Komisi>) reportData, "Commissions Report", "", logoPath);
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