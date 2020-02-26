package com.softseve.migration.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ValidatorTest {

    private Validator validator;

    @BeforeEach
    public void init() {

        validator = new Validator();
        validator.setErrorLogs(new StringBuilder());
    }

    @Test
    public void isValidUUIDThrowsRuntimeException() {

        assertThrows(RuntimeException.class, () ->
            validator.isValidUUID("String",
                1, "column"));
    }

    @Test
    public void isValidUUIDGHappyPath() {

        UUID testUUID = UUID.randomUUID();

        String result = validator.isValidUUID(testUUID.toString(),
            1, "column");

        assertEquals(testUUID, UUID.fromString(result));
    }

    @Test
    public void isValidUUIDLogTest() {

        try {
            validator.isValidUUID("String",
                1, "column");
        } catch (RuntimeException e) {
            assertEquals("Invalid value : String in 1 line in column column\n",
                validator.getErrorLogs().toString());
        }
    }

    @Test
    public void isValidLongThrowsRuntimeException() {

        assertThrows(RuntimeException.class, () ->
            validator.isValidLong("String",
                1, "column"));
    }

    @Test
    public void isValidLongGHappyPath() {

        Long testLong = 1L;

        String result = validator.isValidLong(testLong.toString(),
            1, "column");

        assertEquals(testLong, Long.valueOf(result));
    }

    @Test
    public void isValidLogLogTest() {

        try {
            validator.isValidLong("String",
                1, "column");
        } catch (RuntimeException e) {
            assertEquals("Invalid value : String in 1 line in column column\n",
                validator.getErrorLogs().toString());
        }
    }
}