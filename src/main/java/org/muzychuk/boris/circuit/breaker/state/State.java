package org.muzychuk.boris.circuit.breaker.state;

import org.muzychuk.boris.circuit.breaker.domain.CircuitBreakerResult;
import org.muzychuk.boris.circuit.breaker.domain.CircuitBreakerState;

import java.time.Instant;
import java.util.function.Supplier;

public interface State {

    <T> CircuitBreakerResult<T> execute(Supplier<T> action, Supplier<T> fallback, Instant now);

    void open();

    void close();

    void halfOpen();

    CircuitBreakerState name();

}
