package com.softseve.migration.validator;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class Validator {

    private static final Logger logger = LoggerFactory.getLogger(Validator.class);
    @Setter
    @Getter
    private StringBuilder errorLogs;

    public String isValidUUID(String uuid, long line, String columnName) {

        try {
            UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            logger.error(String
                .format("Invalid value : %s in %s line in column %s",
                    uuid, line, columnName));
            errorLogs.append(String
                .format("Invalid value : %s in %s line in column %s\n",
                    uuid, line, columnName));
            throw new RuntimeException("Invalid " + uuid + " " + columnName);
        }
        return uuid;
    }

    public String isValidLong(String number, long line, String columnName) {

        try {
            Long.parseLong(number);
        } catch (IllegalArgumentException e) {
            logger.error(String
                .format("Invalid value : %s in %s line in column %s",
                    number, line, columnName));
            errorLogs.append(String
                .format("Invalid value : %s in %s line in column %s\n",
                    number, line, columnName));
            throw new RuntimeException("Invalid " + number + " " + columnName);
        }
        return number;
    }

}
