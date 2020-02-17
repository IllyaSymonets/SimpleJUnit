package com.ss.samples;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
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
    Predicate<Entry<String, AbstractCachedEntityChild>> isContainsEntityToDeleteByCountPredicate =
        entry ->
            entry.getValue().getStats().getCountOfUses() < countOfUses;
    Predicate<Entry<String, AbstractCachedEntityChild>> isContainsEntityToDeleteByTimePredicate =
        entry ->
            (System.currentTimeMillis() - entry.getValue().getStats()
                .getTimeOfLastAccess()) / 1000.0
                > secondsToLive;

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
            .anyMatch(isContainsEntityToDeleteByCountPredicate);
    }

    private boolean isCacheContainsEntityToDeleteByTime() {
        return instance.entrySet().stream()
            .anyMatch(isContainsEntityToDeleteByTimePredicate);
    }

    private boolean isFull() {
        return instance.size() >= cacheSize;
    }

    private void filterByTime() {
        instance = instance.entrySet().stream().
            filter(isContainsEntityToDeleteByTimePredicate.negate())
            .collect(
                Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    private void filterByCount() {
        instance = instance.entrySet().stream().
            filter(isContainsEntityToDeleteByCountPredicate.negate())
            .collect(
                Collectors.toMap(Entry::getKey, Entry::getValue));
    }
}
