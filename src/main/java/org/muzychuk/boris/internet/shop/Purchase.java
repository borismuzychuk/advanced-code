package org.muzychuk.boris.internet.shop;

import java.math.BigDecimal;

public record Purchase(
        Long productId,
        BigDecimal price,
        BigDecimal finalPrice
) {

}
