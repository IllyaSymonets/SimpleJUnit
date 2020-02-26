package com.softseve.migration.model;

import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PatientTest {

    private final UUID uuid = UUID.randomUUID();
    private Patient patient1;
    private Patient patient2;
    private Patient patient3;

    @BeforeEach
    void setUp() {
        patient1 = createPatient("String", 1, uuid);
        patient2 = createPatient("Str", 2, uuid);
        patient3 = createPatient("String", 1, UUID.randomUUID());
    }

    @Test
    void equalsSuccessTest() {
        Assertions.assertEquals(patient1, patient2);
    }

    @Test
    void equalsFailTest() {
        Assertions.assertNotEquals(patient1, patient3);
    }

    @Test
    void hashCodeDifferenceTest() {
        Assertions.assertNotEquals(patient1.hashCode(), patient3.hashCode());
    }

    private Patient createPatient(String str, long l, UUID uuid) {
        return Patient.builder()
            .id(uuid)
            .accessDate(str)
            .bDate(str)
            .cPatientDateTime(str)
            .items(l)
            .MPI(l)
            .patientTypeId(str)
            .patientTypeRef(str)
            .patientTypeTxt(str)
            .refId(str)
            .uPatientDateTime(str)
            .patientSrc(new Source(str, l))
            .build();
    }
}