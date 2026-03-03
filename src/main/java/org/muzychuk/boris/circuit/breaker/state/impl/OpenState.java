package org.muzychuk.boris.circuit.breaker.state.impl;

import org.muzychuk.boris.circuit.breaker.domain.CircuitBreakerResult;
import org.muzychuk.boris.circuit.breaker.domain.CircuitBreakerState;
import org.muzychuk.boris.circuit.breaker.state.CircuitBrakerStateContext;
import org.muzychuk.boris.circuit.breaker.state.State;

import java.time.Instant;
import java.util.function.Supplier;

public class OpenState implements State {

    private final CircuitBrakerStateContext context;

    public OpenState(CircuitBrakerStateContext context) {
        this.context = context;
    }

    @Override
    public <T> CircuitBreakerResult<T> execute(Supplier<T> action, Supplier<T> fallback, Instant now) {
        return CircuitBreakerResult.fallback(fallback.get(), name());
    }

    @Override
    public void open() {
        throw new UnsupportedOperationException("OPEN -> OPEN is denied");
    }

    @Override
    public void close() {
        context.changeState(new CloseState(context));
    }

    @Override
    public void halfOpen() {
        throw new UnsupportedOperationException("OPEN -> HALF_OPEN is denied");
    }

    @Override
    public CircuitBreakerState name() {
        return CircuitBreakerState.OPEN;
    }
}
