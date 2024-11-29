package com.mfa.report.security;

import com.mfa.report.repository.exception.TooManyRequestsException;
import io.github.bucket4j.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

@Component
public class Bucket4jRateLimiter {

    private static final int MAX_REQUESTS = 100;
    private static final long REFILL_INTERVAL = 1; // minute
    private static final long REFILL_AMOUNT = 120; // max requests in one minute

    private final Bucket bucket;

    public Bucket4jRateLimiter() {
        Bandwidth limit = Bandwidth.classic(MAX_REQUESTS, Refill.greedy(REFILL_AMOUNT, Duration.ofMinutes(REFILL_INTERVAL)));
        this.bucket = Bucket4j.builder().addLimit(limit).build();
    }

    public boolean tryConsume(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            return true;
        } else {
            response.setStatus(429);
            response.getWriter().write("Too many requests. Please try again later.");
            return false;
        }
    }
}
