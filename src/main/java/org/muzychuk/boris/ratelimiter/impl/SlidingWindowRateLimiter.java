package org.muzychuk.boris.ratelimiter.impl;

import org.muzychuk.boris.ratelimiter.RateLimiter;
import org.muzychuk.boris.ratelimiter.config.RateLimitConfig;
import org.muzychuk.boris.ratelimiter.domain.RateLimitResult;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SlidingWindowRateLimiter implements RateLimiter {

    private final RateLimitConfig config;
    private final Queue<Long> logTimestamps;
    private final Object lock;

    public SlidingWindowRateLimiter(RateLimitConfig config) {
        this.config = config;
        this.logTimestamps = new ConcurrentLinkedQueue<>();
        this.lock = new Object();
    }

    @Override
    public RateLimitResult tryAcquire(String clientId) {
        synchronized (lock) {
            long now = System.currentTimeMillis();
            long start = now - config.windowMs();
            while (!logTimestamps.isEmpty() && logTimestamps.peek() < start) {
                logTimestamps.poll();
            }
            if (logTimestamps.size() < config.maxRequests()) {
                logTimestamps.add(now);
                return new RateLimitResult(true, config.maxRequests() - logTimestamps.size(), 0);
            }
            return new RateLimitResult(false, 0, config.windowMs());
        }
    }
}
