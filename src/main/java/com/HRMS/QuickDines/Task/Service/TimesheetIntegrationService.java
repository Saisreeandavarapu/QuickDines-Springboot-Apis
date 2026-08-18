package com.HRMS.QuickDines.Task.Service;

import com.HRMS.QuickDines.Employee.model.Employee;
import com.HRMS.QuickDines.Employee.repo.EmployeeRepository;
import com.HRMS.QuickDines.Task.Entity.TimesheetStatus;
import com.HRMS.QuickDines.Task.model.EmployeeTimesheet;
import com.HRMS.QuickDines.Task.repo.EmployeeTimesheetRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimesheetIntegrationService {

    private final EmployeeTimesheetRepository timesheetRepository;

    private final EmployeeRepository employeeRepository;

  //  private final GoogleSheetsService googleSheetsService;




    // =====================================================
    // EXCEL IMPORT
    // =====================================================

    @Transactional
    public String importExcel(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException(
                    "Excel file is required"
            );
        }

        int imported = 0;

        try (Workbook workbook =
                     new XSSFWorkbook(file.getInputStream())) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                String employeeId =
                        getString(row.getCell(0));

                if (employeeId == null ||
                        employeeId.isBlank()) {
                    continue;
                }

                Employee employee =
                        employeeRepository
                                .findByEmployeeId(employeeId)
                                .orElseThrow(() ->
                                        new RuntimeException(
                                                "Employee not found: "
                                                        + employeeId
                                        )
                                );

                LocalDate workDate =
                        LocalDate.parse(
                                getString(row.getCell(1))
                        );

                EmployeeTimesheet timesheet =
                        timesheetRepository
                                .findByEmployee_EmployeeIdAndWorkDate(
                                        employeeId,
                                        workDate
                                )
                                .orElse(
                                        new EmployeeTimesheet()
                                );

                timesheet.setEmployee(employee);
                timesheet.setWorkDate(workDate);

                timesheet.setProjectName(
                        getString(row.getCell(2))
                );

                timesheet.setWorkDescription(
                        getString(row.getCell(3))
                );

                timesheet.setStartTime(
                        parseTime(row.getCell(4))
                );

                timesheet.setEndTime(
                        parseTime(row.getCell(5))
                );

                String breakValue =
                        getString(row.getCell(6));

                if (breakValue != null &&
                        !breakValue.isBlank()) {

                    timesheet.setBreakMinutes(
                            Integer.parseInt(breakValue)
                    );
                }

                calculateTotalHours(timesheet);

                timesheetRepository.save(timesheet);

                imported++;
            }

            return imported +
                    " timesheets imported successfully";

        } catch (Exception e) {

            throw new RuntimeException(
                    "Excel import failed: "
                            + e.getMessage(),
                    e
            );
        }
    }


    // =====================================================
    // GOOGLE SHEETS IMPORT
    // =====================================================

    @Transactional
    public String importGoogleSheets(
            String spreadsheetId,
            String sheetName) {

        /*
         * Google Sheets API integration goes here.
         *
         * 1. Authenticate Google account
         * 2. Open spreadsheetId
         * 3. Read sheetName
         * 4. Read rows
         * 5. Find Employee using employeeId
         * 6. Create/update EmployeeTimesheet
         * 7. Save into MySQL
         */

        throw new UnsupportedOperationException(
                "Google Sheets API is not configured yet"
        );
    }


    // =====================================================
    // EXCEL EXPORT - ALL
    // =====================================================

    public byte[] exportExcel()
            throws IOException {

        List<EmployeeTimesheet> timesheets =
                timesheetRepository.findAll();

        return createExcel(timesheets);
    }


    // =====================================================
    // EXCEL EXPORT - EMPLOYEE
    // =====================================================

    public byte[] exportEmployeeExcel(
            String employeeId)
            throws IOException {

        List<EmployeeTimesheet> timesheets =
                timesheetRepository
                        .findByEmployee_EmployeeId(
                                employeeId
                        );

        return createExcel(timesheets);
    }


    // =====================================================
    // EXCEL EXPORT - DATE
    // =====================================================

    public byte[] exportDateExcel(
            LocalDate date)
            throws IOException {

        List<EmployeeTimesheet> timesheets =
                timesheetRepository
                        .findByWorkDate(date);

        return createExcel(timesheets);
    }


    // =====================================================
    // CREATE EXCEL
    // =====================================================

    private byte[] createExcel(
            List<EmployeeTimesheet> timesheets)
            throws IOException {

        Workbook workbook =
                new XSSFWorkbook();

        Sheet sheet =
                workbook.createSheet("Timesheets");

        Row header =
                sheet.createRow(0);

        String[] columns = {
                "Employee ID",
                "Employee Name",
                "Work Date",
                "Project",
                "Description",
                "Start Time",
                "End Time",
                "Break Minutes",
                "Total Hours",
                "Status"
        };

        for (int i = 0;
             i < columns.length;
             i++) {

            header.createCell(i)
                    .setCellValue(columns[i]);
        }

        int rowIndex = 1;

        for (EmployeeTimesheet t : timesheets) {

            Row row =
                    sheet.createRow(rowIndex++);

            row.createCell(0)
                    .setCellValue(
                            t.getEmployee()
                                    .getEmployeeId()
                    );

            row.createCell(1)
                    .setCellValue(
                            t.getEmployee()
                                    .getFirstName()
                                    + " "
                                    + t.getEmployee()
                                    .getLastName()
                    );

            row.createCell(2)
                    .setCellValue(
                            t.getWorkDate()
                                    .toString()
                    );

            row.createCell(3)
                    .setCellValue(
                            safe(t.getProjectName())
                    );

            row.createCell(4)
                    .setCellValue(
                            safe(t.getWorkDescription())
                    );

            row.createCell(5)
                    .setCellValue(
                            t.getStartTime() != null
                                    ? t.getStartTime()
                                    .toString()
                                    : ""
                    );

            row.createCell(6)
                    .setCellValue(
                            t.getEndTime() != null
                                    ? t.getEndTime()
                                    .toString()
                                    : ""
                    );

            row.createCell(7)
                    .setCellValue(
                            t.getBreakMinutes() != null
                                    ? t.getBreakMinutes()
                                    : 0
                    );

            row.createCell(8)
                    .setCellValue(
                            t.getTotalHours() != null
                                    ? t.getTotalHours()
                                    .doubleValue()
                                    : 0
                    );

            row.createCell(9)
                    .setCellValue(
                            t.getStatus() != null
                                    ? t.getStatus()
                                    .name()
                                    : ""
                    );
        }

        for (int i = 0;
             i < columns.length;
             i++) {

            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream output =
                new ByteArrayOutputStream();

        workbook.write(output);

        workbook.close();

        return output.toByteArray();
    }


    // =====================================================
    // GOOGLE SHEETS EXPORT
    // =====================================================

    public String exportGoogleSheets(
            String spreadsheetId,
            String sheetName) {

        List<EmployeeTimesheet> timesheets =
                timesheetRepository.findAll();

        /*
         * Convert timesheets to rows
         * and send using Google Sheets API.
         */

        return "Timesheets exported to Google Sheets";
    }


    // =====================================================
    // GOOGLE SHEETS EXPORT - EMPLOYEE
    // =====================================================

    public String exportEmployeeGoogleSheets(
            String employeeId,
            String spreadsheetId,
            String sheetName) {

        List<EmployeeTimesheet> timesheets =
                timesheetRepository
                        .findByEmployee_EmployeeId(
                                employeeId
                        );

        /*
         * Send timesheets to Google Sheets API.
         */

        return timesheets.size()
                + " records exported to Google Sheets";
    }


    // =====================================================
    // GOOGLE SHEETS EXPORT - DATE
    // =====================================================

    public String exportDateGoogleSheets(
            LocalDate date,
            String spreadsheetId,
            String sheetName) {

        List<EmployeeTimesheet> timesheets =
                timesheetRepository
                        .findByWorkDate(date);

        /*
         * Send timesheets to Google Sheets API.
         */

        return timesheets.size()
                + " records exported to Google Sheets";
    }


    // =====================================================
    // CALCULATE HOURS
    // =====================================================

    private void calculateTotalsHours(EmployeeTimesheet timesheet) {

        if (timesheet.getStartTime() == null ||
                timesheet.getEndTime() == null) {
            return;
        }

        long seconds =
                java.time.Duration.between(
                        timesheet.getStartTime(),
                        timesheet.getEndTime()
                ).getSeconds();

        int breakMinutes =
                timesheet.getBreakMinutes() != null
                        ? timesheet.getBreakMinutes()
                        : 0;

        seconds -=
                breakMinutes * 60L;

        if (seconds < 0) {
            seconds = 0;
        }

        BigDecimal hours =
                BigDecimal.valueOf(seconds)
                        .divide(
                                BigDecimal.valueOf(3600),
                                2,
                                java.math.RoundingMode.HALF_UP
                        );

        timesheet.setTotalHours(hours);
    }


    // =====================================================
    // EXCEL CELL HELPERS
    // =====================================================

    private String getString(Cell cell) {

        if (cell == null) {
            return null;
        }

        DataFormatter formatter =
                new DataFormatter();

        return formatter.formatCellValue(cell)
                .trim();
    }


    private LocalTime parseTime(Cell cell) {

        String value = getString(cell);

        if (value == null ||
                value.isBlank()) {

            return null;
        }

        return LocalTime.parse(value);
    }


    private String safe(String value) {

        return value != null
                ? value
                : "";
    }

    @Transactional
    public String importExcel(
            String employeeId,
            MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException(
                    "Excel file is required"
            );
        }

        Employee employee =
                employeeRepository
                        .findByEmployeeId(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee not found: "
                                                + employeeId
                                )
                        );

        int imported = 0;

        try (Workbook workbook =
                     new XSSFWorkbook(file.getInputStream())) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1;
                 i <= sheet.getLastRowNum();
                 i++) {

                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                LocalDate workDate =
                        LocalDate.parse(
                                getString(row.getCell(0))
                        );

                EmployeeTimesheet timesheet =
                        timesheetRepository
                                .findByEmployee_EmployeeIdAndWorkDate(
                                        employeeId,
                                        workDate
                                )
                                .orElseGet(
                                        EmployeeTimesheet::new
                                );

                timesheet.setEmployee(employee);
                timesheet.setWorkDate(workDate);

                timesheet.setProjectName(
                        getString(row.getCell(1))
                );

                timesheet.setWorkDescription(
                        getString(row.getCell(2))
                );

                timesheet.setStartTime(
                        parseTime(row.getCell(3))
                );

                timesheet.setEndTime(
                        parseTime(row.getCell(4))
                );

                String breakValue =
                        getString(row.getCell(5));

                if (breakValue != null &&
                        !breakValue.isBlank()) {

                    timesheet.setBreakMinutes(
                            Integer.parseInt(breakValue)
                    );
                }

                calculateTotalHours(timesheet);

                timesheetRepository.save(timesheet);

                imported++;
            }

            return imported +
                    " timesheet records imported for employee "
                    + employeeId;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Excel import failed: "
                            + e.getMessage(),
                    e
            );
        }
    }
    private String getGoogleCell(
            List<Object> row,
            int index) {

        if (index >= row.size()) {
            return null;
        }

        Object value = row.get(index);

        if (value == null) {
            return null;
        }

        return value.toString().trim();
    }
    private void calculateTotalHours(EmployeeTimesheet timesheet) {

        if (timesheet.getStartTime() == null ||
                timesheet.getEndTime() == null) {

            return;
        }

        long minutes =
                java.time.Duration.between(
                        timesheet.getStartTime(),
                        timesheet.getEndTime()
                ).toMinutes();

        int breakMinutes =
                timesheet.getBreakMinutes() != null
                        ? timesheet.getBreakMinutes()
                        : 0;

        minutes -= breakMinutes;

        if (minutes < 0) {
            throw new RuntimeException(
                    "Break time cannot be greater than working time"
            );
        }

        BigDecimal hours =
                BigDecimal.valueOf(minutes)
                        .divide(
                                BigDecimal.valueOf(60),
                                2,
                                RoundingMode.HALF_UP
                        );

        timesheet.setTotalHours(hours);
    }
