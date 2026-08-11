package com.HRMS.QuickDines.AdvanceServices;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    public byte[] generatePdf(
            PdfDocumentBuilder builder) {

        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();

        Document document =
                new Document(
                        PageSize.A4,
                        40,
                        40,
                        40,
                        40);

        try {

            PdfWriter.getInstance(
                    document,
                    outputStream);

            document.open();

            builder.build(document);

            document.close();

            return outputStream.toByteArray();

        } catch (Exception e) {

            if (document.isOpen()) {
                document.close();
            }

            throw new RuntimeException(
                    "PDF generation failed",
                    e);
        }
    }
}
