package org.muzychuk.boris.internet.shop;

import java.util.List;

public class DiscountService {

    private final UserDiscountService discountService;

    public DiscountService(UserDiscountService discountService) {
        this.discountService = discountService;
    }

    public List<Purchase> applyDiscount(User user) {
        return discountService.calculationDiscount(user);
    }

}
