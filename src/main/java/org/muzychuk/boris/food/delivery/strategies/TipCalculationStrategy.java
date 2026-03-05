package org.muzychuk.boris.food.delivery.strategies;


import org.muzychuk.boris.food.delivery.domain.Delivery;
import org.muzychuk.boris.food.delivery.domain.Partner;

import java.math.BigDecimal;
import java.util.Map;

public interface TipCalculationStrategy {

    void calculateTip(long tipAmount, Delivery delivery, Map<Partner, BigDecimal> result);

}
