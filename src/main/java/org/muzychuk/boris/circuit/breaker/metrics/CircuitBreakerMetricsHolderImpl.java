package org.muzychuk.boris.circuit.breaker.metrics;

import org.muzychuk.boris.circuit.breaker.domain.CircuitBreakerState;

public class CircuitBreakerMetricsHolderImpl implements CircuitBreakerMetricsHolder {

    private CircuitBreakerState state;
    private int totalCalls;
    private int successCount;
    private int failureCount;
    private int failurePercentage;
    private int consecutiveSuccessesInHalfOpen;

    public CircuitBreakerMetricsHolderImpl(CircuitBreakerState state) {
        this.state = state;
    }

    @Override
    public void updateState(CircuitBreakerState newState) {
        this.state = newState;
    }

    @Override
    public void incrementTotalCalls() {

    }

    @Override
    public void incrementSuccessCount() {

    }

    @Override
    public void incrementFailureCount() {

    }

    @Override
    public void incrementConsecutiveSuccessesInHalfOpen() {

    }

    @Override
    public CircuitBreakerMetrics getMetrics() {
        return new CircuitBreakerMetrics(
                state,
                totalCalls,
                successCount,
                failureCount,
                failurePercentage,
                consecutiveSuccessesInHalfOpen
        );
    }
}
