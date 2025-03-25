package com.healthcare.management.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

   // Secret key (for demo, use a more secure key in production)
   private static final String SECRET_KEY = "YourSecretKeyForJwtShouldBeAtLeast32CharsLong";

   // Token expiration time (e.g., 10 minutes)
   private static final long EXPIRATION_TIME = 10 * 60 * 1000;

   // Generate JWT Token
   public String generateToken(String username) {
       Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
       Map<String, Object> claims = new HashMap<>();

 

       return Jwts.builder()
               .setSubject(username)
               .setIssuedAt(new Date())
               .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
               .signWith(key, SignatureAlgorithm.HS256)
               .compact();
   }
   public static Claims extractAllClaims(String token) {
       Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

       return Jwts.parserBuilder()
               .setSigningKey(key)
               .build()
               .parseClaimsJws(token)
               .getBody();
   }
   public List<String> extractRoles(String token) {
   	return extractAllClaims(token).get("roles", List.class);
   }


   // Extract specific claim (e.g., username)
   public static String extractUsername(String token) {
   	System.out.println("extractAllClaims(token):"+extractAllClaims(token));
       return extractAllClaims(token).get("userInfo", String.class);
   }

   // Extract expiration date
   public static Date extractExpiration(String token) {
       return extractAllClaims(token).getExpiration();
   }

   // Validate and parse JWT Token
   public String validateToken(String token) {
       try {
           Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
           return Jwts.parserBuilder()
                   .setSigningKey(key)
                   .build()
                   .parseClaimsJws(token)
                   .getBody()
                   .getSubject(); // Return username
       } catch (ExpiredJwtException e) {
           return "Token expired";
       } catch (JwtException e) {
           return "Invalid token";
       }
   }

}
