package org.muzychuk.boris.circuit.breaker;

import org.junit.jupiter.api.Test;
import org.muzychuk.boris.circuit.breaker.config.CircuitBreakerConfig;
import org.muzychuk.boris.circuit.breaker.domain.CircuitBreakerResult;
import org.muzychuk.boris.circuit.breaker.domain.CircuitBreakerState;
import org.muzychuk.boris.circuit.breaker.metrics.CircuitBreakerMetrics;
import org.muzychuk.boris.circuit.breaker.metrics.CircuitBreakerMetricsHolder;
import org.muzychuk.boris.circuit.breaker.metrics.CircuitBreakerMetricsHolderImpl;
import org.muzychuk.boris.circuit.breaker.state.CircuitBrakerStateContext;
import org.muzychuk.boris.circuit.breaker.state.impl.CloseState;
import org.muzychuk.boris.circuit.breaker.state.impl.HalfOpenState;
import org.muzychuk.boris.circuit.breaker.state.impl.OpenState;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.muzychuk.boris.circuit.breaker.domain.CircuitBreakerState.*;

class CircuitBreakerTest {

    @Test
    void whenCircuitBreakerClosed_ThenRequestIsSending() {
        CircuitBreakerMetricsHolder metricsHolder = new CircuitBreakerMetricsHolderImpl(new CircuitBreakerMetrics(
                CLOSED,
                0,
                0,
                0,
                0,
                0
        ));
        CircuitBrakerStateContext context = new CircuitBrakerStateContext(metricsHolder,
                new CircuitBreakerConfig(5, 60, Duration.ofSeconds(30), 2, 2));
        context.changeState(new CloseState(context));
        CircuitBreaker circuitBreaker = new CircuitBreaker(context);
        RequestSender requestSender = new RequestSender();

        CircuitBreakerResult<Response> resultS1 = circuitBreaker.execute(
                requestSender::successRequest,
                () -> new Response("FAILED"),
                Instant.now());
        assertEquals("SUCCESS", resultS1.value().status());
        assertEquals(CLOSED, circuitBreaker.getState());
        assertEquals(1, circuitBreaker.getMetrics().totalCalls());
        assertEquals(1, circuitBreaker.getMetrics().successCount());
        assertEquals(0, circuitBreaker.getMetrics().failurePercentage());

        CircuitBreakerResult<Response> resultS2 = circuitBreaker.execute(
                requestSender::successRequest,
                () -> new Response("FAILED"),
                Instant.now());
        assertEquals("SUCCESS", resultS2.value().status());
        assertEquals(CLOSED, circuitBreaker.getState());
        assertEquals(2, circuitBreaker.getMetrics().totalCalls());
        assertEquals(2, circuitBreaker.getMetrics().successCount());
        assertEquals(0, circuitBreaker.getMetrics().failurePercentage());

        CircuitBreakerResult<Response> resultF3 = circuitBreaker.execute(
                requestSender::failRequest,
                () -> new Response("FAILED"),
                Instant.now());
        assertEquals("FAILED", resultF3.value().status());
        assertEquals(CLOSED, circuitBreaker.getState());
        assertEquals(3, circuitBreaker.getMetrics().totalCalls());
        assertEquals(2, circuitBreaker.getMetrics().successCount());
        assertEquals(1, circuitBreaker.getMetrics().failureCount());
        assertEquals(33, circuitBreaker.getMetrics().failurePercentage());

        CircuitBreakerResult<Response> resultS4 = circuitBreaker.execute(
                requestSender::successRequest,
                () -> new Response("FAILED"),
                Instant.now());
        assertEquals("SUCCESS", resultS4.value().status());
        assertEquals(CLOSED, circuitBreaker.getState());
        assertEquals(4, circuitBreaker.getMetrics().totalCalls());
        assertEquals(3, circuitBreaker.getMetrics().successCount());
        assertEquals(1, circuitBreaker.getMetrics().failureCount());
        assertEquals(25, circuitBreaker.getMetrics().failurePercentage());

        CircuitBreakerResult<Response> resultF5 = circuitBreaker.execute(
                requestSender::failRequest,
                () -> new Response("FAILED"),
                Instant.now());
        assertEquals("FAILED", resultF5.value().status());
        assertEquals(CLOSED, circuitBreaker.getState());
        assertEquals(5, circuitBreaker.getMetrics().totalCalls());
        assertEquals(3, circuitBreaker.getMetrics().successCount());
        assertEquals(2, circuitBreaker.getMetrics().failureCount());
        assertEquals(40, circuitBreaker.getMetrics().failurePercentage());
    }

