package org.muzychuk.boris.ratelimiter.api;

import org.muzychuk.boris.ratelimiter.RateLimiterFactory;
import org.muzychuk.boris.ratelimiter.domain.RateLimitResult;
import org.muzychuk.boris.ratelimiter.domain.RateLimiterType;

public class RequestHandler {

    private final RateLimiterFactory rateLimiterFactory;
    private final RateLimiterType rateLimiterType;

    //For test
    public RequestHandler() {
        rateLimiterFactory = new RateLimiterFactory();
        this.rateLimiterType = RateLimiterType.TOKEN_BUCKET;
//        this.rateLimiterType = RateLimiterType.FIXED_WINDOW;
//        this.rateLimiterType = RateLimiterType.SLIDING_WINDOW;
    }

    public void handle(RequestDto request) {
        RateLimitResult rateLimitResult = rateLimiterFactory.getRateLimiter(rateLimiterType.getName())
                .tryAcquire(request.clientId());
        // todo handle rate limiter result
    }

}
