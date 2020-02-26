package com.softseve.migration.model;

import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PatientResultTest {

    private final UUID uuid = UUID.randomUUID();
    private PatientResult result1;
    private PatientResult result2;
    private PatientResult result3;

    @BeforeEach
    void setUp() {
        result1 = createResult("String", 1, uuid);

        result2 = createResult("Str", 2, uuid);

        result3 = createResult("String", 1, UUID.randomUUID());
    }

    @Test
    void equalsSuccessTest() {
        Assertions.assertEquals(result1, result2);
    }

    @Test
    void equalsFailTest() {
        Assertions.assertNotEquals(result1, result3);
    }

    @Test
    void hashcodeDifferenceTest() {
        Assertions.assertNotEquals(result1.hashCode(), result3.hashCode());
    }

    private PatientResult createResult(String str, long l, UUID uuid) {
        return PatientResult.builder()
            .id(uuid)
            .accessDate(str)
            .bDate(str)
            .cContactDateTime(str)
            .cDate(str)
            .cntRef(l)
            .cntRef2(l)
            .contactRef(str)
            .contactType(str)
            .contactTypeId(str)
            .contactTypeIdx(uuid)
            .cPatientDateTime(str)
            .facility(l)
            .firstName(str)
            .lastName(str)
            .items(l)
            .MPI(l)
            .patientTypeId(str)
            .patientTypeRef(str)
            .patientTypeTxt(str)
            .pCode(str)
            .refId(str)
            .sums(l)
            .uContactDateTime(str)
            .uPatientDateTime(str)
            .user(str)
            .contactSrc(new Source(str, l))
            .patientSrc(new Source(str, l))
            .build();
    }
}