    @Test
    void whenPercentErrorsMoreThenFailureThreshold_ThenCircuitBreakerSwitchStateToOpen() {
        CircuitBreakerMetricsHolder metricsHolder = new CircuitBreakerMetricsHolderImpl(new CircuitBreakerMetrics(
                CLOSED,
                0,
                0,
                0,
                0,
                0
        ));
        CircuitBrakerStateContext context = new CircuitBrakerStateContext(metricsHolder,
                new CircuitBreakerConfig(5, 60, Duration.ofSeconds(30), 2, 2));
        context.changeState(new CloseState(context));
        CircuitBreaker circuitBreaker = new CircuitBreaker(context);
        RequestSender requestSender = new RequestSender();

        CircuitBreakerResult<Response> resultS1 = circuitBreaker.execute(
                requestSender::successRequest,
                () -> new Response("FAILED"),
                Instant.now());

        CircuitBreakerResult<Response> resultS2 = circuitBreaker.execute(
                requestSender::successRequest,
                () -> new Response("FAILED"),
                Instant.now());

        CircuitBreakerResult<Response> resultF1 = circuitBreaker.execute(
                requestSender::failRequest,
                () -> new Response("FAILED"),
                Instant.now());

        CircuitBreakerResult<Response> resultF4 = circuitBreaker.execute(
                requestSender::failRequest,
                () -> new Response("FAILED"),
                Instant.now());

        CircuitBreakerResult<Response> resultF5 = circuitBreaker.execute(
                requestSender::failRequest,
                () -> new Response("FAILED"),
                Instant.now());
        assertEquals("FAILED", resultF5.value().status());
        assertEquals(OPEN, circuitBreaker.getState());
        assertEquals(5, circuitBreaker.getMetrics().totalCalls());
        assertEquals(2, circuitBreaker.getMetrics().successCount());
        assertEquals(3, circuitBreaker.getMetrics().failureCount());
        assertEquals(60, circuitBreaker.getMetrics().failurePercentage());

        CircuitBreakerResult<Response> resultF6 = circuitBreaker.execute(
                requestSender::successRequest,
                () -> new Response("FAILED"),
                Instant.now());
        assertEquals("FAILED", resultF5.value().status());
        assertEquals(OPEN, circuitBreaker.getState());
    }

    @Test
    void whenDurationIsDone_ThenCircuitBreakerSwitchToHalfOpen() throws InterruptedException {
        CircuitBreakerMetricsHolder metricsHolder = new CircuitBreakerMetricsHolderImpl(new CircuitBreakerMetrics(
                OPEN,
                4,
                2,
                2,
                40,
                0
        ));
        Duration waitDuration = Duration.ofSeconds(2);
        CircuitBrakerStateContext context = new CircuitBrakerStateContext(
                metricsHolder,
                new CircuitBreakerConfig(5, 60, waitDuration, 2, 2)
        );
        context.changeState(new OpenState(context));
        CircuitBreaker circuitBreaker = new CircuitBreaker(context);
        RequestSender requestSender = new RequestSender();

        CircuitBreakerResult<Response> result = circuitBreaker.execute(
                requestSender::successRequest,
                () -> new Response("FAILED"),
                Instant.now());
        assertEquals("FAILED", result.value().status());
        assertEquals(OPEN, circuitBreaker.getState());

        Thread.sleep(waitDuration.plusMillis(50));

        result = circuitBreaker.execute(
                requestSender::successRequest,
                () -> new Response("FAILED"),
                Instant.now());
        assertEquals("FAILED", result.value().status());
        assertEquals(HALF_OPEN, circuitBreaker.getState());
    }

    @Test
    void whenCircuitBreakerIsHalfOpenAndHasMoreThenMaxHalOpenCalls_ThenCircuitBreakerSwitchToClose() {
        CircuitBreakerMetricsHolder metricsHolder = new CircuitBreakerMetricsHolderImpl(new CircuitBreakerMetrics(
                HALF_OPEN,
                4,
                2,
                2,
                40,
                0
        ));
        Duration waitDuration = Duration.ofSeconds(30);
        CircuitBrakerStateContext context = new CircuitBrakerStateContext(
                metricsHolder,
                new CircuitBreakerConfig(5, 60, waitDuration, 2, 2)
        );
        context.changeState(new HalfOpenState(context));
        CircuitBreaker circuitBreaker = new CircuitBreaker(context);
        RequestSender requestSender = new RequestSender();

        CircuitBreakerResult<Response> result = circuitBreaker.execute(
                requestSender::successRequest,
                () -> new Response("FAILED"),
                Instant.now());
        assertEquals("SUCCESS", result.value().status());
        assertEquals(HALF_OPEN, circuitBreaker.getState());
        assertEquals(1, circuitBreaker.getMetrics().consecutiveSuccessesInHalfOpen());

        result = circuitBreaker.execute(
                requestSender::successRequest,
                () -> new Response("FAILED"),
                Instant.now());
        assertEquals("SUCCESS", result.value().status());
        assertEquals(HALF_OPEN, circuitBreaker.getState());
        assertEquals(2, circuitBreaker.getMetrics().consecutiveSuccessesInHalfOpen());

        result = circuitBreaker.execute(
                requestSender::successRequest,
                () -> new Response("FAILED"),
                Instant.now());
        assertEquals("SUCCESS", result.value().status());
        assertEquals(CLOSED, circuitBreaker.getState());
    }
}