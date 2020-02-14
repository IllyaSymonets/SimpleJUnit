package com.ss.samples;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class CacheServiceImpl {

    private Map<String, AbstractCachedEntity> instance = new HashMap<>();
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
        _put(key, value);
    }

    public Object get(String key) {
        if (!instance.containsKey(key)) {
            Object value = getRealValue(key);
            if (Objects.isNull(value)) {
                throw new RuntimeException("Value was not found!");
            }
            put(key, value);
        }
        return instance.get(key).getValue();
    }

    protected Object getRealValue(String key) {
        return sourceFunction != null ? sourceFunction.apply(key) : null;
    }

    protected void handleKeyExists(String key) {
        if (Objects.nonNull(handler)) {
            handler.accept(instance.get(key));
        }
    }

    protected void _put(String key, Object value) {
        instance.put(key, new AbstractCachedEntity(value));
    }

    class AbstractCachedEntity {

        Object value;

        AbstractCachedEntity(Object value) {
            super();
            this.value = value;
        }

        public Object getValue() {
            return value;
        }
    }
}
