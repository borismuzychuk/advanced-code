package org.muzychuk.boris.inmemory.db.transaction;

public class InMemoryKeyValueStore implements KeyValueStore {

    @Override
    public void set(String key, String value) {

    }

    @Override
    public String get(String key) {
        return "";
    }

    @Override
    public boolean delete(String key) {
        return false;
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
