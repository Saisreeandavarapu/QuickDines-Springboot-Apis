package com.HRMS.QuickDines.AdvanceServices;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Configuration
public class GoogleSheetsConfig {

    @Bean
    public Sheets googleSheets() throws Exception {

        String credentialsJson =
                System.getenv("GOOGLE_CREDENTIALS_JSON");

        if (credentialsJson == null ||
                credentialsJson.isBlank()) {

            throw new RuntimeException(
                    "GOOGLE_CREDENTIALS_JSON environment variable not found"
            );
        }

        GoogleCredentials googleCredentials =
                GoogleCredentials
                        .fromStream(
                                new ByteArrayInputStream(
                                        credentialsJson.getBytes(
                                                StandardCharsets.UTF_8
                                        )
                                )
                        )
                        .createScoped(
                                Collections.singleton(
                                        SheetsScopes.SPREADSHEETS_READONLY
                                )
                        );

        return new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(
                        googleCredentials
                )
        )
                .setApplicationName("QuickDines HRMS")
                .build();
    }
}