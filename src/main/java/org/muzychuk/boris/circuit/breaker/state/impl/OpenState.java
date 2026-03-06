package org.muzychuk.boris.circuit.breaker.state.impl;

import org.muzychuk.boris.circuit.breaker.domain.CircuitBreakerResult;
import org.muzychuk.boris.circuit.breaker.domain.CircuitBreakerState;
import org.muzychuk.boris.circuit.breaker.state.AbstractState;
import org.muzychuk.boris.circuit.breaker.state.CircuitBrakerStateContext;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

public class OpenState extends AbstractState {

    private final CircuitBrakerStateContext context;

    public OpenState(CircuitBrakerStateContext context) {
        this.context = context;
    }

    @Override
    public <T> CircuitBreakerResult<T> execute(Supplier<T> action, Supplier<T> fallback, Instant now) {
        /*
         проверить waitDuration,
         если с момента размыкания не прошло waitDuration,
         то вернуть ResultType.FALLBACK, иначе переход в HALF_OPEN
         */
        Duration waitDuration = context.getConfig().waitDuration();
        Instant lastSwitchToOpenTime = context.getLastSwitchToTime();
        if (now.toEpochMilli() - lastSwitchToOpenTime.toEpochMilli() >= waitDuration.toMillis()) {
            halfOpen();
            return handleFallback(fallback, context.getState().name());
        }
        return handleFallback(fallback, name());// CircuitBreakerResult.fallback(fallback.get(), name());
    }

    @Override
    public void open() {
        throw new UnsupportedOperationException("OPEN -> OPEN is denied");
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("OPEN -> CLOSE is denied");
    }

    @Override
    public void halfOpen() {
        context.changeState(new HalfOpenState(context));
    }

    @Override
    public CircuitBreakerState name() {
        return CircuitBreakerState.OPEN;
    }
}
