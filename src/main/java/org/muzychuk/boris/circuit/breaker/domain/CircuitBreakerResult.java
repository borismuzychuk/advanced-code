package org.muzychuk.boris.circuit.breaker.domain;

public record CircuitBreakerResult<T>(
        T value,
        ResultType type,           // SUCCESS, FALLBACK, REJECTED
        CircuitBreakerState state, // Состояние после вызова
        Exception exception        // null если успех
) {

    public static <T> CircuitBreakerResult<T> success(T value, CircuitBreakerState state) {
        return new CircuitBreakerResult<>(value, ResultType.SUCCESS, state, null);
    }

    public static <T> CircuitBreakerResult<T> fallback(T value, CircuitBreakerState state) {
        return new CircuitBreakerResult<>(value, ResultType.FALLBACK, state, null);
    }

    public static <T> CircuitBreakerResult<T> rejected(CircuitBreakerState state) {
        return new CircuitBreakerResult<>(null, ResultType.REJECTED, state, null);
    }

}
