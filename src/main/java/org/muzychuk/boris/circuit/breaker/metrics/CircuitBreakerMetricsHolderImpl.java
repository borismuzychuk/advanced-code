package org.muzychuk.boris.circuit.breaker.metrics;

import org.muzychuk.boris.circuit.breaker.domain.CircuitBreakerState;

public class CircuitBreakerMetricsHolderImpl implements CircuitBreakerMetricsHolder {

    private volatile CircuitBreakerMetrics metrics;

    public CircuitBreakerMetricsHolderImpl(CircuitBreakerMetrics metrics) {
        this.metrics = metrics;
    }

    @Override
    public synchronized void updateState(CircuitBreakerState newState) {
        this.metrics = new CircuitBreakerMetrics(
                newState,
                0,
                0,
                0,
                0,
                0
        );
    }

    @Override
    public synchronized void incrementSuccessCount() {
        this.metrics = new CircuitBreakerMetrics(
                this.metrics.state(),
                this.metrics.totalCalls() + 1,
                this.metrics.successCount() + 1,
                this.metrics.failureCount(),
                (int) (((double) (this.metrics.failureCount()) / (this.metrics.totalCalls() + 1)) * 100.00),
                this.metrics.consecutiveSuccessesInHalfOpen()
        );
    }

    @Override
    public synchronized void incrementFailureCount() {
        this.metrics = new CircuitBreakerMetrics(
                this.metrics.state(),
                this.metrics.totalCalls() + 1,
                this.metrics.successCount(),
                this.metrics.failureCount() + 1,
                (int) (((double) (this.metrics.failureCount() + 1) / (this.metrics.totalCalls() + 1)) * 100.00),
                this.metrics.consecutiveSuccessesInHalfOpen()
        );
    }

    @Override
    public synchronized void incrementConsecutiveSuccessesInHalfOpen() {
        this.metrics = new CircuitBreakerMetrics(
                this.metrics.state(),
                this.metrics.totalCalls() + 1,
                this.metrics.successCount() + 1,
                this.metrics.failureCount(),
                this.metrics.failureCount() / (this.metrics.totalCalls() + 1),
                this.metrics.consecutiveSuccessesInHalfOpen() + 1
        );
    }

    @Override
    public CircuitBreakerMetrics getMetrics() {
        return metrics;
    }
}
