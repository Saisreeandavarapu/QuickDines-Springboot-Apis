package com.HRMS.QuickDines.Task.Controller;

import com.HRMS.QuickDines.Task.Service.TimesheetIntegrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/timesheets")
@RequiredArgsConstructor
public class TimesheetIntegrationController {

    private final TimesheetIntegrationService service;


    // =====================================================
    // EXCEL IMPORT
    // =====================================================

    @PostMapping(
            value = "/import/excel",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> importExcel(
            @RequestParam("file") MultipartFile file) {

        return ResponseEntity.ok(
                service.importExcel(file)
        );
    }


    // =====================================================
    // GOOGLE SHEETS IMPORT
    // =====================================================

    @PostMapping("/import/google-sheets")
    public ResponseEntity<?> importGoogleSheets(
            @RequestParam String spreadsheetId,
            @RequestParam String sheetName) {

        return ResponseEntity.ok(
                service.importGoogleSheets(
                        spreadsheetId,
                        sheetName
                )
        );
    }


    // =====================================================
    // EXCEL EXPORT - ALL
    // =====================================================

    @GetMapping("/export/excel")
    public ResponseEntity<ByteArrayResource> exportExcel()
            throws Exception {

        byte[] data = service.exportExcel();

        ByteArrayResource resource =
                new ByteArrayResource(data);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=timesheets.xlsx"
                )
                .contentType(
                        MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        )
                )
                .contentLength(data.length)
                .body(resource);
    }


    // =====================================================
    // EXCEL EXPORT - EMPLOYEE
    // =====================================================

    @GetMapping("/export/excel/employee/{employeeId}")
    public ResponseEntity<ByteArrayResource> exportEmployeeExcel(
            @PathVariable String employeeId)
            throws Exception {

        byte[] data =
                service.exportEmployeeExcel(employeeId);

        ByteArrayResource resource =
                new ByteArrayResource(data);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=timesheet-"
                                + employeeId + ".xlsx"
                )
                .contentType(
                        MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        )
                )
                .body(resource);
    }


    // =====================================================
    // EXCEL EXPORT - DATE
    // =====================================================

    @GetMapping("/export/excel/date/{date}")
    public ResponseEntity<ByteArrayResource> exportDateExcel(
            @PathVariable LocalDate date)
            throws Exception {

        byte[] data =
                service.exportDateExcel(date);

        ByteArrayResource resource =
                new ByteArrayResource(data);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=timesheet-"
                                + date + ".xlsx"
                )
                .contentType(
                        MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        )
                )
                .body(resource);
    }


    // =====================================================
    // GOOGLE SHEETS EXPORT - ALL
    // =====================================================

    @PostMapping("/export/google-sheets")
    public ResponseEntity<?> exportGoogleSheets(
            @RequestParam String spreadsheetId,
            @RequestParam String sheetName) {

        return ResponseEntity.ok(
                service.exportGoogleSheets(
                        spreadsheetId,
                        sheetName
                )
        );
    }


    // =====================================================
    // GOOGLE SHEETS EXPORT - EMPLOYEE
    // =====================================================

    @PostMapping(
            "/export/google-sheets/employee/{employeeId}"
    )
    public ResponseEntity<?> exportEmployeeGoogleSheets(
            @PathVariable String employeeId,
            @RequestParam String spreadsheetId,
            @RequestParam String sheetName) {

        return ResponseEntity.ok(
                service.exportEmployeeGoogleSheets(
                        employeeId,
                        spreadsheetId,
                        sheetName
                )
        );
    }


    // =====================================================
    // GOOGLE SHEETS EXPORT - DATE
    // =====================================================

    @PostMapping(
            "/export/google-sheets/date/{date}"
    )
    public ResponseEntity<?> exportDateGoogleSheets(
            @PathVariable LocalDate date,
            @RequestParam String spreadsheetId,
            @RequestParam String sheetName) {

        return ResponseEntity.ok(
                service.exportDateGoogleSheets(
                        date,
                        spreadsheetId,
                        sheetName
                )
        );
    }

    // =====================================================
    // IMPORT EXCEL FOR EMPLOYEE
    // =====================================================

    @PostMapping(
            value = "/import/excel/{employeeId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> importExcel(
            @PathVariable String employeeId,
            @RequestParam("file") MultipartFile file) {

        return ResponseEntity.ok(
                service.importExcel(employeeId, file)
        );
    }


    // =====================================================
    // IMPORT GOOGLE SHEET FOR EMPLOYEE
    // =====================================================

    @PostMapping("/import/google-sheets/{employeeId}")
    public ResponseEntity<?> importGoogleSheet(
            @PathVariable String employeeId,
            @RequestParam String spreadsheetId,
            @RequestParam String sheetName) {

        return ResponseEntity.ok(
                service.importGoogleSheet(
                        employeeId,
                        spreadsheetId,
                        sheetName
                )
        );
    }
}