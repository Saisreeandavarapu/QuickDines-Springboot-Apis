package com.HRMS.QuickDines.AdvanceServices;


import com.lowagie.text.Document;

@FunctionalInterface
public interface PdfDocumentBuilder {

    void build(Document document) throws Exception;
}
