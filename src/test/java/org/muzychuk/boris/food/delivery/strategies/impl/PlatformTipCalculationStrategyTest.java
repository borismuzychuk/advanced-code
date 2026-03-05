package org.muzychuk.boris.food.delivery.strategies.impl;

import org.junit.jupiter.api.Test;
import org.muzychuk.boris.food.delivery.domain.Delivery;
import org.muzychuk.boris.food.delivery.domain.Partner;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PlatformTipCalculationStrategyTest {

    private final PlatformTipCalculationStrategy platformTipCalculationStrategy = new PlatformTipCalculationStrategy();

    @Test
    void whenDeliveryIsNotHard_ThenReturnTip() {
        Map<Partner, BigDecimal> result = new HashMap<>();
        platformTipCalculationStrategy.calculateTip(100,
                new Delivery(1L, 2L, 3L, false), result);
        assertEquals(new BigDecimal("5.00"), result.get(Partner.PLATFORM));
    }

    @Test
    void whenDeliveryIsNotHard_ThenReturnZeroTip() {
        Map<Partner, BigDecimal> result = new HashMap<>();
        platformTipCalculationStrategy.calculateTip(100,
                new Delivery(1L, 2L, 3L, true), result);
        assertEquals(new BigDecimal("0"), result.get(Partner.PLATFORM));
    }
}