package org.muzychuk.boris.inmemory.db.transaction;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class InMemoryKeyValueStore implements KeyValueStore {

    private final Map<String, String> keyValueStorage;
    private final Deque<TransactionLayer> transactionLayers;

    public InMemoryKeyValueStore() {
        this.keyValueStorage = new HashMap<>();
        this.transactionLayers = new LinkedList<>();
    }

    @Override
    public void set(String key, String value) {
        if (transactionLayers.isEmpty()) {
            keyValueStorage.put(key, value);
        } else {
            TransactionLayer transaction = transactionLayers.peek();
            transaction.put(key, value);
        }
    }

    @Override
    public String get(String key) {
        return keyValueStorage.get(key);
    }

    @Override
    public boolean delete(String key) {
        return keyValueStorage.remove(key) != null;
    }

    @Override
    public int count(String value) {
        return 0;
    }

    @Override
    public void begin() {
        transactionLayers.push(new TransactionLayer());
    }

    @Override
    public void commit() {
        if (transactionLayers.isEmpty()) {
            throw new IllegalStateException("Transaction hasn't been started");
        }
        TransactionLayer currTransaction = transactionLayers.pop();
        if (transactionLayers.isEmpty()) {
            currTransaction.mergeTo(keyValueStorage);
        }
    }

    @Override
    public void rollback() {
        if (!transactionLayers.isEmpty()) {
            transactionLayers.pop();
        }
    }
}
