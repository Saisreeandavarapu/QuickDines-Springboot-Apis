package com.HRMS.QuickDines.AdvanceServices;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.util.Collections;

@Configuration
public class GoogleSheetsConfig {

    @Bean
    public Sheets googleSheets() throws Exception {

        InputStream credentials =
                getClass()
                        .getClassLoader()
                        .getResourceAsStream(
                                "google-credentials.json"
                        );

        if (credentials == null) {
            throw new RuntimeException(
                    "google-credentials.json not found"
            );
        }

        GoogleCredentials googleCredentials =
                GoogleCredentials
                        .fromStream(credentials)
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