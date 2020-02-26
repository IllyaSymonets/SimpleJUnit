package com.softseve.migration.loader;

import com.softseve.migration.model.PatientResult;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SourceLoader extends AbstractLoader<PatientResult> {

    private final String QUERY_LOAD_TO_DB = "insert into source (FILE_NAME, DATE_TIME, LINE_NUMBER, CLIENT_ID) " +
            "values (?,?,?,?)";

    private final JdbcTemplate jdbcTemplate;
    private static final int DEFAULT_BATCH_SIZE = 50;

    @Autowired
    public SourceLoader(JdbcTemplate jdbcTemplate) {
        super(DEFAULT_BATCH_SIZE);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(rollbackFor = Exception.class)
    public void load(List<PatientResult> data) {

        getDataParts(data, this.getCustomPartSize()).forEach(batch -> {
            jdbcTemplate.batchUpdate(QUERY_LOAD_TO_DB, new BatchPreparedStatementSetter() {

                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    if (Objects.nonNull(batch.get(i).getContactSrc())) {
                        ps.setString(1, batch.get(i).getContactSrc().getFileName());
                        ps.setLong(3, batch.get(i).getContactSrc().getLineNumber());
                    }

                    if (Objects.nonNull(batch.get(i).getPatientSrc())) {
                        ps.setLong(3, batch.get(i).getPatientSrc().getLineNumber());
                        ps.setString(1, batch.get(i).getPatientSrc().getFileName());
                    }

                    ps.setString(2, LocalDateTime.now().toString());
                    ps.setObject(4, batch.get(i).getId());
                }

                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });
        });
    }
}
