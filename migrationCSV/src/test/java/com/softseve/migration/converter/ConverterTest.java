package com.softseve.migration.converter;

import com.softseve.migration.model.Patient;
import com.softseve.migration.model.PatientContact;
import com.softseve.migration.model.PatientResult;
import com.softseve.migration.model.Source;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ConverterTest {

    private Converter converter;
    private Map<UUID, Patient> patients;
    private Map<UUID, PatientContact> contacts;

    private final UUID uuid1 = UUID.randomUUID();
    private final UUID uuid2 = UUID.randomUUID();
    private Patient patient1;
    private Patient patient2;
    private Patient patient3;
    private PatientContact contact1;
    private PatientContact contact2;
    private PatientContact contact3;

    @BeforeEach
    public void setUp() {
        converter = new Converter();

        patient1 = createPatient("String", 1, uuid1);
        patient2 = createPatient("Str", 2, uuid2);
        patient3 = createPatient("String", 1, UUID.randomUUID());
        contact1 = createContact("String", 1, uuid1);
        contact2 = createContact("Str", 2, uuid2);
        contact3 = createContact("String", 1, UUID.randomUUID());

        patients = new HashMap<UUID, Patient>() {{
            put(patient1.getId(), patient1);
            put(patient2.getId(), patient2);
            put(patient3.getId(), patient3);
        }};

        contacts = new HashMap<UUID, PatientContact>() {{
            put(contact1.getId(), contact1);
            put(contact2.getId(), contact2);
            put(contact3.getId(), contact3);
        }};
    }

    @Test
    public void convert() {
        List<PatientResult> results = converter.convert(patients, contacts);

        List<PatientResult> expectedResults = new ArrayList<PatientResult>() {{
            add(createResult(patient1, contact1));
            add(createResult(patient2, contact2));
            add(addContactToResult(contact3));
            add(addPatientToResult(patient3));
        }};

        results.sort(Comparator.comparingInt(PatientResult::hashCode));
        expectedResults.sort(Comparator.comparingInt(PatientResult::hashCode));

        Assertions.assertEquals(expectedResults, results);
    }

    private PatientResult createResult(Patient patient, PatientContact contact) {
        return PatientResult.builder()
            .id(patient.getId())
            .accessDate(patient.getAccessDate())
            .bDate(patient.getBDate())
            .cContactDateTime(contact.getCContactDateTime())
            .cDate(contact.getCDate())
            .cntRef(contact.getCntRef())
            .cntRef2(contact.getCntRef2())
            .contactRef(contact.getContactRef())
            .contactType(contact.getContactType())
            .contactTypeId(contact.getContactTypeId())
            .contactTypeIdx(contact.getContactTypeIdx())
            .cPatientDateTime(patient.getCPatientDateTime())
            .facility(contact.getFacility())
            .firstName(contact.getFirstName())
            .lastName(contact.getLastName())
            .items(patient.getItems())
            .MPI(patient.getMPI())
            .patientTypeId(patient.getPatientTypeId())
            .patientTypeRef(patient.getPatientTypeRef())
            .patientTypeTxt(patient.getPatientTypeTxt())
            .pCode(contact.getPCode())
            .refId(patient.getRefId())
            .sums(contact.getSums())
            .uContactDateTime(contact.getUContactDateTime())
            .uPatientDateTime(patient.getUPatientDateTime())
            .user(contact.getUser())
            .contactSrc(contact.getContactSrc())
            .patientSrc(patient.getPatientSrc())
            .build();
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

    private PatientResult addContactToResult(PatientContact contact) {
        return PatientResult.builder()
            .id(contact.getId())
            .cContactDateTime(contact.getCContactDateTime())
            .cDate(contact.getCDate())
            .cntRef(contact.getCntRef())
            .cntRef2(contact.getCntRef2())
            .contactRef(contact.getContactRef())
            .contactType(contact.getContactType())
            .contactTypeId(contact.getContactTypeId())
            .contactTypeIdx(contact.getContactTypeIdx())
            .facility(contact.getFacility())
            .firstName(contact.getFirstName())
            .lastName(contact.getLastName())
            .pCode(contact.getPCode())
            .sums(contact.getSums())
            .uContactDateTime(contact.getUContactDateTime())
            .user(contact.getUser())
            .contactSrc(contact.getContactSrc())
            .build();
    }

    private PatientResult addPatientToResult(Patient patient) {
        return PatientResult.builder()
            .id(patient.getId())
            .accessDate(patient.getAccessDate())
            .bDate(patient.getBDate())
            .cPatientDateTime(patient.getCPatientDateTime())
            .id(patient.getId())
            .items(patient.getItems())
            .MPI(patient.getMPI())
            .patientTypeId(patient.getPatientTypeId())
            .patientTypeRef(patient.getPatientTypeRef())
            .patientTypeTxt(patient.getPatientTypeTxt())
            .refId(patient.getRefId())
            .uPatientDateTime(patient.getUPatientDateTime())
            .patientSrc(patient.getPatientSrc())
            .build();
    }
}