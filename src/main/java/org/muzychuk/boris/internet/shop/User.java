package org.muzychuk.boris.internet.shop;

import java.util.List;

public class User {

    private Long userId;
    private List<Purchase> purchases;


    public User(Long userId, List<Purchase> purchases) {
        this.userId = userId;
        this.purchases = purchases;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
    }
}
