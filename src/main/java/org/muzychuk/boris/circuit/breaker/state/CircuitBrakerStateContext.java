package org.muzychuk.boris.circuit.breaker.state;

import org.muzychuk.boris.circuit.breaker.domain.CircuitBreakerResult;
import org.muzychuk.boris.circuit.breaker.metrics.CircuitBreakerMetrics;
import org.muzychuk.boris.circuit.breaker.metrics.CircuitBreakerMetricsHolder;

import java.time.Instant;
import java.util.function.Supplier;

public class CircuitBrakerStateContext {

    private final CircuitBreakerMetricsHolder metricsHolder;
    private State state;

    public CircuitBrakerStateContext(CircuitBreakerMetricsHolder metricsHolder) {
        this.metricsHolder = metricsHolder;
    }

    public <T> CircuitBreakerResult<T> execute(Supplier<T> action, Supplier<T> fallback, Instant now) {
        return state.execute(action, fallback, now);
    }


    public void changeState(State newState) {
        this.state = newState;
    }

    public State getState() {
        return state;
    }

    public CircuitBreakerMetrics getMetrics() {
        return metricsHolder.getMetrics();
    }
}
