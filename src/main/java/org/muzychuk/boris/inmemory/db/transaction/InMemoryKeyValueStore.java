package org.muzychuk.boris.inmemory.db.transaction;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class InMemoryKeyValueStore implements KeyValueStore {

    private final Map<String, String> keyValueStorage;
    private final Deque<TransactionLayer> transactionLayers;
    private final Map<String, Integer> valueStatistics;

    public InMemoryKeyValueStore() {
        this.keyValueStorage = new HashMap<>();
        this.transactionLayers = new LinkedList<>();
        this.valueStatistics = new HashMap<>();
    }

    @Override
    public void set(String key, String value) {
        if (transactionLayers.isEmpty()) {
            keyValueStorage.put(key, value);
            valueStatistics.merge(value, 1, Integer::sum);
        } else {
            TransactionLayer transaction = transactionLayers.peek();
            transaction.put(key, value);
        }
    }

    @Override
    public String get(String key) {
        if (transactionLayers.isEmpty()) {
            return keyValueStorage.get(key);
        } else {
            return transactionLayers.getFirst().get(key);
        }
    }

    // TODO сделать транзакционным
    @Override
    public boolean delete(String key) {
        if (transactionLayers.isEmpty()) {
            String removed = keyValueStorage.remove(key);
            boolean removingResult = removed != null;
            if (removingResult) {
                valueStatistics.put(removed, valueStatistics.get(removed) - 1);
            }
            return removingResult;
        } else {
            return transactionLayers.peek().delete(key);
        }
    }

    @Override
    public int count(String value) {
        if (transactionLayers.isEmpty()) {
            Integer count = valueStatistics.get(value);
            return count == null ? 0 : count;
        }
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
        } else {
            currTransaction.mergeTo(transactionLayers.peek());
        }
    }

    @Override
    public void rollback() {
        if (!transactionLayers.isEmpty()) {
            transactionLayers.pop();
        }
    }
}
