package com.softseve.migration.reader;

import com.softseve.migration.manager.FileManger;
import com.softseve.migration.model.Patient;
import com.softseve.migration.model.PatientContact;
import com.softseve.migration.model.Source;
import com.softseve.migration.validator.Validator;
import com.softseve.migration.watcher.FileType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CSVFileReader implements Reader {

    private final Validator validator;
    private final FileManger fileManger;

    @Override
    public Map<UUID, Patient> readPatients(List<Path> paths) throws IOException {
        Map<UUID, Patient> patients = new HashMap<>();
        Path patientsPath = getPatientPath(paths);
        boolean hasErrors = false;
        validator.setErrorLogs(new StringBuilder());
        try (
            java.io.Reader reader =
                Files.newBufferedReader(patientsPath);
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                .withAllowDuplicateHeaderNames()
                .withAllowMissingColumnNames()
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase())
        ) {
            for (CSVRecord csvRecord : csvParser) {
                try {
                    Patient patient = Patient.builder()
                        .id(UUID.fromString(validator
                            .isValidUUID(csvRecord.get("ID"),
                                csvRecord.getRecordNumber(),
                                "ID")))
                        .bDate(csvRecord.get("B_DATE"))
                        .refId(csvRecord.get("REF_ID"))
                        .accessDate(csvRecord.get("ACCESS_DATE"))
                        .items(Long.parseLong(validator
                            .isValidLong(csvRecord.get("ITEMS"),
                                csvRecord.getRecordNumber(),
                                "ITEMS")))
                        .MPI(Long.parseLong(validator
                            .isValidLong(csvRecord.get("MPI"),
                                csvRecord.getRecordNumber(),
                                "MPI")))
                        .patientTypeId(csvRecord.get("PATIENT_TYPE_ID"))
                        .patientTypeTxt(csvRecord.get("PATIENT_TYPE_TXT"))
                        .patientTypeRef(validator
                            .isValidUUID(csvRecord
                                    .get("PATIENT_TYPE_REF"),
                                csvRecord.getRecordNumber(),
                                "PATIENT_TYPE_REF"))
                        .cPatientDateTime(csvRecord.get("C_DATETIME"))
                        .uPatientDateTime(csvRecord.get("U_DATETIME"))
                        .patientSrc(new Source(patientsPath
                            .getFileName().toString(),
                            csvRecord.getRecordNumber()))
                        .build();
                    patients.put(patient.getId(), patient);
                } catch (RuntimeException e) {
                    hasErrors = true;
                    continue;
                }
            }
        }
        fileManger.changePaths(paths, fileManger
            .moveFile(hasErrors, patientsPath));
        fileManger.createLogFile(getPatientPath(paths),
            validator.getErrorLogs(), FileType.CSV);
        return patients;
    }

    @Override
    public Map<UUID, PatientContact> readPatientsContacts(List<Path> paths) throws IOException {
        Map<UUID, PatientContact> contacts = new HashMap<>();
        Path contactsPath = getContactsPath(paths);
        boolean hasErrors = false;
        validator.setErrorLogs(new StringBuilder());
        try (
            java.io.Reader reader =
                Files.newBufferedReader(contactsPath);
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                .withAllowDuplicateHeaderNames()
                .withAllowMissingColumnNames()
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase())
        ) {
            for (CSVRecord csvRecord : csvParser) {
                try {
                    PatientContact contact = PatientContact.builder().
                        id(UUID.fromString(validator
                            .isValidUUID(csvRecord.get("ID"),
                                csvRecord.getRecordNumber(),
                                "ID")))
                        .cDate(csvRecord.get("C_DATE"))
                        .contactRef(csvRecord.get("CONTACT_REF"))
                        .pCode(csvRecord.get("P_CODE"))
                        .firstName(csvRecord.get("FIRST_NAME"))
                        .lastName(csvRecord.get("LAST_NAME"))
                        .user(csvRecord.get("USER"))
                        .facility(Long.parseLong(validator
                            .isValidLong(csvRecord.get("FACILITY"),
                                csvRecord.getRecordNumber(),
                                "FACILITY")))
                        .cntRef(Long.parseLong(validator
                            .isValidLong(csvRecord.get("CNT_REF"),
                                csvRecord.getRecordNumber(),
                                "CNT_REF")))
                        .cntRef2(Long.parseLong(validator
                            .isValidLong(csvRecord.get("CNT_REF_2"),
                                csvRecord.getRecordNumber(),
                                "CNT_REF_2")))
                        .contactTypeId(csvRecord.get("CONTACT_TYPE_ID"))
                        .contactType(csvRecord.get("CONTACT_TYPE"))
                        .contactTypeIdx(UUID.fromString(validator
                            .isValidUUID(csvRecord.get("CONTACT_TYPE_IDX"),
                                csvRecord.getRecordNumber(),
                                "CONTACT_TYPE_IDX")))
                        .sums(Long.parseLong(validator
                            .isValidLong(csvRecord.get("SUM"),
                                csvRecord.getRecordNumber(),
                                "SUM")))
                        .cContactDateTime(csvRecord.get("C_DATE_TIME"))
                        .uContactDateTime(csvRecord.get("U_DATE_TIME"))
                        .contactSrc(new Source(contactsPath
                            .getFileName().toString(),
                            csvRecord.getRecordNumber()))
                        .build();
                    contacts.put(contact.getId(), contact);
                } catch (RuntimeException e) {
                    hasErrors = true;
                    continue;
                }
            }
        }
        fileManger.changePaths(paths, fileManger
            .moveFile(hasErrors, contactsPath));
        fileManger.createLogFile(getContactsPath(paths),
            validator.getErrorLogs(), FileType.CSV);
        return contacts;
    }


    private Path getPatientPath(List<Path> paths) throws IOException {
        try (
            java.io.Reader reader = Files.newBufferedReader(paths.get(0));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                .withAllowDuplicateHeaderNames()
                .withAllowMissingColumnNames()
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase())) {
            if (csvParser.getHeaderNames().size() < 20) {
                return paths.get(0);
            } else {
                return paths.get(1);
            }
        }
    }

    private Path getContactsPath(List<Path> paths) throws IOException {
        try (
            java.io.Reader reader = Files.newBufferedReader(paths.get(0));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                .withAllowDuplicateHeaderNames()
                .withAllowMissingColumnNames()
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase())) {
            if (csvParser.getHeaderNames().size() > 20) {
                return paths.get(0);
            } else {
                return paths.get(1);
            }
        }
    }

}
