package org.muzychuk.boris.circuit.breaker.state.impl;

import org.muzychuk.boris.circuit.breaker.domain.CircuitBreakerResult;
import org.muzychuk.boris.circuit.breaker.domain.CircuitBreakerState;
import org.muzychuk.boris.circuit.breaker.domain.ResultType;
import org.muzychuk.boris.circuit.breaker.state.AbstractState;
import org.muzychuk.boris.circuit.breaker.state.CircuitBrakerStateContext;

import java.time.Instant;
import java.util.function.Supplier;

public class HalfOpenState extends AbstractState {

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
            CircuitBreakerResult<T> result = handleAction(action, fallback, null);
            if (ResultType.SUCCESS == result.type()) {
                context.getMetricsHolder().incrementConsecutiveSuccessesInHalfOpen();
                close();
            } else {
                context.getMetricsHolder().incrementFailureCount();
                open();
            }
            return new CircuitBreakerResult<>(result.value(), result.type(), context.getState().name(), result.exception());
        } else {
            return handleFallback(fallback, context.getState().name());
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
