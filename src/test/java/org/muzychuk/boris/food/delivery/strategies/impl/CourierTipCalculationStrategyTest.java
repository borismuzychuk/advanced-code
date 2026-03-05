package org.muzychuk.boris.food.delivery.strategies.impl;

import org.junit.jupiter.api.Test;
import org.muzychuk.boris.food.delivery.domain.Delivery;
import org.muzychuk.boris.food.delivery.domain.Partner;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CourierTipCalculationStrategyTest {

    private final CourierTipCalculationStrategy courierTipCalculationStrategy = new CourierTipCalculationStrategy();

    @Test
    void whenDeliveryIsNotHard_ThenReturnStandardTip() {
        Map<Partner, BigDecimal> result = new HashMap<>();
        courierTipCalculationStrategy.calculateTip(100,
                new Delivery(1L, 2L, 3L, false), result);
        assertEquals(new BigDecimal("85.00"), result.get(Partner.COURIER));
    }

    @Test
    void whenDeliveryIsHard_ThenReturnHigherTip() {
        Map<Partner, BigDecimal> result = new HashMap<>();
        courierTipCalculationStrategy.calculateTip(
                100,
                new Delivery(1L, 2L, 3L, true), result);
        assertEquals(new BigDecimal("90.0"), result.get(Partner.COURIER));
    }
}