package com.jovan.erp_v1.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.jovan.erp_v1.enumeration.TripStatus;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.model.Driver;
import com.jovan.erp_v1.repository.DriverRepository;
import com.jovan.erp_v1.repository.TripRepository;
import com.jovan.erp_v1.response.DriverReportResponse;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import lombok.RequiredArgsConstructor;

/**
 *Driver service class for generating a report
 */
@Service
@RequiredArgsConstructor
public class DriverReportService {

	private final DriverRepository driverRepository;
	private final TripRepository tripRepository;
	
	public DriverReportResponse generateDriverReport(Long driverId) {
		Driver driver = driverRepository.findById(driverId).orElseThrow(() -> new ValidationException("Driver not found with id "+driverId));
		Long totalTrips = tripRepository.countByDriver_Id(driverId);
        Long completedTrips = tripRepository.countByDriver_IdAndStatus(driver.getId(), TripStatus.COMPLETED);
        Long cancelledTrips = tripRepository.countByDriver_IdAndStatus(driver.getId(), TripStatus.CANCELLED);
        Long activeTrips = tripRepository.countByDriver_IdAndStatus(driver.getId(), TripStatus.IN_PROGRESS);
        BigDecimal avgDuration = tripRepository.calculateAverageDuration(driverId);
        BigDecimal totalRevenue = tripRepository.calculateTotalRevenue(driverId);
        return new DriverReportResponse(
                driver.getId(),
                driver.getFirstName() + " " + driver.getLastName(),
                driver.getPhone(),
                totalTrips,
                completedTrips,
                cancelledTrips,
                activeTrips,
                avgDuration,
                totalRevenue
        );
	}
	
	//za generisanje PDF izvestaja
	public ByteArrayInputStream exportDriverReportToPdf(DriverReportResponse report) {
	    Document document = new Document();
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    try {
	        PdfWriter.getInstance(document, out);
	        document.open();
	        document.add(new Paragraph("Driver Report", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
	        document.add(new Paragraph("Generated: " + LocalDateTime.now()));
	        document.add(new Paragraph("\n"));
	        PdfPTable table = new PdfPTable(2);
	        table.setWidthPercentage(100);
	        table.addCell("Driver ID");
	        table.addCell(report.driverId().toString());
	        table.addCell("Full Name");
	        table.addCell(report.fullName());
	        table.addCell("Phone");
	        table.addCell(report.phone());
	        table.addCell("Total Trips");
	        table.addCell(report.totalTrips().toString());
	        table.addCell("Completed Trips");
	        table.addCell(report.completedTrips().toString());
	        table.addCell("Cancelled Trips");
	        table.addCell(report.cancelledTrips().toString());
	        table.addCell("Active Trips");
	        table.addCell(report.activeTrips().toString());
	        table.addCell("Average Duration (hrs)");
	        table.addCell(String.format("%.2f", report.averageDurationInHours()));
	        table.addCell("Total Revenue");
	        table.addCell(String.format("%.2f", report.totalRevenue()));
	        document.add(table);
	        document.close();
	    } catch (DocumentException e) {
	        throw new RuntimeException("Error generating PDF", e);
	    }

	    return new ByteArrayInputStream(out.toByteArray());
	}
	
	//za generisanje Excel izvestaja
	public ByteArrayInputStream exportDriverReportToExcel(DriverReportResponse report) throws IOException {
	    try (Workbook workbook = new XSSFWorkbook()) {
	        Sheet sheet = workbook.createSheet("Driver Report");
	        Row header = sheet.createRow(0);
	        header.createCell(0).setCellValue("Driver ID");
	        header.createCell(1).setCellValue("Full Name");
	        header.createCell(2).setCellValue("Phone");
	        header.createCell(3).setCellValue("Total Trips");
	        header.createCell(4).setCellValue("Completed Trips");
	        header.createCell(5).setCellValue("Cancelled Trips");
	        header.createCell(6).setCellValue("Active Trips");
	        header.createCell(7).setCellValue("Avg Duration (hrs)");
	        header.createCell(8).setCellValue("Total Revenue");
	        Row row = sheet.createRow(1);
	        row.createCell(0).setCellValue(report.driverId());
	        row.createCell(1).setCellValue(report.fullName());
	        row.createCell(2).setCellValue(report.phone());
	        row.createCell(3).setCellValue(report.totalTrips());
	        row.createCell(4).setCellValue(report.completedTrips());
	        row.createCell(5).setCellValue(report.cancelledTrips());
	        row.createCell(6).setCellValue(report.activeTrips());
	        row.createCell(7).setCellValue(report.averageDurationInHours().doubleValue());
	        row.createCell(8).setCellValue(report.totalRevenue().doubleValue());
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        workbook.write(out);
	        return new ByteArrayInputStream(out.toByteArray());
	    }
	}
}
