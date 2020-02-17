package com.ss.samples;

import com.google.common.annotations.VisibleForTesting;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CleanerCacheService extends AbstractCacheService {

    private Map<String, StatisticsCacheEntity> cache = new HashMap<>(MAX_CAPACITY);
    private Function<String, StatisticsCacheEntity> sourceFunctionChild = null;
    private static final int MAX_CAPACITY = 100000;
    private static final long TIME_TO_LIVE = 5 * 60;
    private static final long NUMBER_OF_TIMES_USED = 5;

    @Override
    protected void _put(String key, Object value) {
        if (isFull()) {
            clean();
        }
        cache.put(key, new StatisticsCacheEntity(value));
    }

    @Override
    public Object get(String key) {
        if (!cache.containsKey(key)) {
            StatisticsCacheEntity value = getRealValue(key);
            if (Objects.isNull(value)) {
                throw new RuntimeException("Value was not found!");
            }
            _put(key, value);
        }
        cache.get(key).getStatistics().updateNumberOfUses();
        cache.get(key).getStatistics().updateTime();
        return cache.get(key).getValue();
    }

    protected StatisticsCacheEntity getRealValue(String key) {
        return sourceFunctionChild != null ? sourceFunctionChild.apply(key) : null;
    }

    private void clean() {
        cache = cache.entrySet().stream()
            .filter(entity -> entity.getValue().getStatistics().getLastAccessTime()
                .isBefore(Instant.now().minusSeconds(TIME_TO_LIVE)))
            .filter(entity -> entity.getValue().getStatistics().getNumberOfUses() >
                NUMBER_OF_TIMES_USED)
            .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    @VisibleForTesting
    boolean isFull() {
        return cache.size() == MAX_CAPACITY;
    }

    @Getter
    @Setter
    static class StatisticsCacheEntity extends AbstractCachedEntity {

        private Statistics statistics;

        public StatisticsCacheEntity(Object value) {
            super(value);
            this.statistics = new Statistics();
        }
    }
}
