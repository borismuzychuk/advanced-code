package org.muzychuk.boris.internet.shop;

import java.util.List;

public interface UserDiscountService {

    List<Purchase> calculationDiscount(User user);

}
