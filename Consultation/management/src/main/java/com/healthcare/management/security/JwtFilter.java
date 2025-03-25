package com.healthcare.management.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends OncePerRequestFilter {

   private final JwtUtil jwtUtil;

   public JwtFilter(JwtUtil jwtUtil) {
       this.jwtUtil = jwtUtil;
   }

   @Override
   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
           throws ServletException, IOException {

       String authHeader = request.getHeader("Authorization");

       if (authHeader == null || !authHeader.startsWith("Bearer ")) {
           chain.doFilter(request, response);
           return;
       }

       String token = authHeader.substring(7);
       if (jwtUtil.validateToken(token)!=null) {
           chain.doFilter(request, response);
           return;
       }

       // Extract user details from JWT
       String username = jwtUtil.extractUsername(token);
       List<String> roles = jwtUtil.extractRoles(token);

       // Convert roles to GrantedAuthority list
       List<org.springframework.security.core.GrantedAuthority> authorities = roles.stream()
               .map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority(role))
               .collect(Collectors.toList());

       // Create UserDetails object

       UserDetails userDetails = new User(username, "N/A", authorities);
       System.out.println("test:"+userDetails);
       // Set authentication in security context
       UsernamePasswordAuthenticationToken authToken =
               new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
       authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

       SecurityContextHolder.getContext().setAuthentication(authToken);

       chain.doFilter(request, response);
   }
}