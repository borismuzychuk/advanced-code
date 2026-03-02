package org.muzychuk.boris.ratelimiter;


import org.muzychuk.boris.ratelimiter.domain.RateLimitResult;

public interface RateLimiter {

    /**
     * Проверить, разрешён ли запрос для данного клиента.
     *
     * @param clientId идентификатор клиента
     * @return результат проверки
     */
    RateLimitResult tryAcquire(String clientId);

}
