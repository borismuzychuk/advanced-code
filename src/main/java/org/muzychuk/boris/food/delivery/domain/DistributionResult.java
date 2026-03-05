package org.muzychuk.boris.food.delivery.domain;

import java.math.BigDecimal;
import java.util.Map;

public record DistributionResult(Map<Partner, BigDecimal> result) {

    public BigDecimal total() {
        // todo вычислить сумму всех чайвых
        return null;
    }

}
