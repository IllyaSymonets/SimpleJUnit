package com.softseve.migration.model;

import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PatientContactTest {

    private final UUID uuid = UUID.randomUUID();
    private PatientContact contact1;
    private PatientContact contact2;
    private PatientContact contact3;

    @BeforeEach
    void setUp() {
        contact1 = createContact("String", 1, uuid);

        contact2 = createContact("Str", 2, uuid);

        contact3 = createContact("String", 1, UUID.randomUUID());
    }

    @Test
    void equalsSuccessTest() {
        Assertions.assertEquals(contact1, contact2);
    }

    @Test
    void equalsFailTest() {
        Assertions.assertNotEquals(contact1, contact3);
    }

    @Test
    void hashCodeDifferenceTest() {
        Assertions.assertNotEquals(contact1.hashCode(), contact3.hashCode());
    }

    private PatientContact createContact(String str, long l, UUID uuid) {
        return PatientContact.builder()
            .id(uuid)
            .cContactDateTime(str)
            .cDate(str)
            .cntRef(l)
            .cntRef2(l)
            .contactRef(str)
            .contactType(str)
            .contactTypeId(str)
            .contactTypeIdx(uuid)
            .facility(l)
            .firstName(str)
            .lastName(str)
            .pCode(str)
            .sums(l)
            .uContactDateTime(str)
            .user(str)
            .contactSrc(new Source(str, l))
            .build();
    }
}