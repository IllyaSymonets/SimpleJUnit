package com.ss.samples;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CacheServiceImplChild extends CacheServiceImpl {

    @Getter
    class AbstractCachedEntityChild extends CacheServiceImpl.AbstractCachedEntity {

        private Stats stats = new Stats();

        AbstractCachedEntityChild(Object value) {
            super(value);
        }
    }

    private Map<String, AbstractCachedEntityChild> instance = new HashMap<>();
    private int countOfUses = 10;
    private long secondsToLive = 3;
    private int cacheSize = 10;

    @Override
    protected void _put(String key, Object value) {
        while (isFull()) {
            cleanCache();
        }
        instance.put(key, new AbstractCachedEntityChild(value));
    }

    @Override
    public Object get(String key) {
        instance.get(key).getStats().update();
        return instance.get(key).getValue();
    }

    private void cleanCache() {
        if (isCacheContainsEntityToDeleteByCountOfUses()) {
            filterByCount();
        } else {
            if (isCacheContainsEntityToDeleteByTime()) {
                filterByTime();
            }
        }
    }

    private boolean isCacheContainsEntityToDeleteByCountOfUses() {
        return instance.entrySet().stream()
            .anyMatch(entry ->
                entry.getValue().getStats().getCountOfUses() < countOfUses);
    }

    private boolean isCacheContainsEntityToDeleteByTime() {
        return instance.entrySet().stream()
            .anyMatch(entry ->
                (System.currentTimeMillis() -
                    entry.getValue().getStats().getTimeOfLastAccess()) / 1000.0
                    > secondsToLive);
    }

    private boolean isFull() {
        return instance.size() >= cacheSize;
    }

    private void filterByTime() {
        instance = instance.entrySet().stream().
            filter(cacheEntry -> ((System.currentTimeMillis())
                - cacheEntry.getValue().getStats().getTimeOfLastAccess()) / 1000.0
                < secondsToLive)
            .collect(
                Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    private void filterByCount() {
        instance = instance.entrySet().stream().
            filter(entry ->
                entry.getValue().getStats().getCountOfUses() >= countOfUses)
            .collect(
                Collectors.toMap(Entry::getKey, Entry::getValue));
    }
}
