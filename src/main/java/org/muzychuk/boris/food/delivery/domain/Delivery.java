package org.muzychuk.boris.food.delivery.domain;

public record Delivery(
        Long orderId,
        Long courierId,
        Long restaurantId,
        Boolean isHardDelivery
) {
}
