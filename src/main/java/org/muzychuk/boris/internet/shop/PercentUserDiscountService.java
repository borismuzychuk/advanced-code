package org.muzychuk.boris.internet.shop;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class PercentUserDiscountService implements UserDiscountService {

    public final BigDecimal percentDiscount;

    public PercentUserDiscountService(BigDecimal percentDiscount) {
        this.percentDiscount = percentDiscount;
    }

    @Override
    public List<Purchase> calculationDiscount(User user) {
        return user.getPurchases().stream().map(purchase ->
                new Purchase(purchase.productId(), purchase.price(),
                        purchase.price().subtract(purchase.price().multiply(percentDiscount))
                                .setScale(2, RoundingMode.HALF_UP))
        ).toList();
    }
}
