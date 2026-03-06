package org.muzychuk.boris.circuit.breaker.config;

import java.time.Duration;

public record CircuitBreakerConfig(
        int windowSize,            // Размер окна (количество вызовов)
        int failureThreshold,      // Порог ошибок в процентах (50 = 50%)
        Duration waitDuration,     // Ожидание перед HALF_OPEN
        int successThreshold,      // Успехов подряд для возврата в CLOSED
        int maxHalfOpenCalls       // Макс. одновременных вызовов в HALF_OPEN
) {

}
