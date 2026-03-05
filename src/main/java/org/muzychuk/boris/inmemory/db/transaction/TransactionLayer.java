package org.muzychuk.boris.inmemory.db.transaction;

import java.util.*;

public class TransactionLayer {

    // Хранит изменения текущей транзакции
    // key → Optional<String> (Optional.empty() = DELETE)
    private final Map<String, Optional<String>> changes;
    private final Map<String, Integer> valueStatistic;

    TransactionLayer() {
        this.changes = new HashMap<>();
        this.valueStatistic = new HashMap<>();
    }

    public void put(String key, String value) {
        changes.put(key, Optional.ofNullable(value));
        valueStatistic.merge(value, 1, Integer::sum);
    }

    public void mergeTo(Map<String, String> changes) {
        for (String key : this.changes.keySet()) {
            changes.merge(key, this.changes.get(key).get(),
                    (old, value) -> value);
        }
        collectStatistics(changes);
    }

    // TODO сделать один метод mergeTo(Map<String, String> changes)
    public void mergeTo(TransactionLayer transaction) {
        for (String key : transaction.changes.keySet()) {
            transaction.changes.merge(key, changes.get(key),
                    (old, value) -> value);
        }
        collectTransactionStatistics(transaction.changes);
    }

    public String get(String key) {
        return changes.get(key).orElseGet(null);
    }

    public boolean delete(String key) {
        Optional<String> removed = changes.remove(key);
        changes.put(key, Optional.empty());
        removed.ifPresent(value ->
                valueStatistic.put(value, valueStatistic.get(value) - 1));
        return true;
    }

    public Integer count(String value) {
        Integer count = valueStatistic.get(value);
        return count == null ? 0 : count;
    }

    public void mergeWith(Map<String, String> changes) {
        for (Map.Entry<String, String> keyValue : changes.entrySet()) {
            this.changes.merge(keyValue.getKey(), Optional.ofNullable(keyValue.getValue()),
                    (old, value) -> value);
        }
        collectStatistics(changes);
    }

    private void collectStatistics(Map<String, String> changes) {
        for (Map.Entry<String, String> keyValue : changes.entrySet()) {
            String value = keyValue.getValue();
            valueStatistic.merge(value, 1, Integer::sum);
        }
    }

    // todo сделать один метод collectStatistics(Map<String, String> changes)
    private void collectTransactionStatistics(Map<String, Optional<String>> changes) {
        Set<String> values = new HashSet<>();
        for (Map.Entry<String, Optional<String>> keyValue : changes.entrySet()) {
            String value = keyValue.getValue().orElse(null);
            if (value != null && values.contains(value)) {
                valueStatistic.put(value, valueStatistic.get(value) + 1);
            }
            if (value != null) {
                values.add(value);
            }
        }
    }
}
