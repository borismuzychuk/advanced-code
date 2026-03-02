package org.muzychuk.boris.ratelimiter.config;

public record RateLimitConfig(
        int maxRequests,
        long windowMs
) {
}
