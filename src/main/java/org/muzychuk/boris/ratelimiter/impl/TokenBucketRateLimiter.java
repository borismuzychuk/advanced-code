package org.muzychuk.boris.ratelimiter.impl;


import org.muzychuk.boris.ratelimiter.RateLimiter;
import org.muzychuk.boris.ratelimiter.config.RateLimitConfig;
import org.muzychuk.boris.ratelimiter.domain.RateLimitResult;
import org.muzychuk.boris.ratelimiter.domain.Token;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TokenBucketRateLimiter implements RateLimiter {

    private final RateLimitConfig config;
    private final ScheduledExecutorService scheduledExecutorService;
    private final Queue<Token> bucket;
    private final Object lock;

    public TokenBucketRateLimiter(RateLimitConfig config) {
        this.config = config;
        this.scheduledExecutorService = Executors.newScheduledThreadPool(1);
        this.lock = new Object();
        this.bucket = new ConcurrentLinkedQueue<>();
        scheduledFillTokenBucket(config);
    }

    private void scheduledFillTokenBucket(RateLimitConfig config) {
        this.scheduledExecutorService.scheduleAtFixedRate(() -> {
                    while (bucket.size() < config.maxRequests()) {
                        bucket.add(new Token(System.currentTimeMillis()));
                    }
                },
                0, config.windowMs(), TimeUnit.MILLISECONDS);
    }

    @Override
    public RateLimitResult tryAcquire(String clientId) {
        // думаю синхронизация здесь нужна, потому что без нее могут быть проблемы с публикацией объекта RateLimitResult()
        synchronized (lock) {
            if (bucket.isEmpty()) {
                return new RateLimitResult(false, 0, config.windowMs());
            }
            return new RateLimitResult(true, bucket.size(), 0);
        }
    }
}
