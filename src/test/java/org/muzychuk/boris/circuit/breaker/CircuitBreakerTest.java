package org.muzychuk.boris.circuit.breaker;

import org.junit.jupiter.api.Test;
import org.muzychuk.boris.circuit.breaker.domain.CircuitBreakerResult;
import org.muzychuk.boris.circuit.breaker.domain.CircuitBreakerState;
import org.muzychuk.boris.circuit.breaker.metrics.CircuitBreakerMetricsHolder;
import org.muzychuk.boris.circuit.breaker.metrics.CircuitBreakerMetricsHolderImpl;
import org.muzychuk.boris.circuit.breaker.state.CircuitBrakerStateContext;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CircuitBreakerTest {

    @Test
    void execute() {
        CircuitBreakerMetricsHolder metricsHolder = new CircuitBreakerMetricsHolderImpl(CircuitBreakerState.CLOSED);
        CircuitBrakerStateContext context = new CircuitBrakerStateContext(metricsHolder);
        CircuitBreaker circuitBreaker = new CircuitBreaker(context);
        RequestSender requestSender = new RequestSender();

        CircuitBreakerResult<Response> result = circuitBreaker.execute(
                requestSender::successRequest,
                () -> new Response("FAILED"),
                Instant.now());

        assertEquals("SUCCESS", result.value().status());
    }
}