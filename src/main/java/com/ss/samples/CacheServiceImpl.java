package com.ss.samples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class CacheServiceImpl {

    Map<String, AbstractCachedEntity> instance = new HashMap<>();
    private Function<String, Object> sourceFunction = null;
    private Consumer<AbstractCachedEntity> handler = null;

    public void setSourceFunction(
        Function<String, Object> sourceFunction) {
        this.sourceFunction = sourceFunction;
    }

    public void setHandler(Consumer<AbstractCachedEntity> handler) {
        this.handler = handler;
    }

    public void put(String key, Object value) {
        if (instance.containsKey(key)) {
            handleKeyExists(key);
        }
        _put(key, createEntityInstance(key, value));
    }

    protected final int size() {
        return instance.size();
    }

    protected final void remove(String key) {
        instance.remove(key);
    }

    protected List<AbstractCachedEntity> getActualValues() {
        return new ArrayList<>(instance.values());
    }

    protected AbstractCachedEntity createEntityInstance(String key, Object value) {
        return new AbstractCachedEntity(key, value);
    }

    public Object get(String key) {
        if (!instance.containsKey(key)) {
            Object value = getRealValue(key);
            if (Objects.isNull(value)) {
                throw new RuntimeException("Value was not found!");
            }
            put(key, value);
        }
        AbstractCachedEntity entity = instance.get(key);
        updateStats(entity);
        return entity.getValue();
    }

    protected void updateStats(AbstractCachedEntity entity) {

    }

    protected Object getRealValue(String key) {
        return sourceFunction != null ? sourceFunction.apply(key) : null;
    }

    protected void handleKeyExists(String key) {
        if (Objects.nonNull(handler)) {
            handler.accept(instance.get(key));
        }
    }

    protected void _put(String key, AbstractCachedEntity value) {
        instance.put(key, value);
    }

    class AbstractCachedEntity{

        final String key;
        final Object value;

        AbstractCachedEntity(String key, Object value) {
            super();
            this.key = key;
            this.value = value;
        }

        public String getKey(){
            return key;
        }

        public Object getValue() {
            return value;
        }
    }
}
