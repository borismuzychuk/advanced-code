package org.muzychuk.boris.circuit.breaker.state;

import org.muzychuk.boris.circuit.breaker.domain.CircuitBreakerResult;
import org.muzychuk.boris.circuit.breaker.domain.CircuitBreakerState;

import java.util.function.Supplier;

public abstract class AbstractState implements State {


    public <T> CircuitBreakerResult<T> handleAction(Supplier<T> action, Supplier<T> fallback, CircuitBreakerState circuitBreakerState) {
        try {
            return CircuitBreakerResult.success(action.get(), circuitBreakerState);
        } catch (Exception e) {
            return handleFallback(fallback, circuitBreakerState);
        }
    }

    public <T> CircuitBreakerResult<T> handleFallback(Supplier<T> fallback, CircuitBreakerState circuitBreakerState) {
        try {
            return CircuitBreakerResult.fallback(fallback.get(), circuitBreakerState);
        } catch (Exception ex) {
            return CircuitBreakerResult.rejected(circuitBreakerState);
        }
    }

}
