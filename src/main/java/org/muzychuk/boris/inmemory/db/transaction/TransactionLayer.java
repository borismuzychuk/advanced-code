package org.muzychuk.boris.inmemory.db.transaction;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TransactionLayer {

    // Хранит изменения текущей транзакции
    // key → Optional<String> (Optional.empty() = DELETE)
    private final Map<String, Optional<String>> changes;

    TransactionLayer() {
        this.changes = new HashMap<>();
    }

    public void put(String key, String value) {
        changes.put(key, Optional.ofNullable(value));
    }
}
