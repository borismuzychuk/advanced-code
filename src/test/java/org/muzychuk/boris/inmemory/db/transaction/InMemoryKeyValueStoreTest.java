package org.muzychuk.boris.inmemory.db.transaction;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryKeyValueStoreTest {

    @Test
    void set() {
        InMemoryKeyValueStore keyValueStore = new InMemoryKeyValueStore();
        keyValueStore.set("test key", "test value");
        assertEquals("test value", keyValueStore.get("test key"));
    }
}