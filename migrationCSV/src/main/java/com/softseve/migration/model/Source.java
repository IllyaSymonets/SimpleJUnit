package com.softseve.migration.model;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Source {

    private int lineId;
    private final String fileName;
    private final long lineNumber;
    private LocalDateTime fileDateTime;
    private UUID clientId;
}
