package org.muzychuk.boris.ratelimiter.domain;

public record Token(long timestamp) {
    // Не придумал какую информацию можно в токенах хранить, но ,возможно, timestamp будет полезен в логах
}
