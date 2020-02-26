package com.softseve.migration.converter;

import com.softseve.migration.model.Patient;
import com.softseve.migration.model.PatientContact;
import com.softseve.migration.model.PatientResult;
import com.softseve.migration.model.Source;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ConverterTest {

    private final UUID uuid = UUID.randomUUID();
    private Converter converter;
    private Map<UUID, Patient> patients;
    private Map<UUID, PatientContact> contacts;
    private Patient patient;
    private PatientContact patientContact;

    @BeforeEach
    public void setUp() {
        converter = new Converter();
        patients = new HashMap<>();
        contacts = new HashMap<>();
        patient = Patient.builder()
            .accessDate("String")
            .bDate("String")
            .cPatientDateTime("String")
            .id(uuid)
            .items(1)
            .MPI(1)
            .patientTypeId("String")
            .patientTypeRef("String")
            .patientTypeTxt("String")
            .refId("String")
            .uPatientDateTime("String")
            .patientSrc(new Source("String", 1))
            .build();

        patientContact = PatientContact.builder()
            .id(uuid)
            .cContactDateTime("String")
            .cDate("String")
            .cntRef(1)
            .cntRef2(1)
            .contactRef("String")
            .contactType("String")
            .contactTypeId("String")
            .contactTypeIdx(UUID.randomUUID())
            .facility(1)
            .firstName("String")
            .lastName("String")
            .pCode("String")
            .sums(1)
            .uContactDateTime("String")
            .user("String")
            .contactSrc(new Source("String", 1))
            .build();
    }

    @Test
    public void convert() {
        patients.put(patient.getId(), patient);
        contacts.put(patientContact.getId(), patientContact);

        List<PatientResult> results = converter.convert(patients, contacts);

        PatientResult patientResult = PatientResult.builder()
            .accessDate(patient.getAccessDate())
            .bDate(patient.getBDate())
            .cContactDateTime(patientContact.getCContactDateTime())
            .cDate(patientContact.getCDate())
            .cntRef(patientContact.getCntRef())
            .cntRef2(patientContact.getCntRef2())
            .contactRef(patientContact.getContactRef())
            .contactType(patientContact.getContactType())
            .contactTypeId(patientContact.getContactTypeId())
            .contactTypeIdx(patientContact.getContactTypeIdx())
            .cPatientDateTime(patient.getCPatientDateTime())
            .facility(patientContact.getFacility())
            .firstName(patientContact.getFirstName())
            .lastName(patientContact.getLastName())
            .id(patient.getId())
            .items(patient.getItems())
            .MPI(patient.getMPI())
            .patientTypeId(patient.getPatientTypeId())
            .patientTypeRef(patient.getPatientTypeRef())
            .patientTypeTxt(patient.getPatientTypeTxt())
            .pCode(patientContact.getPCode())
            .refId(patient.getRefId())
            .sums(patientContact.getSums())
            .uContactDateTime(patientContact.getUContactDateTime())
            .uPatientDateTime(patient.getUPatientDateTime())
            .user(patientContact.getUser())
            .contactSrc(patientContact.getContactSrc())
            .patientSrc(patient.getPatientSrc())
            .build();

        Assertions.assertEquals(results.get(0), patientResult);
    }
}