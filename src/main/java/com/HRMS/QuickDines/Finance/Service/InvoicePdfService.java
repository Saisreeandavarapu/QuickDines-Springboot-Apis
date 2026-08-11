package com.HRMS.QuickDines.Finance.Service;
import com.HRMS.QuickDines.AdvanceServices.PdfService;
import com.HRMS.QuickDines.Finance.model.InvoiceManagement;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class InvoicePdfService {

    private final PdfService pdfService;

    public byte[] generateInvoicePdf(
            InvoiceManagement invoice) {

        return pdfService.generatePdf(
                document -> {

                    // ==============================
                    // HEADER
                    // ==============================

                    Font titleFont =
                            FontFactory.getFont(
                                    FontFactory.HELVETICA_BOLD,
                                    20);

                    Paragraph title =
                            new Paragraph(
                                    "QUICKDINE",
                                    titleFont);

                    title.setAlignment(
                            Element.ALIGN_CENTER);

                    document.add(title);

                    Paragraph invoiceTitle =
                            new Paragraph(
                                    "INVOICE / RECEIPT",
                                    FontFactory.getFont(
                                            FontFactory
                                                    .HELVETICA_BOLD,
                                            14));

                    invoiceTitle.setAlignment(
                            Element.ALIGN_CENTER);

                    document.add(invoiceTitle);

                    document.add(
                            new Paragraph(" "));

                    // ==============================
                    // INVOICE DETAILS
                    // ==============================

                    PdfPTable table =
                            new PdfPTable(2);

                    table.setWidthPercentage(100);

                    addRow(
                            table,
                            "Invoice Number",
                            invoice.getInvoiceNumber());

                    addRow(
                            table,
                            "Invoice Type",
                            invoice.getInvoiceType());

                    addRow(
                            table,
                            "Invoice Date",
                            String.valueOf(
                                    invoice.getInvoiceDate()));

                    addRow(
                            table,
                            "Due Date",
                            String.valueOf(
                                    invoice.getDueDate()));

                    addRow(
                            table,
                            "Payment Status",
                            invoice.getPaymentStatus());

                    document.add(table);

                    document.add(
                            new Paragraph(" "));

                    // ==============================
                    // AMOUNT DETAILS
                    // ==============================

                    PdfPTable amountTable =
                            new PdfPTable(2);

                    amountTable.setWidthPercentage(100);

                    addRow(
                            amountTable,
                            "Subtotal",
                            formatAmount(
                                    invoice.getSubtotal()));

                    addRow(
                            amountTable,
                            "Tax",
                            formatAmount(
                                    invoice.getTaxAmount()));

                    addRow(
                            amountTable,
                            "Discount",
                            formatAmount(
                                    invoice.getDiscount()));

                    addRow(
                            amountTable,
                            "TOTAL",
                            formatAmount(
                                    invoice.getTotalAmount()));

                    document.add(amountTable);

                    document.add(
                            new Paragraph(" "));

                    // ==============================
                    // FOOTER
                    // ==============================

                    Paragraph footer =
                            new Paragraph(
                                    "Thank you for choosing QuickDine.");

                    footer.setAlignment(
                            Element.ALIGN_CENTER);

                    document.add(footer);
                });
    }


    private void addRow(
            PdfPTable table,
            String label,
            String value) {

        table.addCell(label);
        table.addCell(
                value == null ? "" : value);
    }


    private String formatAmount(
            BigDecimal amount) {

        if (amount == null) {
            return "₹ 0.00";
        }

        return "₹ "
                + amount
                .setScale(2)
                .toString();
    }
}