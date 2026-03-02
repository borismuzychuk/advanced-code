package org.muzychuk.boris.ratelimiter.domain;


import lombok.Getter;

@Getter
public enum ClientTier {

    FREE(60, 60_000),        // 60 req/min
    STANDARD(600, 60_000),   // 600 req/min
    PREMIUM(6000, 60_000);   // 6000 req/min

    final int maxRequests;
    final long windowMs;

    ClientTier(int maxRequests, long windowMs) {
        this.maxRequests = maxRequests;
        this.windowMs = windowMs;
    }

}
