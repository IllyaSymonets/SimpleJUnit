package com.softseve.migration.model;

import java.util.Objects;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Patient {

    private UUID id;
    private String bDate;
    private String refId;
    private String accessDate;
    private long items;
    private long MPI;
    private String patientTypeId;
    private String patientTypeTxt;
    private String patientTypeRef;
    private String cPatientDateTime;
    private String uPatientDateTime;
    private Source patientSrc;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Patient patient = (Patient) o;
        return Objects.equals(id, patient.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
