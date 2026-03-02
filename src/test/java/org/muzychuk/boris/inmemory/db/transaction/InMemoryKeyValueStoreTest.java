package org.muzychuk.boris.inmemory.db.transaction;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryKeyValueStoreTest {

    private final InMemoryKeyValueStore keyValueStore = new InMemoryKeyValueStore();

    @Test
    void create() {
        keyValueStore.set("test key", "test value");
        assertEquals("test value", keyValueStore.get("test key"));
    }

    @Test
    void replace() {
        keyValueStore.set("key", "value 1");
        keyValueStore.set("key", "value 2");
        assertEquals("value 2", keyValueStore.get("key"));
    }

}