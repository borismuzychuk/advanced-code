package org.muzychuk.boris.circuit.breaker;

import org.muzychuk.boris.circuit.breaker.domain.CircuitBreakerResult;
import org.muzychuk.boris.circuit.breaker.domain.CircuitBreakerState;
import org.muzychuk.boris.circuit.breaker.metrics.CircuitBreakerMetrics;
import org.muzychuk.boris.circuit.breaker.state.CircuitBrakerStateContext;
import org.muzychuk.boris.circuit.breaker.state.impl.CloseState;

import java.time.Instant;
import java.util.function.Supplier;

/**
 * Circuit Breaker для защиты от каскадных отказов.
 * <p>
 * Потокобезопасный: несколько потоков могут одновременно
 * вызывать execute.
 */
public class CircuitBreaker {

    private final CircuitBrakerStateContext stateContext;

    public CircuitBreaker(CircuitBrakerStateContext stateContext) {
        this.stateContext = stateContext;
        stateContext.changeState(new CloseState(this.stateContext));
    }

    /**
     * Выполнить защищённый вызов.
     *
     * @param action   основное действие (вызов внешнего сервиса)
     * @param fallback запасное действие (если цепь разомкнута или ошибка)
     * @param now      текущий момент времени
     * @return результат выполнения
     */
    public <T> CircuitBreakerResult<T> execute(Supplier<T> action,
                                               Supplier<T> fallback,
                                               Instant now) {
        return stateContext.execute(action, fallback, now);
    }

    /**
     * Получить текущее состояние.
     */
    public CircuitBreakerState getState() {
        // место для кода
        return stateContext.getState().name();
    }

    /**
     * Получить метрики.
     */
    public CircuitBreakerMetrics getMetrics() {
        // место для кода
        return stateContext.getMetrics();
    }

}
