package com.HRMS.QuickDines.Task.Service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoogleSheetsService {

    private final Sheets googleSheets;


    // =====================================================
    // READ GOOGLE SHEET
    // =====================================================

    public List<List<Object>> readSheet(
            String spreadsheetId,
            String sheetName) {

        try {

            String range = sheetName + "!A:F";

            ValueRange response =
                    googleSheets
                            .spreadsheets()
                            .values()
                            .get(
                                    spreadsheetId,
                                    range
                            )
                            .execute();

            List<List<Object>> values =
                    response.getValues();

            if (values == null) {
                return Collections.emptyList();
            }

            return values;

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to read Google Sheet: "
                            + e.getMessage(),
                    e
            );
        }
    }
}