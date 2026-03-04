package org.muzychuk.boris.circuit.breaker.metrics;

import org.muzychuk.boris.circuit.breaker.domain.CircuitBreakerState;

public interface CircuitBreakerMetricsHolder {

    void updateState(CircuitBreakerState newState);

    void incrementSuccessCount();

    void incrementFailureCount();

    void incrementConsecutiveSuccessesInHalfOpen();

    CircuitBreakerMetrics getMetrics();
}
