package org.muzychuk.boris.ratelimiter.impl;

import org.muzychuk.boris.ratelimiter.RateLimiter;
import org.muzychuk.boris.ratelimiter.config.RateLimitConfig;
import org.muzychuk.boris.ratelimiter.domain.RateLimitResult;

import java.util.concurrent.atomic.AtomicInteger;

public class FixedWindowRateLimiter implements RateLimiter {

    private final RateLimitConfig config;
    private volatile long startTimestamp;
    private final AtomicInteger counter;
    private final Object lock;

    public FixedWindowRateLimiter(RateLimitConfig config) {
        this.config = config;
        this.startTimestamp = System.currentTimeMillis();
        this.counter = new AtomicInteger(0);
        this.lock = new Object();
    }

    @Override
    public RateLimitResult tryAcquire(String clientId) {
        synchronized (lock) {
            long now = System.currentTimeMillis();
            if (now - startTimestamp > config.windowMs()) {
                counter.set(0);
                startTimestamp = now;
            }
            if (counter.get() < config.maxRequests()) {
                return new RateLimitResult(true, config.maxRequests() - counter.get(), 0);
            }
            return new RateLimitResult(false, 0, config.windowMs());
        }
    }
}
