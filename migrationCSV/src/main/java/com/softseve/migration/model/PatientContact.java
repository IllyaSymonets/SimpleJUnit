package com.softseve.migration.model;

import java.util.Objects;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PatientContact {

    private UUID id;
    private String cDate;
    private String contactRef;
    private String pCode;
    private String firstName;
    private String lastName;
    private String user;
    private long facility;
    private long cntRef;
    private long cntRef2;
    private String contactTypeId;
    private String contactType;
    private UUID contactTypeIdx;
    private long sums;
    private String cContactDateTime;
    private String uContactDateTime;
    private Source contactSrc;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PatientContact contact = (PatientContact) o;
        return Objects.equals(id, contact.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
