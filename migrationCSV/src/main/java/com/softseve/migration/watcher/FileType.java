package com.softseve.migration.watcher;

import lombok.Getter;

@Getter
public enum FileType {

    CSV(".csv");

    private String fileType;

    FileType(String fileType) {
        this.fileType = fileType;
    }
}
