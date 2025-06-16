package be_sitruck.backend_sitruck.util;

import be_sitruck.backend_sitruck.model.*;
import be_sitruck.backend_sitruck.repository.AssetDb;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPCell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import java.text.NumberFormat;
import java.util.Locale;


import java.io.ByteArrayOutputStream;
import java.util.List;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

@Component
public class PDFExporter {
    Locale indo = new Locale("id", "ID");
    NumberFormat currencyFmt = NumberFormat.getCurrencyInstance(indo);

    @Autowired
    private AssetDb assetDb;

    private void addReportHeader(Document document, String reportTitle, String startDate, String endDate) throws DocumentException {
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new int[]{1, 10});
        try {
            InputStream logoStream = getClass().getResourceAsStream("/static/GIB.png");
            if (logoStream != null) {
                Image logo = Image.getInstance(IOUtils.toByteArray(logoStream));
                logo.scaleToFit(110, 110);
                PdfPCell logoCell = new PdfPCell(logo, false);
                logoCell.setBorder(Rectangle.NO_BORDER);
                logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                logoCell.setPadding(0f);
                headerTable.addCell(logoCell);
                logoStream.close();
            } else {
                PdfPCell logoCell = new PdfPCell(new Phrase("[LOGO]"));
                logoCell.setBorder(Rectangle.NO_BORDER);
                logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                logoCell.setPadding(0f);
                headerTable.addCell(logoCell);
            }
        } catch (Exception e) {
            PdfPCell logoCell = new PdfPCell(new Phrase("[LOGO]"));
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            logoCell.setPadding(0f);
            headerTable.addCell(logoCell);
        }
        String companyInfo = "PT. GLORIOUS INTERBUANA\n" +
                "Kawasan Berikat Nusatara Marunda\n" +
                "Jl. Medan No. 4 Kav. C 18/2\n" +
                "Jakarta Utara - Indonesia";
        PdfPCell infoCell = new PdfPCell();
        infoCell.setBorder(Rectangle.NO_BORDER);
        infoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        infoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        infoCell.setPadding(0f);
        infoCell.setPaddingLeft(50f);
        infoCell.addElement(new Paragraph(companyInfo, new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)));
        headerTable.addCell(infoCell);
        document.add(headerTable);
        document.add(new Paragraph(" "));
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
        Paragraph title = new Paragraph(reportTitle, titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        if (startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty()) {
            Paragraph periodP = new Paragraph("Order period: " + startDate + " until " + endDate, new Font(Font.FontFamily.HELVETICA, 12));
            periodP.setAlignment(Element.ALIGN_CENTER);
            document.add(periodP);
        }
        document.add(new Paragraph(" "));
    }

    public byte[] exportCustomersToPDF(List<Customer> customers) throws DocumentException {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();
            addReportHeader(document, "Customer Report", "", "");
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);

            table.addCell("Customer ID");
            table.addCell("Customer Name");
            table.addCell("Site ID");
            table.addCell("Address");
            table.addCell("Destination");

            for (Customer customer : customers) {
                table.addCell(customer.getId());
                table.addCell(customer.getName());
                table.addCell(customer.getSiteId());
                table.addCell(customer.getAddress());
                table.addCell(customer.getCityDestination());
            }

            document.add(table);
        } finally {
            document.close();
        }

        return out.toByteArray();
    }

    public byte[] exportVehiclesToPDF(List<Truck> vehicles) throws DocumentException {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();
            addReportHeader(document, "Vehicle Report", "", "");
            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);

            table.addCell("Vehicle ID");
            table.addCell("Vehicle Brand");
            table.addCell("Year");
            table.addCell("Type");
            table.addCell("Plate No.");
            table.addCell("KIR No.");
            table.addCell("STNK Expiration");
            table.addCell("KIR Expiration");

            for (Truck vehicle : vehicles) {
                table.addCell(vehicle.getVehicleId());
                table.addCell(vehicle.getVehicleBrand());
                table.addCell(vehicle.getVehicleYear());
                table.addCell(vehicle.getVehicleType());
                table.addCell(vehicle.getVehiclePlateNo());
                table.addCell(vehicle.getVehicleKIRNo());
                table.addCell(vehicle.getVehicleSTNKDate().toString());
                table.addCell(vehicle.getVehicleKIRDate().toString());
            }

            document.add(table);
        } finally {
            document.close();
        }

        return out.toByteArray();
    }

    public byte[] exportChassisToPDF(List<Chassis> chassisList) throws DocumentException {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();
            addReportHeader(document, "Chassis Report", "", "");
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);

            table.addCell("Chassis ID");
            table.addCell("Year");
            table.addCell("Size");
            table.addCell("Type");
            table.addCell("Chassis No.");
            table.addCell("KIR No.");
            table.addCell("KIR Expiration");

            for (Chassis chassis : chassisList) {
                table.addCell(chassis.getChassisId());
                table.addCell(chassis.getChassisYear());
                table.addCell(String.valueOf(chassis.getChassisSize()));
                table.addCell(chassis.getChassisType());
                table.addCell(chassis.getChassisNumber());
                table.addCell(chassis.getChassisKIRNo());
                table.addCell(chassis.getChassisKIRDate().toString());
            }

            document.add(table);
        } finally {
            document.close();
        }

        return out.toByteArray();
    }

    public byte[] exportDriversToPDF(List<SopirModel> drivers) throws DocumentException {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();
            addReportHeader(document, "Driver Report", "", "");
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);

            table.addCell("Driver ID");
            table.addCell("Driver Name");
            table.addCell("KTP No.");
            table.addCell("SIM No.");
            table.addCell("Join Date");

            for (SopirModel driver : drivers) {
                table.addCell(driver.getDriverId());
                table.addCell(driver.getDriverName());
                table.addCell(driver.getDriver_KTP_No());
                table.addCell(driver.getDriver_SIM_No());
                table.addCell(driver.getDriverJoinDate().toString());
            }

            document.add(table);
        } finally {
            document.close();
        }

        return out.toByteArray();
    }

    public byte[] exportOrdersToPDF(List<Order> orders, String fromDate, String endDate) throws DocumentException {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();
            addReportHeader(document, "Order Report", fromDate, endDate);
            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);

            table.addCell("Order ID");
            table.addCell("Customer ID");
            table.addCell("Customer Name");
            table.addCell("Order Date");
            table.addCell("Move Type");
            table.addCell("Down Payment");
            table.addCell("20' Chassis Qty");
            table.addCell("40' Chassis Qty");

            for (Order order : orders) {
                table.addCell(order.getOrderId());
                table.addCell(order.getCustomer().getId());
                table.addCell(order.getCustomer().getName());
                table.addCell(order.getOrderDate().toString());
                table.addCell(order.getMoveType());
                String dwnPayment = currencyFmt.format(order.getDownPayment());
                table.addCell(dwnPayment);
                table.addCell(String.valueOf(order.getQtyChassis20()));
                table.addCell(String.valueOf(order.getQtyChassis40()));
            }

            document.add(table);
        } finally {
            document.close();
        }

        return out.toByteArray();
    }

    public byte[] exportSpjToPDF(List<Spj> spjList, String fromDate, String endDate) throws DocumentException {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();
            addReportHeader(document, "SPJ Report", fromDate, endDate);
            PdfPTable table = new PdfPTable(14);
            table.setWidthPercentage(100);

            table.addCell("SPJ ID");
            table.addCell("Order ID");
            table.addCell("Customer ID");
            table.addCell("Customer Name");
            table.addCell("Driver Name");
            table.addCell("Vehicle ID");
            table.addCell("Chassis ID");
            table.addCell("Chassis Size");
            table.addCell("Container Qty");
            table.addCell("Container Type");
            table.addCell("Commission");
            table.addCell("Other Commission");
            table.addCell("Date Out");
            table.addCell("Date In");

            for (Spj spj : spjList) {
                table.addCell(spj.getId());
                table.addCell(spj.getOrder().getOrderId());
                table.addCell(spj.getCustomer().getId());
                table.addCell(spj.getCustomer().getName());
                table.addCell(spj.getDriver().getDriverName());
                table.addCell(spj.getVehicle().getVehicleId());
                table.addCell(spj.getChassis().getChassisId());
                table.addCell(String.valueOf(spj.getChassisSize()));
                table.addCell(String.valueOf(spj.getContainerQty()));
                table.addCell(spj.getContainerType());
                String comm = currencyFmt.format(spj.getCommission());
                table.addCell(comm);
                String othersComm = currencyFmt.format(spj.getOthersCommission());
                table.addCell(othersComm);
                table.addCell(spj.getDateOut().toString());
                table.addCell(spj.getDateIn().toString());
            }

            document.add(table);
        } finally {
            document.close();
        }

        return out.toByteArray();
    }

    public byte[] exportReportTruckToPDF(ReportTruck report) throws DocumentException {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();
            addReportHeader(document, "Vehicle Maintenance Report", "", "");
            PdfPTable table = new PdfPTable(10);
            table.setWidthPercentage(100);

            table.addCell("Report ID");
            table.addCell("Date");
            table.addCell("Start Repair");
            table.addCell("Finish Repair");
            table.addCell("Vehicle ID");
            table.addCell("Vehicle Brand");
            table.addCell("Vehicle Type");
            table.addCell("Vehicle Plate No.");
            table.addCell("Description");
            table.addCell("Assets Used (Quantity)");

            table.addCell(report.getReportTruckId());
            table.addCell(report.getDate().toString());
            table.addCell(report.getStartRepair().toString());
            table.addCell(report.getFinishRepair().toString());
            table.addCell(report.getVehicleId());
            table.addCell(report.getVehicleBrand());
            table.addCell(report.getVehicleType());
            table.addCell(report.getVehiclePlateNo());
            table.addCell(report.getDescription());

            StringBuilder assets = new StringBuilder();
            for (ReportTruckAsset assetUsed : report.getAssets()) {
                String assetName = "-";
                if (assetUsed.getAssetId() != null) {
                    Asset asset = assetDb.findByAssetId(assetUsed.getAssetId());
                    if (asset != null) {
                        assetName = asset.getJenisAsset();
                    }
                }
                assets.append(assetName).append(" (").append(assetUsed.getQuantity()).append(")").append(", ");
            }
            if (assets.length() > 0) {
                assets.setLength(assets.length() - 2);
            }
            table.addCell(assets.toString());

            document.add(table);
        } finally {
            document.close();
        }

        return out.toByteArray();
    }

    public byte[] exportKomisiToPDF(List<Komisi> komisiList) throws DocumentException {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();
            addReportHeader(document, "Commissions Report ", "", "");
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);

            table.addCell("Commission ID");
            table.addCell("Truck ID");
            table.addCell("Vehicle Name");
            table.addCell("Location");
            table.addCell("Truck Commission Fee");
            table.addCell("Commission Fee");
            

            for (Komisi komisi : komisiList) {
                table.addCell(komisi.getKomisiId());
                table.addCell(komisi.getTruck().getVehicleId());
                table.addCell(komisi.getTruck().getVehicleBrand());
                table.addCell(komisi.getLocation());
                String truckFee = currencyFmt.format(komisi.getTruckCommission());
                table.addCell(truckFee);
                String commissionFee = currencyFmt.format(komisi.getCommissionFee());
                table.addCell(commissionFee);
            }

            document.add(table);
        } finally {
            document.close();
        }

        return out.toByteArray();
    }
}