package com.jovan.erp_v1.service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.dto.GoodsDispatchDTO;
import com.jovan.erp_v1.dto.GoodsItemDTO;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

@Service
/**
 * Service for generating a PDF document
 */
public class PdfGeneratorService {

	public byte[] generateDispatchConfirmation(GoodsDispatchDTO dto) throws Exception {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        document.add(new Paragraph("Potvrda o otpremi robe"));
        document.add(new Paragraph("Datum: " + LocalDateTime.now()));
        document.add(new Paragraph("Radnik: " + dto.getEmployeeName()));
        document.add(new Paragraph("Proizvodi:"));

        for (GoodsItemDTO item : dto.getItems()) {
            document.add(new Paragraph("- " + item.getName() + ": " + item.getQuantity() + " " + item.getUnit()));
        }

        document.close();
        return out.toByteArray();
    }
	
}
