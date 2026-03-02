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

    @Test
    void delete() {
        keyValueStore.set("key", "value");
        boolean result = keyValueStore.delete("key");
        assertTrue(result);
        assertNull(keyValueStore.get("key"));
    }

    @Test
    void transactionWithRollback() {
        keyValueStore.set("a", "10");
        keyValueStore.begin();
        keyValueStore.set("a", "20");
        keyValueStore.rollback();
        assertEquals("10", keyValueStore.get("a"));
    }

    @Test
    void transactionWithCommit() {
        keyValueStore.set("a", "10");
        keyValueStore.begin();
        keyValueStore.set("a", "20");
        keyValueStore.commit();
        assertEquals("20", keyValueStore.get("a"));
    }

    @Test
    void nestedTransactionWithCommit() {
        keyValueStore.set("a", "10");
        keyValueStore.begin();
        keyValueStore.set("a", "20");
        assertEquals("20", keyValueStore.get("a"));
        keyValueStore.begin();
        keyValueStore.set("a", "30");
        assertEquals("30", keyValueStore.get("a"));
        keyValueStore.commit();
        keyValueStore.commit();
        assertEquals("30", keyValueStore.get("a"));
    }

    @Test
    void nestedTransactionWithRollback() {
        keyValueStore.set("a", "10");
        keyValueStore.begin();
        keyValueStore.set("a", "20");
        assertEquals("20", keyValueStore.get("a"));
        keyValueStore.begin();
        keyValueStore.set("a", "30");
        assertEquals("30", keyValueStore.get("a"));
        keyValueStore.rollback();
        keyValueStore.commit();
        assertEquals("20", keyValueStore.get("a"));
    }

    @Test
    void fullRollback() {
        keyValueStore.set("a", "10");
        keyValueStore.begin();
        keyValueStore.set("a", "20");
        assertEquals("20", keyValueStore.get("a"));
        keyValueStore.begin();
        keyValueStore.set("a", "30");
        assertEquals("30", keyValueStore.get("a"));
        keyValueStore.commit();
        keyValueStore.rollback();
        assertEquals("10", keyValueStore.get("a"));
    }

    @Test
    void whenCommitWithoutStartTransaction_ThenThrowsIllegalStateException() {
        keyValueStore.set("key", "value");
        assertThrows(IllegalStateException.class, keyValueStore::commit);
    }

    @Test
    void count() {
        keyValueStore.set("a","foo");
        keyValueStore.set("b","foo");
        keyValueStore.set("c","bar");
        assertEquals(2, keyValueStore.count("foo"));
        assertEquals(1, keyValueStore.count("bar"));
        assertEquals(0, keyValueStore.count("baz"));

        keyValueStore.begin();
        keyValueStore.delete("a");
        assertEquals(1, keyValueStore.count("foo"));

        keyValueStore.rollback();
        assertEquals(2, keyValueStore.count("foo"));
    }

    @Test
    void countWithoutTransactions() {
        keyValueStore.set("a","foo");
        keyValueStore.set("b","foo");
        keyValueStore.set("c","bar");
        keyValueStore.set("d","test");
        keyValueStore.set("e","test");
        keyValueStore.delete("d");
        assertEquals(2, keyValueStore.count("foo"));
        assertEquals(1, keyValueStore.count("bar"));
        assertEquals(1, keyValueStore.count("test"));
        assertEquals(0, keyValueStore.count("baz"));
    }

}