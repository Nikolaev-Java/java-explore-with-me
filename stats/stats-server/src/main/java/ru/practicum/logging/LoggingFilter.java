package ru.practicum.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
public class LoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        CachedBodyHttpServletsRequest httpServletRequest = new CachedBodyHttpServletsRequest(request);
        filterChain.doFilter(httpServletRequest, response);
        StringBuilder requestBody = new StringBuilder();
        InputStream inputStream = httpServletRequest.getInputStream();
        if ("POST".equals(request.getMethod())) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    requestBody.append(line);
                }
            }
        }
        String logMessage = String.format("request method: %s, request URI: %s, body: %s, param: %s, response status: %d, ",

                request.getMethod(), request.getRequestURI(), requestBody, request.getQueryString(),
                response.getStatus());

        // Запись лога
        logger.info(logMessage);
    }
}
