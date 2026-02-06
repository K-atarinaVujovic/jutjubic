package com.jutjubiccorps.jutjubic.security.auth;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;

@Component
public class LoginRateLimitFilter implements Filter {

    private static final int MAX_REQUESTS = 5;
    private static final long INTERVAL_SEC = 60_000;

    private final Map<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    private final Map<String, Long> timestamps = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();

        if (!path.equals("/api/auth/login") || !method.equals("POST")) {
            chain.doFilter(request, response); // dont do rate limiter for other requests
            return;
        }

        String clientIp = httpRequest.getRemoteAddr();

        long now = System.currentTimeMillis();

        timestamps.putIfAbsent(clientIp, now);
        requestCounts.putIfAbsent(clientIp, new AtomicInteger(0));

        // Reset after 1 minute
        if (now - timestamps.get(clientIp) > INTERVAL_SEC) {
            requestCounts.get(clientIp).set(0);
            timestamps.put(clientIp, now);
        }

        int currentCount = requestCounts.get(clientIp).incrementAndGet();

        if (currentCount > MAX_REQUESTS) {
            httpResponse.setStatus(429); // Too many requests code
            httpResponse.setContentType("application/json");

            httpResponse.getWriter().write("""
                {
                  "error": "Too many login attempts",
                  "message": "Please wait 1 minute before trying again"
                }
            """);
            return;
        }

        chain.doFilter(request, response);
    }
}
