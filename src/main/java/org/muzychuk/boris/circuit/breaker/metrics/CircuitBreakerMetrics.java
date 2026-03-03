package org.muzychuk.boris.circuit.breaker.metrics;

import org.muzychuk.boris.circuit.breaker.domain.CircuitBreakerState;

public record CircuitBreakerMetrics(
        CircuitBreakerState state,
        int totalCalls,
        int successCount,
        int failureCount,
        int failurePercentage,
        int consecutiveSuccessesInHalfOpen
) {
}
