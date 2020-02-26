package com.softseve.migration.manager;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import com.softseve.migration.watcher.FileType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class FileManger {

    public Path moveFile(boolean hasErrors, Path path) throws IOException {
        Path errorPath = Paths.get(path.getParent().toString()
            .concat("/ERROR/").concat(path.getFileName().toString()));
        Path donePath = Paths.get(path.getParent().toString()
            .concat("/DONE/").concat(path.getFileName().toString()));
        if (hasErrors) {
            if (!Files.exists(errorPath.getParent())) {
                Files.createDirectory(errorPath.getParent());
            }
            Files.move(path, errorPath, REPLACE_EXISTING);
            return errorPath;
        } else {
            if (!Files.exists(donePath.getParent())) {
                Files.createDirectory(donePath.getParent());
            }
            Files.move(path, donePath, REPLACE_EXISTING);
            return donePath;
        }
    }

    public void changePaths(List<Path> paths, Path newPath) {
        List<Path> newPaths = new ArrayList<>();
        for (Path path : paths) {
            if (path.getFileName().equals(newPath.getFileName())) {
                newPaths.add(newPath);
            } else {
                newPaths.add(path);
            }
        }
        paths.remove(0);
        paths.remove(0);
        paths.add(newPaths.get(0));
        paths.add(newPaths.get(1));
    }

    public void createLogFile(Path pathOfDataFile,
        StringBuilder logs, FileType fileType) throws IOException {
        if (!logs.toString().equals("")) {
            Path pathOfLogFile = Paths.get(pathOfDataFile
                .getParent().toString().concat("/")
                .concat(pathOfDataFile.getFileName().toString()
                    .replace(fileType.getFileType(), ".err")));
            if (Files.exists(pathOfLogFile)) {
                Files.delete(pathOfLogFile);
            }
            Files.createFile(pathOfLogFile);
            Files.write(pathOfLogFile, logs.toString().getBytes());
        }
    }

}
