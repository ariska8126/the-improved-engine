/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadirapp.HadirAuth.jwtutil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import net.minidev.json.JSONObject;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author herli
 */
public class JwtUtil {
    
    private String SECRET_KEY = "secret";
    
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }
    
    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }
    
    public <T> T extractClaim(String token, Function<Claims, T> claimResolver){
        final Claims claims = extractAllClaim(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaim(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }
    
    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }
    
    public String generateToken(UserDetails userDetails, JSONObject j){
        Map<String, Object> claims = new HashMap<>();
        claims.put("data", j);
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(
                new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis()+
                        1000*60*60)).signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }
    
    public boolean validateToken(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
