package com.project.ecommerce.filter;

import com.project.ecommerce.service.CustomUserDetailsService;
import com.project.ecommerce.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        System.out.println("=== Filter läuft: " + request.getRequestURI() + " - " + request.getMethod());
        System.out.println("=== Header: " + request.getHeader("Authorization"));

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }


        String token = header.substring(7);
        String email;




        try{
            email = jwtService.extractEmail(token);
        } catch (Exception e){
            filterChain.doFilter(request, response);
            return;
        }



        if (email == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

            if (jwtService.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);


            }
        } catch (Exception e) {

            System.out.println("=== Exception: " + e.getClass().getName());
            System.out.println("=== Message: " + e.getMessage());

        }

        filterChain.doFilter(request, response);

    }
}
