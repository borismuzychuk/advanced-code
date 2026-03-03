package org.muzychuk.boris.circuit.breaker;

import org.muzychuk.boris.circuit.breaker.domain.CircuitBreakerResult;
import org.muzychuk.boris.circuit.breaker.domain.CircuitBreakerState;
import org.muzychuk.boris.circuit.breaker.metrics.CircuitBreakerMetrics;
import org.muzychuk.boris.circuit.breaker.metrics.CircuitBreakerMetricsHolderImpl;
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
        stateContext.changeState(new CloseState(stateContext));
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
        // место для кода
        // вынести

        CircuitBreakerResult<T> result;
        result = stateContext.execute(action, fallback, now);

        // проверить текущий статус
        // если state = CircuitBreakerState.CLOSED, то выполнить запрос
        // и обновить метрики с учетном ответа (SUCCESS или FAILED)
        // после обновления метрик либо оставить текущий статус, либо перевести в другой

        // если статус CircuitBreakerState.OPEN,
        // то проверить waitDuration, если с момента размыкания не прошло waitDuration,
        // то вернуть ResultType.FALLBACK, иначе выполнить запрос
        // и обновить метрики с учетном ответа (SUCCESS или FAILED)
        // после обновления метрик либо оставить текущий статус, либо перевести в другой

        // если статус CircuitBreakerState.HALF_OPEN,
        // то, если currentHalfOpenCalls < maxHalfOpenCalls
        // то выполинть запрос
        // и обновить метрики с учетном ответа (SUCCESS или FAILED)
        // после обновления метрик либо оставить текущий статус, либо перевести в другой
        // иначе вернуть ResultType.FALLBACK

        return result;
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
