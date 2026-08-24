package com.HRMS.QuickDines.Auth.Config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private long expiration;


    // =====================================================
    // ACCESS TOKEN
    // =====================================================

    public String generateToken(String employeeId) {

        return Jwts.builder()
                .subject(employeeId)
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + expiration
                        )
                )
                .signWith(getSignKey())
                .compact();
    }


    // =====================================================
    // REFRESH TOKEN
    // =====================================================

    public String generateRefreshToken(String employeeId) {

        return Jwts.builder()
                .subject(employeeId)
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + 1000L
                                        * 60
                                        * 60
                                        * 24
                                        * 7
                        )
                )
                .signWith(getSignKey())
                .compact();
    }


    // =====================================================
    // EXTRACT EMPLOYEE ID
    // =====================================================

    public String extractUsername(String token) {

        return extractAllClaims(token).getSubject();
    }


    // =====================================================
    // VALIDATE TOKEN
    // =====================================================

    public boolean isTokenValid(
            String token,
            String employeeId) {

        return extractUsername(token)
                .equals(employeeId)
                && !isTokenExpired(token);
    }


    // =====================================================
    // CHECK EXPIRATION
    // =====================================================

    private boolean isTokenExpired(String token) {

        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }


    // =====================================================
    // EXTRACT CLAIMS
    // =====================================================

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    // =====================================================
    // SECRET KEY
    // =====================================================

    private SecretKey getSignKey() {

        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes(
                        StandardCharsets.UTF_8
                )
        );
    }
}