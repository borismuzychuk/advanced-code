package org.muzychuk.boris.circuit.breaker.state.impl;

import org.muzychuk.boris.circuit.breaker.CircuitBreaker;
import org.muzychuk.boris.circuit.breaker.config.CircuitBreakerConfig;
import org.muzychuk.boris.circuit.breaker.domain.CircuitBreakerResult;
import org.muzychuk.boris.circuit.breaker.domain.CircuitBreakerState;
import org.muzychuk.boris.circuit.breaker.metrics.CircuitBreakerMetrics;
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
        /* выполнить запрос
         обновить метрики с учетном ответа (SUCCESS или FAILED)
         после обновления метрик либо оставить текущий статус, либо перевести в другой
         вернуть ответ
         */
        try {
            T response = action.get();
            context.getMetricsHolder().incrementSuccessCount();
            open();
            return CircuitBreakerResult.success(response, context.getState().name());
        } catch (Exception e) {
            context.getMetricsHolder().incrementFailureCount();
            open();
            try {
                return CircuitBreakerResult.fallback(fallback.get(), context.getState().name());
            } catch (Exception ex) {
                return CircuitBreakerResult.rejected(context.getState().name());
            }
        }

    }

    @Override
    public void open() {
        CircuitBreakerMetrics metrics = context.getMetrics();
        CircuitBreakerConfig config = context.getConfig();
        if (config.failureThreshold() <= metrics.failurePercentage()) {
            context.changeState(new OpenState(context));
        }
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("CLOSED -> CLOSED is denied");
    }

    @Override
    public void halfOpen() {
        throw new UnsupportedOperationException("CLOSED -> OPEN is denied");
    }

    @Override
    public CircuitBreakerState name() {
        return CircuitBreakerState.CLOSED;
    }
}
