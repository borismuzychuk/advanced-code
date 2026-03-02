package org.muzychuk.boris.food.delivery;


import org.muzychuk.boris.food.delivery.domain.Delivery;
import org.muzychuk.boris.food.delivery.domain.DistributionResult;
import org.muzychuk.boris.food.delivery.domain.Partner;
import org.muzychuk.boris.food.delivery.strategies.TipCalculationStrategy;
import org.muzychuk.boris.food.delivery.strategies.impl.CourierTipCalculationStrategy;
import org.muzychuk.boris.food.delivery.strategies.impl.PlatformTipCalculationStrategy;
import org.muzychuk.boris.food.delivery.strategies.impl.RestaurantTipCalculationStrategy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Сервис распределения чаевых.
 * <p>
 * Правила округления: в копейках, округление вниз для платформы и ресторана,
 * остаток — курьеру (сумма долей должна равняться исходным чаевым).
 */

public class TipDistributionService {

    // Spring инжект
    private final List<TipCalculationStrategy> tipCalculationStrategy;

    //For test
    public TipDistributionService() {
        tipCalculationStrategy = new ArrayList<>();
        tipCalculationStrategy.add(new CourierTipCalculationStrategy());
        tipCalculationStrategy.add(new PlatformTipCalculationStrategy());
        tipCalculationStrategy.add(new RestaurantTipCalculationStrategy());
    }

    /**
     * Распределить чаевые между участниками.
     *
     * @param tipAmount сумма чаевых в копейках
     * @param delivery  информация о доставке
     * @return результат распределения
     */
    public DistributionResult distribute(long tipAmount, Delivery delivery) {
        Map<Partner, BigDecimal> result = new HashMap<>();
        for (TipCalculationStrategy calculationStrategy : tipCalculationStrategy) {
            calculationStrategy.calculateTip(tipAmount, delivery, result);
        }
        return new DistributionResult(result);
    }

}
