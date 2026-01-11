//package com.jutjubiccorps.jutjubic.security.auth;
//
//import org.springframework.stereotype.Component;
//import com.github.bucket4j.Bucket;
//import com.github.bucket4j.Bucket4j;
//import com.github.bucket4j.Refill;
//import com.github.bucket4j.Bandwidth;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.GenericFilterBean;
//
//import java.time.Duration;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Component
//public class LoginRateLimitFilter extends GenericFilterBean {
//
//    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
//
//    private Bucket resolveBucket(String ip) {
//        return buckets.computeIfAbsent(ip, k -> {
//            Bandwidth limit = Bandwidth.classic(5, Refill.intervally(5, Duration.ofMinutes(1)));
//            return Bucket4j.builder().addLimit(limit).build();
//        });
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws java.io.IOException, jakarta.servlet.ServletException {
//        HttpServletRequest req = (HttpServletRequest) request;
//        HttpServletResponse res = (HttpServletResponse) response;
//
//        if ("/api/auth/login".equals(req.getRequestURI()) && "POST".equalsIgnoreCase(req.getMethod())) {
//            String ip = req.getRemoteAddr();
//            Bucket bucket = resolveBucket(ip);
//
//            if (bucket.tryConsume(1)) {
//                chain.doFilter(request, response);
//            } else {
//                res.setStatus(429);
//                res.getWriter().write("Too many login attempts. Try again later.");
//                return;
//            }
//        } else {
//            chain.doFilter(request, response);
//        }
//    }
//}