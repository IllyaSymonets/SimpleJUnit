package com.ss.samples;

import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

public class CacheServiceImpl_Irina extends CacheServiceImpl implements GarbageCollector {

    private Map<String, CachedEntity> cacheEntity = new TreeMap<>();

    public Map<String, CachedEntity> getCacheEntity() {
        return cacheEntity;
    }

    @Override
    public void collectGarbageByFrequency(Map<String, CachedEntity> cacheEntity) {
        this.cacheEntity = cacheEntity.entrySet().stream().filter(entity ->
                (entity.getValue().getStatisticInfo().getFrequencyOfTouch())
                        <= Constants.MIN_FREQUENCY_OF_USE).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void collectGarbageByLeastUsedEntity(Map<String, CachedEntity> cacheEntity) {
        this.cacheEntity = cacheEntity.entrySet().stream()
                .sorted(Comparator.comparingDouble(entry -> entry.getValue().getStatisticInfo().getFrequencyOfTouch()))
                        .limit(2)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) ->
                        newValue, TreeMap::new))
        ;
    }

    @Override
    public void _put(String key, AbstractCachedEntity value) {
        if (isFull(cacheEntity)) {
            collectGarbageByFrequency(cacheEntity);
        }
        cacheEntity.put(key, new CachedEntity(value));
    }

    @Override
    public Object get(String key) {
        if (!cacheEntity.containsKey(key)) {
            Object value = getRealValue(key);
            if (Objects.isNull(value)) {
                throw new RuntimeException("Value was not found!");
            }
            put(key, value);
        }
        CachedEntity cachedEntity = cacheEntity.get(key);
        cachedEntity.statisticInfo.incrementTheStatistic();
        return cachedEntity.getValue();
    }

    private boolean isFull(Map<String, CachedEntity> instance) {
        return instance.size() >= Constants.MAP_CAPACITY;
    }

    @Setter
    class CachedEntity extends AbstractCachedEntity implements Comparable<CachedEntity> {
        private StatisticInfo statisticInfo;

        public StatisticInfo getStatisticInfo() {
            return statisticInfo;
        }

        public CachedEntity(Object value) {
            super(value);
            this.statisticInfo = new StatisticInfo();
        }

        @Override
        public int compareTo(CachedEntity o) {
            return statisticInfo.compareTo(o.getStatisticInfo());
        }
    }
}
