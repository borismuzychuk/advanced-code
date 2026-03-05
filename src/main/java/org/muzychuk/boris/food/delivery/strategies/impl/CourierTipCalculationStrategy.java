package org.muzychuk.boris.food.delivery.strategies.impl;

import org.muzychuk.boris.food.delivery.domain.Delivery;
import org.muzychuk.boris.food.delivery.domain.Partner;
import org.muzychuk.boris.food.delivery.strategies.TipCalculationStrategy;

import java.math.BigDecimal;
import java.util.Map;

public class CourierTipCalculationStrategy implements TipCalculationStrategy {

    @Override
    public void calculateTip(long tipAmount, Delivery delivery, Map<Partner, BigDecimal> result) {
        BigDecimal resultTip;
        // можно упростить
        if (delivery.isHardDelivery()) {
            if (delivery.restaurantId() != null) {
                resultTip = new BigDecimal("0.9").multiply(new BigDecimal(tipAmount));
            } else {
                resultTip = new BigDecimal(tipAmount);
            }
        } else {
            if (delivery.restaurantId() != null) {
                resultTip = new BigDecimal("0.85").multiply(new BigDecimal(tipAmount));
            } else {
                resultTip = new BigDecimal("0.95").multiply(new BigDecimal(tipAmount));
            }
        }
        result.put(Partner.COURIER, resultTip);
    }
}
