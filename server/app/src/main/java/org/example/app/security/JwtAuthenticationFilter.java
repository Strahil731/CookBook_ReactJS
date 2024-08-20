package org.example.app.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.app.service.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String header = request.getHeader("Authorization");

        try {
            if (header != null && header.startsWith("Bearer ")) {
                final String jwtToken = header.replace("Bearer ", "");

                final UserDetails userDetails = jwtService.getUserDetails(jwtToken);

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    final Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,
                            null,
                            userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }

        filterChain.doFilter(request, response);
    }
}