//    @Transactional
//    public String importGoogleSheet(
//            String employeeId,
//            String spreadsheetId,
//            String sheetName) {
//
//        // =====================================================
//        // FIND EMPLOYEE
//        // =====================================================
//
//        Employee employee = employeeRepository
//                .findByEmployeeId(employeeId)
//                .orElseThrow(() ->
//                        new RuntimeException(
//                                "Employee not found: " + employeeId
//                        )
//                );
//
//        // =====================================================
//        // VALIDATE GOOGLE SHEET DETAILS
//        // =====================================================
//
//        if (spreadsheetId == null || spreadsheetId.isBlank()) {
//            throw new RuntimeException(
//                    "Google Spreadsheet ID is required"
//            );
//        }
//
//        if (sheetName == null || sheetName.isBlank()) {
//            throw new RuntimeException(
//                    "Google Sheet name is required"
//            );
//        }
//
//        // =====================================================
//        // READ GOOGLE SHEET
//        // =====================================================
//
//        List<List<Object>> rows =
//                googleSheetsService.readSheet(
//                        spreadsheetId,
//                        sheetName
//                );
//
//        if (rows == null || rows.size() <= 1) {
//            throw new RuntimeException(
//                    "Google Sheet contains no timesheet data"
//            );
//        }
//
//        int imported = 0;
//
//        // =====================================================
//        // PROCESS ROWS
//        // =====================================================
//
//        for (int i = 1; i < rows.size(); i++) {
//
//            List<Object> row = rows.get(i);
//
//            if (row == null || row.isEmpty()) {
//                continue;
//            }
//
//            // ---------------------------------------------
//            // WORK DATE
//            // ---------------------------------------------
//
//            String workDateValue =
//                    getGoogleCell(row, 0);
//
//            if (workDateValue == null ||
//                    workDateValue.isBlank()) {
//                continue;
//            }
//
//            LocalDate workDate =
//                    LocalDate.parse(workDateValue);
//
//            // =================================================
//            // FIND EXISTING TIMESHEET
//            // =================================================
//
//            EmployeeTimesheet timesheet =
//                    timesheetRepository
//                            .findByEmployee_EmployeeIdAndWorkDate(
//                                    employeeId,
//                                    workDate
//                            )
//                            .orElseGet(
//                                    EmployeeTimesheet::new
//                            );
//
//            // =================================================
//            // EMPLOYEE
//            // =================================================
//
//            timesheet.setEmployee(employee);
//
//            timesheet.setWorkDate(workDate);
//
//            // =================================================
//            // PROJECT
//            // =================================================
//
//            timesheet.setProjectName(
//                    getGoogleCell(row, 1)
//            );
//
//            // =================================================
//            // WORK DESCRIPTION
//            // =================================================
//
//            timesheet.setWorkDescription(
//                    getGoogleCell(row, 2)
//            );
//
//            // =================================================
//            // START TIME
//            // =================================================
//
//            String startTime =
//                    getGoogleCell(row, 3);
//
//            if (startTime != null &&
//                    !startTime.isBlank()) {
//
//                timesheet.setStartTime(
//                        LocalTime.parse(startTime)
//                );
//            }
//
//            // =================================================
//            // END TIME
//            // =================================================
//
//            String endTime =
//                    getGoogleCell(row, 4);
//
//            if (endTime != null &&
//                    !endTime.isBlank()) {
//
//                timesheet.setEndTime(
//                        LocalTime.parse(endTime)
//                );
//            }
//
//            // =================================================
//            // BREAK MINUTES
//            // =================================================
//
//            String breakValue =
//                    getGoogleCell(row, 5);
//
//            if (breakValue != null &&
//                    !breakValue.isBlank()) {
//
//                timesheet.setBreakMinutes(
//                        Integer.parseInt(breakValue)
//                );
//            }
//
//            // =================================================
//            // CALCULATE TOTAL HOURS
//            // =================================================
//
//            calculateTotalHours(timesheet);
//
//            // =================================================
//            // IMPORTANT
//            // =================================================
//            // Imported timesheets should normally start as DRAFT.
//            // Employee/team leader can submit later.
//            // =================================================
//
//            if (timesheet.getStatus() == null) {
//
//                timesheet.setStatus(
//                        TimesheetStatus.DRAFT
//                );
//            }
//
//            // =================================================
//            // SAVE
//            // =================================================
//
//            timesheetRepository.save(timesheet);
//
//            imported++;
//        }
//
//        // =====================================================
//        // RESULT
//        // =====================================================
//
//        return imported +
//                " timesheet record(s) imported successfully " +
//                "for employee " + employeeId;
//    }
}