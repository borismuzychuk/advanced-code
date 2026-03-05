package org.muzychuk.boris.food.delivery.strategies.impl;


import org.muzychuk.boris.food.delivery.domain.Delivery;
import org.muzychuk.boris.food.delivery.domain.Partner;
import org.muzychuk.boris.food.delivery.strategies.TipCalculationStrategy;

import java.math.BigDecimal;
import java.util.Map;

public class PlatformTipCalculationStrategy implements TipCalculationStrategy {

    private static final String PLATFORM_PERCENT = "0.05";

    @Override
    public void calculateTip(long tipAmount, Delivery delivery, Map<Partner, BigDecimal> result) {
        if (delivery.isHardDelivery()) {
            result.put(Partner.PLATFORM, new BigDecimal(0));
        } else {
            result.put(Partner.PLATFORM, new BigDecimal(tipAmount).multiply(new BigDecimal(PLATFORM_PERCENT)));
        }
    }
}
