package org.muzychuk.boris.inmemory.db.transaction;

import java.util.HashMap;
import java.util.Map;

public class InMemoryKeyValueStore implements KeyValueStore {

    private final Map<String, String> keyValueStorage;

    public InMemoryKeyValueStore() {
        this.keyValueStorage = new HashMap<>();
    }

    @Override
    public void set(String key, String value) {
        keyValueStorage.put(key, value);
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

    }

    @Override
    public void commit() {

    }

    @Override
    public void rollback() {

    }
}
