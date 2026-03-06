package org.muzychuk.boris.internet.shop;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class FixedUserDiscountService implements UserDiscountService {

    private final BigDecimal fixedDiscount;

    public FixedUserDiscountService(BigDecimal fixedDiscount) {
        this.fixedDiscount = fixedDiscount;
    }

    @Override
    public List<Purchase> calculationDiscount(User user) {
        List<Purchase> result = new ArrayList<>();
        BigDecimal sum = new BigDecimal("0");
        for (Purchase purchase : user.getPurchases()) {
            sum = sum.add(purchase.price());
        }

        for (Purchase purchase : user.getPurchases()) {
            BigDecimal percent = purchase.price().divide(sum);
            result.add(new Purchase(purchase.productId(),
                    purchase.price(),
                    purchase.price().multiply(percent).setScale(2, RoundingMode.HALF_UP)
            ));
        }

        return result;
    }
}
