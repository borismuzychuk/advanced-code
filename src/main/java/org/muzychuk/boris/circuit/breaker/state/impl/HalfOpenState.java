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
         /* если currentHalfOpenCalls < maxHalfOpenCalls
         то выполинть запрос и обновить метрики с учетном ответа (SUCCESS или FAILED)
         после обновления метрик либо оставить текущий статус, либо перевести в другой
         иначе вернуть ResultType.FALLBACK */
        if (context.getMetrics().totalCalls() < context.getConfig().maxHalfOpenCalls()) {
            try {
                T response = action.get();
                context.getMetricsHolder().incrementConsecutiveSuccessesInHalfOpen();
                close();
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
        } else {
            return CircuitBreakerResult.fallback(fallback.get(), context.getState().name());
        }
    }

    @Override
    public void open() {
        context.changeState(new OpenState(context));
    }

    @Override
    public void close() {
        if (context.getConfig().successThreshold() <= context.getMetrics().consecutiveSuccessesInHalfOpen()) {
            context.changeState(new CloseState(context));
        }
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
