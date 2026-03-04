package org.muzychuk.boris.circuit.breaker.state;

import org.muzychuk.boris.circuit.breaker.config.CircuitBreakerConfig;
import org.muzychuk.boris.circuit.breaker.domain.CircuitBreakerResult;
import org.muzychuk.boris.circuit.breaker.metrics.CircuitBreakerMetrics;
import org.muzychuk.boris.circuit.breaker.metrics.CircuitBreakerMetricsHolder;

import java.time.Instant;
import java.util.function.Supplier;

public class CircuitBrakerStateContext {

    private final CircuitBreakerMetricsHolder metricsHolder;
    private final CircuitBreakerConfig config;
    private State state;

    public CircuitBrakerStateContext(CircuitBreakerMetricsHolder metricsHolder, CircuitBreakerConfig config) {
        this.metricsHolder = metricsHolder;
        this.config = config;
    }

    public <T> CircuitBreakerResult<T> execute(Supplier<T> action, Supplier<T> fallback, Instant now) {
        return state.execute(action, fallback, now);
    }


    public void changeState(State newState) {
        this.getMetricsHolder().updateState(newState.name());
        this.state = newState;
    }

    public State getState() {
        return state;
    }

    public CircuitBreakerMetrics getMetrics() {
        return metricsHolder.getMetrics();
    }

    public CircuitBreakerMetricsHolder getMetricsHolder() {
        return metricsHolder;
    }

    public CircuitBreakerConfig getConfig() {
        return config;
    }
}
