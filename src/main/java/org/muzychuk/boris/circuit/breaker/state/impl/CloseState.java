package org.muzychuk.boris.circuit.breaker.state.impl;

import org.muzychuk.boris.circuit.breaker.domain.CircuitBreakerResult;
import org.muzychuk.boris.circuit.breaker.domain.CircuitBreakerState;
import org.muzychuk.boris.circuit.breaker.state.CircuitBrakerStateContext;
import org.muzychuk.boris.circuit.breaker.state.State;

import java.time.Instant;
import java.util.function.Supplier;

public class CloseState implements State {

    private final CircuitBrakerStateContext context;

    public CloseState(CircuitBrakerStateContext context) {
        this.context = context;
    }

    @Override
    public <T> CircuitBreakerResult<T> execute(Supplier<T> action, Supplier<T> fallback, Instant now) {

        return null;
    }

    @Override
    public void open() {
        throw new UnsupportedOperationException("CLOSED -> OPEN is denied");
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("CLOSED -> CLOSED is denied");
    }

    @Override
    public void halfOpen() {
        context.changeState(new HalfOpenState(context));
    }

    @Override
    public CircuitBreakerState name() {
        return CircuitBreakerState.CLOSED;
    }
}
