package org.muzychuk.boris.inmemory.db.transaction;

public interface KeyValueStore {

    /** Установить значение. Перезаписывает, если ключ уже существует. */
    void set(String key, String value);

    /** Получить значение. null если ключ не найден. */
    String get(String key);

    /** Удалить ключ. Возвращает true если ключ существовал. */
    boolean delete(String key);

    /** Количество ключей, имеющих данное значение. */
    int count(String value);

    /** Начать новую транзакцию. Транзакции могут быть вложенными. */
    void begin();

    /** Применить текущую транзакцию. */
    void commit();

    /** Откатить текущую транзакцию. */
    void rollback();

}
