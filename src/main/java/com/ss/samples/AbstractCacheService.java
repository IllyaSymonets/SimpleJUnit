package com.ss.samples;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbstractCacheService {

    private Map<String, AbstractCachedEntity> instance = new HashMap<>();
    private Function<String, AbstractCachedEntity> sourceFunction = null;
    private Consumer<Object> handler = null;

    public void put(String key, Object value) {
        if (instance.containsKey(key)) {
            handleKeyExists(key);
        }
        _put(key, new AbstractCachedEntity(value));
    }

    public Object get(String key) {
        if (!instance.containsKey(key)) {
            AbstractCachedEntity value = getRealValue(key);
            if (Objects.isNull(value)) {
                throw new RuntimeException("Value was not found!");
            }
            _put(key, value);
        }
        return instance.get(key).getValue();
    }

    protected AbstractCachedEntity getRealValue(String key) {
        return sourceFunction != null ? sourceFunction.apply(key) : null;
    }

    protected void handleKeyExists(String key) {
        if (Objects.nonNull(handler)) {
            handler.accept(instance.get(key));
        }
    }

    protected void _put(String key, Object value) {
        instance.put(key, (AbstractCachedEntity) value);
    }

    @Getter
    static class AbstractCachedEntity {

        private Object value;

        AbstractCachedEntity(Object value) {
            super();
            this.value = value;
        }
    }
}
