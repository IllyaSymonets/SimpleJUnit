package com.softseve.migration.loader;

import com.softseve.migration.model.PatientResult;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatientLoader extends AbstractLoader<PatientResult> {

    private final String QUERY_LOAD_TO_DB ="insert into patient_result ("
        + "ID,"
        + " B_DATE,"
        + " REF_ID,"
        + " ACCESS_DATE,"
        + " ITEMS, MPI, "
        + " PATIENT_TYPE_ID,"
        + " PATIENT_TYPE_TXT,"
        + " PATIENT_TYPE_REF,"
        + " C_PATIENT_DATETIME,"
        + " U_PATIENT_DATETIME,"
        + " C_DATE, "
        + " CONTACT_REF,"
        + " P_CODE,"
        + " FIRST_NAME,"
        + " LAST_NAME,"
        + " USER_,"
        + " FACILITY,"
        + " CNT_REF,"
        + " CNT_REF_2,"
        + " CONTACT_TYPE_ID, "
        + "CONTACT_TYPE,"
        + " CONTACT_TYPE_IDX,"
        + " SUMS,"
        + " C_CONTACT_DATE_TIME,"
        + " U_CONTACT_DATE_TIME) "
        + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
        + " ON CONFLICT (ID) DO UPDATE SET "
        + " B_DATE = excluded.B_DATE,"
        + " REF_ID = excluded.REF_ID,"
        + " ACCESS_DATE = excluded.ACCESS_DATE,"
        + " ITEMS = excluded.ITEMS ,"
        + " MPI = excluded.MPI,"
        + " PATIENT_TYPE_ID = excluded.PATIENT_TYPE_ID,"
        + " PATIENT_TYPE_TXT = excluded.PATIENT_TYPE_TXT ,"
        + " PATIENT_TYPE_REF = excluded.PATIENT_TYPE_REF,"
        + " C_PATIENT_DATETIME = excluded.C_PATIENT_DATETIME,"
        + " U_PATIENT_DATETIME = excluded.U_PATIENT_DATETIME,"
        + " C_DATE = excluded.C_DATE,"
        + " CONTACT_REF = excluded.CONTACT_REF,"
        + " P_CODE = excluded.P_CODE,"
        + " FIRST_NAME = excluded.FIRST_NAME ,"
        + " LAST_NAME = excluded.LAST_NAME,"
        + " USER_ = excluded.USER_,"
        + " FACILITY = excluded.FACILITY,"
        + " CNT_REF = excluded.CNT_REF,"
        + " CNT_REF_2 = excluded.CNT_REF_2,"
        + " CONTACT_TYPE_ID = excluded.CONTACT_TYPE_ID,"
        + " CONTACT_TYPE = excluded.CONTACT_TYPE,"
        + " CONTACT_TYPE_IDX = excluded.CONTACT_TYPE_IDX,"
        + " SUMS = excluded.SUMS,"
        + " C_CONTACT_DATE_TIME = excluded.C_CONTACT_DATE_TIME,"
        + " U_CONTACT_DATE_TIME = excluded.U_CONTACT_DATE_TIME";


    private final JdbcTemplate jdbcTemplate;
    private static final int DEFAULT_BATCH_SIZE = 50;

    @Autowired
    public PatientLoader(JdbcTemplate jdbcTemplate) {
        super(DEFAULT_BATCH_SIZE);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(rollbackFor = Exception.class)
    public void load(List<PatientResult> data) {

        getDataParts(data, this.getCustomPartSize()).forEach(
            part -> jdbcTemplate.batchUpdate(QUERY_LOAD_TO_DB, new BatchPreparedStatementSetter() {

                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setObject(1, part.get(i).getId());
                    ps.setString(2, part.get(i).getBDate());
                    ps.setString(3, part.get(i).getRefId());
                    ps.setString(4, part.get(i).getAccessDate());
                    ps.setLong(5, part.get(i).getItems());
                    ps.setLong(6, part.get(i).getMPI());
                    ps.setString(7, part.get(i).getPatientTypeId());
                    ps.setString(8, part.get(i).getPatientTypeTxt());
                    ps.setString(9, part.get(i).getPatientTypeRef());
                    ps.setString(10, part.get(i).getCPatientDateTime());
                    ps.setString(11, part.get(i).getUPatientDateTime());
                    ps.setString(12, part.get(i).getCDate());
                    ps.setString(13, part.get(i).getContactRef());
                    ps.setString(14, part.get(i).getPCode());
                    ps.setString(15, part.get(i).getFirstName());
                    ps.setString(16, part.get(i).getLastName());
                    ps.setString(17, part.get(i).getUser());
                    ps.setLong(18, part.get(i).getFacility());
                    ps.setLong(19, part.get(i).getCntRef());
                    ps.setLong(20, part.get(i).getCntRef2());
                    ps.setString(21, part.get(i).getContactTypeId());
                    ps.setString(22, part.get(i).getContactType());
                    ps.setObject(23, part.get(i).getContactTypeIdx());
                    ps.setLong(24, part.get(i).getSums());
                    ps.setString(25, part.get(i).getCContactDateTime());
                    ps.setString(26, part.get(i).getUContactDateTime());
                }

                @Override
                public int getBatchSize() {
                    return part.size();
                }
            }));
    }
}
