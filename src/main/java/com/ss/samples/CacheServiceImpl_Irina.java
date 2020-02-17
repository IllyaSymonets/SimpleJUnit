package com.ss.samples;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class CacheServiceImpl_Irina extends CacheServiceImpl implements GarbageCollector {

    private Map<String, CachedEntity> instance = new HashMap<>();

    public Map<String, CachedEntity> getInstance() {
        return instance;
    }

    @Override
    public void collectGarbageByFrequency(Map<String, CachedEntity> instance) {
        this.instance = instance.entrySet().stream().filter(entity ->
                (entity.getValue().getStatisticInfo().getFrequencyOfTouch())
                        <= Constants.MIN_FREQUENCY_OF_USE).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void collectGarbageByLeastUsedEntity(Map<String, CachedEntity> instance) {
        this.instance = instance.entrySet().stream().limit(90000)
                .sorted(Map.Entry.comparingByValue()).                      //how to compare correct? find the way
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void _put(String key, AbstractCachedEntity value) {
        if (isFull(instance)) {
            collectGarbageByFrequency(instance);
        }
        instance.put(key, new CachedEntity(value));
    }

    @Override
    public Object get(String key) {
        if (!instance.containsKey(key)) {
            Object value = getRealValue(key);
            if (Objects.isNull(value)) {
                throw new RuntimeException("Value was not found!");
            }
            put(key, value);
        }
        CachedEntity cachedEntity = instance.get(key);
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
