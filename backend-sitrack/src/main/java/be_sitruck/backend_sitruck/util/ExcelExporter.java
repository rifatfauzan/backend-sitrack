package be_sitruck.backend_sitruck.util;

import be_sitruck.backend_sitruck.model.*;
import be_sitruck.backend_sitruck.repository.AssetDb;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

@Component
public class ExcelExporter {

    @Autowired
    private AssetDb assetDb;

    public byte[] exportCustomersToExcel(List<Customer> customers, String reportTitle, String period, String logoPath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Customers");
            String companyInfo = "PT. GLORIOUS INTERBUANA\nKawasan Berikat Nusatara Marunda\nJl. Medan No. 4 Kav. C 18/2\nJakarta Utara - Indonesia";
            String[] columns = {"Customer ID", "Customer Name", "Site ID", "Address", "Destination"};
            int startRow = addExcelHeader(workbook, sheet, logoPath, companyInfo, reportTitle, period, columns.length);
            Row headerRow = sheet.createRow(startRow);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }
            int rowNum = startRow + 1;
            for (Customer customer : customers) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(customer.getId());
                row.createCell(1).setCellValue(customer.getName());
                row.createCell(2).setCellValue(customer.getSiteId());
                row.createCell(3).setCellValue(customer.getAddress());
                row.createCell(4).setCellValue(customer.getCityDestination());
            }
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to export customers to Excel", e);
        }
    }

    public byte[] exportCustomersToExcel(List<Customer> customers) {
        return exportCustomersToExcel(customers, "Customer Report", "", "src/main/resources/static/GIB.png");
    }

    public byte[] exportVehiclesToExcel(List<Truck> vehicles, String reportTitle, String period, String logoPath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Vehicles");
            String companyInfo = "PT. GLORIOUS INTERBUANA\n" +
                "Kawasan Berikat Nusatara Marunda\n" +
                "Jl. Medan No. 4 Kav. C 18/2\n" +
                "Jakarta Utara - Indonesia";
            String[] columns = {"Vehicle ID", "Vehicle Brand", "Year", "Type", "Plate No.", "KIR No.", "STNK Expiration", "KIR Expiration"};
            int startRow = addExcelHeader(workbook, sheet, logoPath, companyInfo, reportTitle, period, columns.length);
            Row headerRow = sheet.createRow(startRow);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }
            int rowNum = startRow + 1;
            for (Truck vehicle : vehicles) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(vehicle.getVehicleId());
                row.createCell(1).setCellValue(vehicle.getVehicleBrand());
                row.createCell(2).setCellValue(vehicle.getVehicleYear());
                row.createCell(3).setCellValue(vehicle.getVehicleType());
                row.createCell(4).setCellValue(vehicle.getVehiclePlateNo());
                row.createCell(5).setCellValue(vehicle.getVehicleKIRNo());
                row.createCell(6).setCellValue(vehicle.getVehicleSTNKDate().toString());
                row.createCell(7).setCellValue(vehicle.getVehicleKIRDate().toString());
            }
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to export vehicles to Excel", e);
        }
    }

    public byte[] exportVehiclesToExcel(List<Truck> vehicles) {
        return exportVehiclesToExcel(vehicles, "Vehicle Report", "", "src/main/resources/static/GIB.png");
    }

    public byte[] exportChassisToExcel(List<Chassis> chassisList, String reportTitle, String period, String logoPath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Chassis");
            String companyInfo = "PT. GLORIOUS INTERBUANA\nKawasan Berikat Nusatara Marunda\nJl. Medan No. 4 Kav. C 18/2\nJakarta Utara - Indonesia";
            String[] columns = {"Chassis ID", "Year", "Size", "Type", "Chassis No.", "KIR No.", "KIR Expiration"};
            int startRow = addExcelHeader(workbook, sheet, logoPath, companyInfo, reportTitle, period, columns.length);
            Row headerRow = sheet.createRow(startRow);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }
            int rowNum = startRow + 1;
            for (Chassis chassis : chassisList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(chassis.getChassisId());
                row.createCell(1).setCellValue(chassis.getChassisYear());
                row.createCell(2).setCellValue(chassis.getChassisSize());
                row.createCell(3).setCellValue(chassis.getChassisType());
                row.createCell(4).setCellValue(chassis.getChassisNumber());
                row.createCell(5).setCellValue(chassis.getChassisKIRNo());
                row.createCell(6).setCellValue(chassis.getChassisKIRDate().toString());
            }
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to export chassis to Excel", e);
        }
    }

    public byte[] exportChassisToExcel(List<Chassis> chassisList) {
        return exportChassisToExcel(chassisList, "Chassis Report", "", "src/main/resources/static/GIB.png");
    }

    public byte[] exportDriversToExcel(List<SopirModel> drivers, String reportTitle, String period, String logoPath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Drivers");
            String companyInfo = "PT. GLORIOUS INTERBUANA\nKawasan Berikat Nusatara Marunda\nJl. Medan No. 4 Kav. C 18/2\nJakarta Utara - Indonesia";
            String[] columns = {"Driver ID", "Driver Name", "KTP No.", "SIM No.", "Join Date"};
            int startRow = addExcelHeader(workbook, sheet, logoPath, companyInfo, reportTitle, period, columns.length);
            Row headerRow = sheet.createRow(startRow);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }
            int rowNum = startRow + 1;
            for (SopirModel driver : drivers) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(driver.getDriverId());
                row.createCell(1).setCellValue(driver.getDriverName());
                row.createCell(2).setCellValue(driver.getDriver_KTP_No());
                row.createCell(3).setCellValue(driver.getDriver_SIM_No());
                row.createCell(4).setCellValue(driver.getDriverJoinDate().toString());
            }
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to export drivers to Excel", e);
        }
    }

    public byte[] exportDriversToExcel(List<SopirModel> drivers) {
        return exportDriversToExcel(drivers, "Driver Report", "", "src/main/resources/static/GIB.png");
    }

    public byte[] exportSpjToExcel(List<Spj> spjList, String reportTitle, String period, String logoPath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("SPJ");
            String companyInfo = "PT. GLORIOUS INTERBUANA\nKawasan Berikat Nusatara Marunda\nJl. Medan No. 4 Kav. C 18/2\nJakarta Utara - Indonesia";
            String[] columns = {"SPJ ID", "Order ID", "Customer ID", "Customer Name", "Driver Name", "Vehicle ID", "Chassis ID", "Chassis Size", "Chassis Type", "Container Qty", "Container Type", "Commission", "Other Commission", "Date Out", "Date In"};
            int startRow = addExcelHeader(workbook, sheet, logoPath, companyInfo, reportTitle, period, columns.length);
            Row headerRow = sheet.createRow(startRow);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }
            int rowNum = startRow + 1;
            for (Spj spj : spjList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(spj.getId());
                row.createCell(1).setCellValue(spj.getOrder().getOrderId());
                row.createCell(2).setCellValue(spj.getCustomer().getId());
                row.createCell(3).setCellValue(spj.getCustomer().getName());
                row.createCell(4).setCellValue(spj.getDriver().getDriverName());
                row.createCell(5).setCellValue(spj.getVehicle().getVehicleId());
                row.createCell(6).setCellValue(spj.getChassis().getChassisId());
                row.createCell(7).setCellValue(spj.getChassisSize());
                row.createCell(8).setCellValue(spj.getChassis().getChassisType());
                row.createCell(9).setCellValue(spj.getContainerQty());
                row.createCell(10).setCellValue(spj.getContainerType());
                row.createCell(11).setCellValue(spj.getCommission());
                row.createCell(12).setCellValue(spj.getOthersCommission());
                row.createCell(13).setCellValue(spj.getDateOut().toString());
                row.createCell(14).setCellValue(spj.getDateIn().toString());
            }
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to export SPJ to Excel", e);
        }
    }

    public byte[] exportSpjToExcel(List<Spj> spjList) {
        return exportSpjToExcel(spjList, "SPJ Report", "", "src/main/resources/static/GIB.png");
    }

    private int addExcelHeader(Workbook workbook, Sheet sheet, String logoPath, String companyInfo, String reportTitle, String period, int colCount) {
        int rowIdx = 0;
        try {
            if (logoPath != null && !logoPath.isEmpty()) {
                InputStream is = getClass().getResourceAsStream("/static/GIB.png");
                if (is != null) {
                    byte[] bytes = IOUtils.toByteArray(is);
                    int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
                    is.close();
                    CreationHelper helper = workbook.getCreationHelper();
                    Drawing<?> drawing = sheet.createDrawingPatriarch();
                    ClientAnchor anchor = helper.createClientAnchor();
                    anchor.setCol1(0);
                    anchor.setRow1(rowIdx);
                    Picture pict = drawing.createPicture(anchor, pictureIdx);
                    pict.resize(1, 3); // 1 kolom, 3 baris
                }
            }
        } catch (Exception e) { /* ignore logo if error */ }
        // Company info
        Row companyRow = sheet.createRow(rowIdx);
        Cell companyCell = companyRow.createCell(1);
        companyCell.setCellValue(companyInfo);
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short)10);
        style.setFont(font);
        style.setWrapText(true);
        companyCell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(rowIdx, rowIdx+2, 1, colCount-1));
        rowIdx += 3;
        // Judul
        Row titleRow = sheet.createRow(rowIdx++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(reportTitle);
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short)18);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), 0, colCount-1));
        // Periode
        if (period != null && !period.isEmpty()) {
            Row periodRow = sheet.createRow(rowIdx++);
            Cell periodCell = periodRow.createCell(0);
            periodCell.setCellValue(period);
            CellStyle periodStyle = workbook.createCellStyle();
            Font periodFont = workbook.createFont();
            periodFont.setFontHeightInPoints((short)12);
            periodStyle.setFont(periodFont);
            periodStyle.setAlignment(HorizontalAlignment.CENTER);
            periodCell.setCellStyle(periodStyle);
            sheet.addMergedRegion(new CellRangeAddress(periodRow.getRowNum(), periodRow.getRowNum(), 0, colCount-1));
        }
        rowIdx++;
        return rowIdx;
    }

    public byte[] exportOrdersToExcel(List<Order> orders, String reportTitle, String period, String logoPath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Orders");
            String companyInfo = "PT. GLORIOUS INTERBUANA\nKawasan Berikat Nusatara Marunda\nJl. Medan No. 4 Kav. C 18/2\nJakarta Utara - Indonesia";
            String[] columns = {"Order ID", "Customer ID", "Customer Name", "Order Date", "Move Type", "Down Payment", "20' Chassis Qty", "40' Chassis Qty"};
            int startRow = addExcelHeader(workbook, sheet, logoPath, companyInfo, reportTitle, period, columns.length);
            // Create header row
            Row headerRow = sheet.createRow(startRow);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }
            // Create data rows
            int rowNum = startRow + 1;
            for (Order order : orders) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(order.getOrderId());
                row.createCell(1).setCellValue(order.getCustomer().getId());
                row.createCell(2).setCellValue(order.getCustomer().getName());
                row.createCell(3).setCellValue(order.getOrderDate().toString());
                row.createCell(4).setCellValue(order.getMoveType());
                row.createCell(5).setCellValue(order.getDownPayment());
                row.createCell(6).setCellValue(order.getQtyChassis20());
                row.createCell(7).setCellValue(order.getQtyChassis40());
            }
            // Auto-size columns
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to export orders to Excel", e);
        }
    }

    public byte[] exportReportTruckToExcel(ReportTruck reportTruck, String reportTitle, String period, String logoPath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Vehicle Maintenance Report");
            String companyInfo = "PT. GLORIOUS INTERBUANA\nKawasan Berikat Nusatara Marunda\nJl. Medan No. 4 Kav. C 18/2\nJakarta Utara - Indonesia";
            String[] columns = {"Report Truck ID", "Date", "Start Repair", "Finish Repair", "Vehicle ID", "Vehicle Plate No.", "Vehicle Type", "Vehicle Brand", "Description", "Assets Used (Quantity)"};
            int startRow = addExcelHeader(workbook, sheet, logoPath, companyInfo, reportTitle, period, columns.length);
            Row headerRow = sheet.createRow(startRow);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }
            int rowNum = startRow + 1;
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(reportTruck.getReportTruckId());
            row.createCell(1).setCellValue(reportTruck.getDate().toString());
            row.createCell(2).setCellValue(reportTruck.getStartRepair().toString());
            row.createCell(3).setCellValue(reportTruck.getFinishRepair().toString());
            row.createCell(4).setCellValue(reportTruck.getVehicleId());
            row.createCell(5).setCellValue(reportTruck.getVehiclePlateNo());
            row.createCell(6).setCellValue(reportTruck.getVehicleType());
            row.createCell(7).setCellValue(reportTruck.getVehicleBrand());
            row.createCell(8).setCellValue(reportTruck.getDescription());
            StringBuilder assets = new StringBuilder();
            for (ReportTruckAsset asset : reportTruck.getAssets()) {
                String assetName = "-";
                if (asset.getAssetId() != null) {
                    be_sitruck.backend_sitruck.model.Asset assetObj = assetDb.findByAssetId(asset.getAssetId());
                    if (assetObj != null) {
                        assetName = assetObj.getJenisAsset();
                    }
                }
                assets.append(assetName).append(" (").append(asset.getQuantity()).append(")").append(", ");
            }
            if (assets.length() > 0) {
                assets.setLength(assets.length() - 2);
            }
            row.createCell(9).setCellValue(assets.toString());

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to export Vehicle Maintenance Report to Excel", e);
        }
    }

    public byte[] exportReportTruckToExcel(ReportTruck reportTruck) {
        return exportReportTruckToExcel(reportTruck, "Vehicle Maintenance Report", "", "src/main/resources/static/GIB.png");
    }

    public byte[] exportCommissionsToExcel(List<Komisi> komisiList, String reportTitle, String period, String logoPath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Commissions");
            String companyInfo = "PT. GLORIOUS INTERBUANA\nKawasan Berikat Nusatara Marunda\nJl. Medan No. 4 Kav. C 18/2\nJakarta Utara - Indonesia";
            String[] columns = {"Commission ID", "Truck ID", "Vehicle Name", "Location", "Truck Commission Fee", "Commission Fee"};
            int startRow = addExcelHeader(workbook, sheet, logoPath, companyInfo, reportTitle, period, columns.length);
            Row headerRow = sheet.createRow(startRow);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }
            int rowNum = startRow + 1;
            for (Komisi komisi : komisiList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(komisi.getKomisiId());
                row.createCell(1).setCellValue(komisi.getTruck().getVehicleId());
                row.createCell(2).setCellValue(komisi.getTruck().getVehicleBrand());
                row.createCell(3).setCellValue(komisi.getLocation());
                row.createCell(4).setCellValue(komisi.getTruckCommission());
                row.createCell(5).setCellValue(komisi.getCommissionFee());
            }
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to export drivers to Excel", e);
        }
    }

    public byte[] exportCommissionsToExcel(List<Komisi> komisiList) {
        return exportCommissionsToExcel(komisiList, "Commissions Report", "", "src/main/resources/static/GIB.png");
    }
}