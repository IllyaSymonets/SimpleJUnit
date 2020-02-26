package com.softseve.migration.reader;

import com.softseve.migration.model.Patient;
import com.softseve.migration.model.PatientContact;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface Reader {

    Map<UUID, Patient> readPatients(List<Path> paths) throws IOException;

    Map<UUID, PatientContact> readPatientsContacts(List<Path> paths) throws IOException;

}
