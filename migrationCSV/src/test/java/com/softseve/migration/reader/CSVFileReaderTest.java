package com.softseve.migration.reader;

import static org.junit.jupiter.api.Assertions.*;

import com.softseve.migration.manager.FileManger;
import com.softseve.migration.model.Patient;
import com.softseve.migration.model.PatientContact;
import com.softseve.migration.model.Source;
import com.softseve.migration.validator.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.xml.bind.ValidationEvent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

class CSVFileReaderTest {

    private static Path pathToPatients = Paths.get("test/patients.csv");
    private static Path pathToContacts = Paths.get("test/contacts.csv");
    List<Path> paths;
    private Validator validatorMock;
    private FileManger fileMangerMock;
    private CSVFileReader csvReader;

    @BeforeAll
    public static void initData() throws IOException {
        String patientData =
            "ID,B_DATE,REF_ID,F0,ACCESS_DATE,,ITEMS,MPI,RF,PATIENT_TYPE_ID,PATIENT_TYPE_TXT,PATIENT_TYPE_REF,F1,F2,F3,C_DATETIME,U_DATETIME\n"
                + "a93719ee-5949-4cc0-b2a8-7a4611a23339,String,String,,String,String,0,0,,String,String,a93719ee-5949-4cc0-b2a8-7a4611a23339,,,,String,String";
        String contactsData =
            "ID,C_DATE,CONTACT_REF,F1,R_DATE,,,,,P_CODE,FIRST_NAME,LAST_NAME,,USER,FACILITY,CNT_REF,CNT_REF_2,CONTACT_TYPE_ID,CONTACT_TYPE,CONTACT_TYPE_IDX,,,,SUM,,C_DATE_TIME,U_DATE_TIME\n"
                + "a93719ee-5949-4cc0-b2a8-7a4611a23339,String,String,,String,String,0,String,String,String,String,String,,String,0000,0,0,String,String,a93719ee-5949-4cc0-b2a8-7a4611a23339,,,,0,0,String,String";
        Files.createDirectory(Paths.get("test"));
        Files.createFile(pathToContacts);
        Files.createFile(pathToPatients);
        Files.write(pathToPatients, patientData.getBytes());
        Files.write(pathToContacts, contactsData.getBytes());
    }

    @BeforeEach
    public void initObjects() {
        validatorMock = Mockito.mock(Validator.class);
        fileMangerMock = Mockito.mock(FileManger.class);
        csvReader = new CSVFileReader(validatorMock, fileMangerMock);
        paths = new ArrayList<>();
        paths.add(pathToContacts);
        paths.add(pathToPatients);
    }

    @Test
    void readPatients() throws IOException {

        Mockito.when(validatorMock.isValidLong(Mockito.anyString(),Mockito.anyLong(),Mockito.anyString())).thenReturn("0");
        Mockito.when(validatorMock.isValidUUID(Mockito.anyString(),Mockito.anyLong(),Mockito.anyString())).thenReturn("a93719ee-5949-4cc0-b2a8-7a4611a23339");
        Mockito.when(fileMangerMock.moveFile(Mockito.anyBoolean(), Mockito.any())).thenReturn(pathToPatients);
        Mockito.when(fileMangerMock.changePaths(Mockito.any(), Mockito.any())).thenReturn(paths);

        Map<UUID, Patient> patients = csvReader.readPatients(paths);

        Patient patient = patients.get(UUID.fromString("a93719ee-5949-4cc0-b2a8-7a4611a23339"));

        assertTrue(patients.containsKey(
            UUID.fromString("a93719ee-5949-4cc0-b2a8-7a4611a23339"))
            && patient.getId()
            .equals(UUID.fromString("a93719ee-5949-4cc0-b2a8-7a4611a23339"))
            && patient.getBDate().equals("String")
            && patient.getBDate().equals("String")
            && patient.getRefId().equals("String")
            && patient.getAccessDate().equals("String")
            && patient.getItems() == 0
            && patient.getMPI() == 0
            && patient.getPatientTypeId().equals("String")
            && patient.getPatientTypeTxt().equals("String")
            && patient.getPatientTypeRef().equals("a93719ee-5949-4cc0-b2a8-7a4611a23339")
            && patient.getCPatientDateTime().equals("String")
            && patient.getUPatientDateTime().equals("String")
            && patient.getPatientSrc().getFileName().equals("patients.csv")
            && patient.getPatientSrc().getLineNumber() == 1
        );
    }

    @Test
    void readPatientsContacts() throws IOException {

        Mockito.when(validatorMock.isValidLong(Mockito.anyString(),Mockito.anyLong(),Mockito.anyString())).thenReturn("0");
        Mockito.when(validatorMock.isValidUUID(Mockito.anyString(),Mockito.anyLong(),Mockito.anyString())).thenReturn("a93719ee-5949-4cc0-b2a8-7a4611a23339");
        Mockito.when(fileMangerMock.moveFile(Mockito.anyBoolean(), Mockito.any())).thenReturn(pathToPatients);
        Mockito.when(fileMangerMock.changePaths(Mockito.any(), Mockito.any())).thenReturn(paths);

        Map<UUID, PatientContact> contacts = csvReader.readPatientsContacts(paths);
        PatientContact contact = contacts.get(UUID.fromString("a93719ee-5949-4cc0-b2a8-7a4611a23339"));

        System.out.println(contact.toString());
        assertTrue(contacts.containsKey(
            UUID.fromString("a93719ee-5949-4cc0-b2a8-7a4611a23339"))
            && contact.getId()
            .equals(UUID.fromString("a93719ee-5949-4cc0-b2a8-7a4611a23339"))
            && contact.getCDate().equals("String")
            && contact.getContactRef().equals("String")
            && contact.getPCode().equals("String")
            && contact.getFirstName().equals("String")
            && contact.getLastName().equals("String")
            && contact.getUser().equals("String")
            && contact.getFacility() == 0
            && contact.getCntRef() == 0
            && contact.getCntRef2() == 0
            && contact.getContactTypeId().equals("String")
            && contact.getContactType().equals("String")
            && contact.getContactTypeIdx().equals(UUID.fromString("a93719ee-5949-4cc0-b2a8-7a4611a23339"))
            && contact.getSums() == 0
            && contact.getCContactDateTime().equals("String")
            && contact.getUContactDateTime().equals("String")
            && contact.getContactSrc().getFileName().equals("contacts.csv")
            && contact.getContactSrc().getLineNumber() == 1
        );
    }

    @AfterAll
    public static void destroy() throws IOException {
        Files.deleteIfExists(pathToContacts);
        Files.deleteIfExists(pathToPatients);
        Files.deleteIfExists(Paths.get("test"));
    }
}