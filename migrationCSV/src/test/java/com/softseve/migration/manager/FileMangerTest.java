package com.softseve.migration.manager;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.softseve.migration.watcher.FileType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class FileMangerTest {

    private final FileManger fileManger = new FileManger();
    private final Path pathOfDirectory = Paths.get("test");
    private final Path pathOfErrorDirectory = Paths.get("test/ERROR");
    private final Path pathOfDoneDirectory = Paths.get("test/DONE");
    private final Path pathOfFile = Paths.get("test/test.txt");
    private final Path pathOfErrorFile = Paths.get("test/ERROR/test.txt");
    private final Path pathOfDoneFile = Paths.get("test/DONE/test.txt");
    private final Path pathOfLogFile = Paths.get("test/test.err");

    @BeforeEach
    public void init() throws IOException {

        Files.createDirectory(pathOfDirectory);
        Files.createFile(pathOfFile);
    }

    @AfterEach
    public void destroy() throws IOException {

        Files.deleteIfExists(pathOfErrorFile);
        Files.deleteIfExists(pathOfDoneFile);
        Files.deleteIfExists(pathOfFile);
        Files.deleteIfExists(pathOfLogFile);
        Files.deleteIfExists(pathOfErrorDirectory);
        Files.deleteIfExists(pathOfDoneDirectory);
        Files.deleteIfExists(pathOfDirectory);
    }

    @Test
    public void moveFileHasError() throws IOException {

        fileManger.moveFile(true, pathOfFile);

        assertTrue(Files.exists(
            Paths.get(pathOfDirectory.toString(),
                "ERROR/", pathOfFile.getFileName().toString())));
    }

    @Test
    public void moveFileHasNotError() throws IOException {

        fileManger.moveFile(false, pathOfFile);

        assertTrue(Files.exists(
            Paths.get(pathOfDirectory.toString(),
                "DONE/", pathOfFile.getFileName().toString())));
    }

    @Test
    public void changePaths() {

        Path testPath = Paths.get("test/testFile.txt");
        List<Path> paths = new ArrayList<>();
        paths.add(pathOfFile);
        paths.add(testPath);

        fileManger.changePaths(paths, pathOfDoneFile);

        assertTrue(paths.contains(pathOfDoneFile)
            && paths.contains(testPath)
            && !paths.contains(pathOfFile)
            && paths.size() == 2);
    }

    @Test
    public void createLogFileEmptyLogs() throws IOException {

        fileManger.createLogFile(pathOfFile,
            new StringBuilder(""), FileType.TXT);

        Assert.assertTrue(!Files.exists(pathOfLogFile));
    }

    @Test
    public void createLogFileNotExist() throws IOException {

        fileManger.createLogFile(pathOfFile,
            new StringBuilder("logs"), FileType.TXT);

        Assert.assertTrue(Files.exists(pathOfLogFile));
    }

    @Test
    public void createLogFileExist() throws IOException {

        Files.createFile(pathOfLogFile);
        Files.write(pathOfLogFile, "hello".getBytes());

        fileManger.createLogFile(pathOfFile,
            new StringBuilder("logs"), FileType.TXT);

        Assert.assertTrue(Files.exists(pathOfLogFile)
            && Files.readAllLines(pathOfLogFile)
            .containsAll(Arrays.asList("logs")));
    }


}