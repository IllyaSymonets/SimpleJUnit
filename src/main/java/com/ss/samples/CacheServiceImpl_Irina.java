package com.ss.samples;

import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class CacheServiceImpl_Irina extends CacheServiceImpl implements GarbageCollector {

    private Map<String, CachedEntity> cacheEntity = new TreeMap<>();

    public Map<String, CachedEntity> getCacheEntity() {
        return cacheEntity;
    }

    private int mapCapacity = 100000;
    private double minFrequencyOfUse = 0.0001;

    @Override
    public void collectGarbageByFrequency(Map<String, CachedEntity> cacheEntity) {
        this.cacheEntity = cacheEntity.
                entrySet().
                stream().
                filter(entity -> (entity.getValue().getStatisticInfo().getFrequencyOfTouch()) <= minFrequencyOfUse)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void collectGarbageByLeastUsedEntity(Map<String, CachedEntity> cacheEntity) {
        this.cacheEntity = cacheEntity
                .entrySet()
                .stream()
                .sorted(Comparator.comparingDouble(entry -> entry.getValue().getStatisticInfo().getFrequencyOfTouch()))
                .limit((int) (0.9 * mapCapacity))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) ->
                        newValue, TreeMap::new));
    }

    @Override
    public void _put(String key, AbstractCachedEntity value) {
        if (isFull(cacheEntity)) {
            collectGarbageByLeastUsedEntity(cacheEntity);
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
        return instance.size() >= mapCapacity;
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
