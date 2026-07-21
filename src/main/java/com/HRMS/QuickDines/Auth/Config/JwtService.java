package com.HRMS.QuickDines.Auth.Config;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;


    @Value("${jwt.expiration}")
    private long expiration;


    public String generateToken(String username){
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey())
                .compact();
    }



    public String extractUsername(String token){

        return extractAllClaims(token).getSubject();

    }



    public boolean isTokenValid(String token,String username){
        return extractUsername(token).equals(username) && !isTokenExpired(token);

    }


    private boolean isTokenExpired(String token){
        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());

    }


    private Claims extractAllClaims(String token){

        return Jwts.parser()

                .verifyWith((SecretKey) getSignKey())

                .build()

                .parseSignedClaims(token)

                .getPayload();

    }



    private Key getSignKey(){

        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes()
        );

    }


}
