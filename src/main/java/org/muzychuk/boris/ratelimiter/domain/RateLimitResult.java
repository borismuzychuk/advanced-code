package org.muzychuk.boris.ratelimiter.domain;

public record RateLimitResult(
        boolean allowed,
        int remainingRequests,
        long retryAfterMs  // через сколько мс можно повторить, если отклонён
) {

}
