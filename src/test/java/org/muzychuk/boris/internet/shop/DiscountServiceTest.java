package org.muzychuk.boris.internet.shop;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DiscountServiceTest {

    @Test
    void whenPercentDiscount() {
        DiscountService discountService = new DiscountService(new PercentUserDiscountService(new BigDecimal("0.10")));
        List<Purchase> price = discountService.applyDiscount(new User(42L, List.of(
                new Purchase(1L, new BigDecimal("33.34"), new BigDecimal("0")),
                new Purchase(2L, new BigDecimal("33.33"), new BigDecimal("0")),
                new Purchase(3L, new BigDecimal("33.33"), new BigDecimal("0"))
        )));

        List<Purchase> expected = discountService.applyDiscount(new User(42L, List.of(
                new Purchase(1L, new BigDecimal("33.34"), new BigDecimal("30.01")),
                new Purchase(2L, new BigDecimal("33.33"), new BigDecimal("30.00")),
                new Purchase(3L, new BigDecimal("33.33"), new BigDecimal("30.00"))
        )));
        assertEquals(expected, price);
    }

    @Test
    void whenFixedDiscount() {
        DiscountService discountService = new DiscountService(new FixedUserDiscountService(new BigDecimal("10")));
        List<Purchase> price = discountService.applyDiscount(new User(42L, List.of(
                new Purchase(1L, new BigDecimal("20"), new BigDecimal("0")),
                new Purchase(2L, new BigDecimal("30"), new BigDecimal("0"))
        )));

        List<Purchase> expected = discountService.applyDiscount(new User(42L, List.of(
                new Purchase(1L, new BigDecimal("20"), new BigDecimal("16")),
                new Purchase(2L, new BigDecimal("30"), new BigDecimal("24"))
        )));
        assertEquals(expected, price);
    }


}