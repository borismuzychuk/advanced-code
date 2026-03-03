package org.muzychuk.boris.circuit.breaker.state.impl;

import org.muzychuk.boris.circuit.breaker.domain.CircuitBreakerResult;
import org.muzychuk.boris.circuit.breaker.domain.CircuitBreakerState;
import org.muzychuk.boris.circuit.breaker.state.CircuitBrakerStateContext;
import org.muzychuk.boris.circuit.breaker.state.State;

import java.time.Instant;
import java.util.function.Supplier;

public class HalfOpenState implements State {

    private final CircuitBrakerStateContext context;

    public HalfOpenState(CircuitBrakerStateContext context) {
        this.context = context;
    }

    @Override
    public <T> CircuitBreakerResult<T> execute(Supplier<T> action, Supplier<T> fallback, Instant now) {
        return null;
    }

    @Override
    public void open() {
        context.changeState(new OpenState(context));
    }

    @Override
    public void close() {
        context.changeState(new CloseState(context));
    }

    @Override
    public void halfOpen() {
        throw new UnsupportedOperationException("HALF_OPEN -> HALF_OPEN is denied");
    }

    @Override
    public CircuitBreakerState name() {
        return CircuitBreakerState.HALF_OPEN;
    }
}
