package com.ss.samples;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class CacheServiceImplTest_Irina {

    CacheServiceImpl cacheService;

    @Before
    public void before() {
        cacheService = new CacheServiceImpl();
        cacheService.instance = new HashMap<>();
    }

    @After
    public void after() {
        cacheService.instance = new HashMap<>();
    }

    @Test
    public void get() {
        User expectedObject = new User(100500, "User");
        cacheService.instance.put(String.valueOf(expectedObject.getId()), expectedObject);

        assertEquals(expectedObject, cacheService.get(String.valueOf(expectedObject.getId())));
    }

    @Test(expected = Exception.class)
    public void getIfNotExistsInRealValue() throws Exception {
        User user = new User(100500, "User");
        Function<String, Object> sourceFunction = s -> null;
        setValueToPrivateField(cacheService, "sourceFunction", sourceFunction);

        cacheService.get(String.valueOf(user.getId()));
    }

    @Test
    public void getIfNotExistsButExistsInRealValue() throws Exception {
        User expectedObject = new User(100500, "User");
        Function<String, Object> sourceFunction = s -> expectedObject;

        setValueToPrivateField(cacheService, "sourceFunction", sourceFunction);

        assertEquals(expectedObject, cacheService.get(String.valueOf(expectedObject.getId())));
    }

    @Test
    public void put() throws Exception {
        Consumer<String> handlerToMock = System.out::println;
        setValueToPrivateField(cacheService, "handler", handlerToMock);

        User user = new User(100500, "User");
        cacheService.put(String.valueOf(user.getId()), user);
        assertEquals(user, cacheService.instance.get(String.valueOf(user.getId())));
    }

    @Test
    public void putIfExists() throws Exception {
        Consumer<String> handlerToMock = System.out::println;
        setValueToPrivateField(cacheService, "handler", handlerToMock);

        User user = new User(100500, "User");
        User userTwo = new User(100500, "User Two with the same id");

        cacheService.put(String.valueOf(user.getId()), user);
        cacheService.put(String.valueOf(userTwo.getId()), userTwo);
        User actualUser = (User) cacheService.instance.get(String.valueOf(user.getId()));
        assertEquals(userTwo.getName(), actualUser.getName());
    }

    private void setValueToPrivateField(
            Object targetObject,
            String fieldName,
            Object value
    ) throws NoSuchFieldException, IllegalAccessException {
        Field sourceFunctionField = CacheServiceImpl.class.getDeclaredField(fieldName);
        sourceFunctionField.setAccessible(true);
        sourceFunctionField.set(targetObject, value);
    }

    public static class User {
        private long id;
        private String name;

        public User(long id, String name) {
            this.id = id;
            this.name = name;
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof User)) return false;
            User user = (User) o;
            if (getId() != user.getId()) return false;
            return getName() != null ? getName().equals(user.getName()) : user.getName() == null;
        }

        @Override
        public int hashCode() {
            int result = (int) (getId() ^ (getId() >>> 32));
            result = 31 * result + (getName() != null ? getName().hashCode() : 0);
            return result;
        }
    }
}
