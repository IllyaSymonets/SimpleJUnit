package com.softseve.migration.watcher;

import com.softseve.migration.processor.Processor;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class Watcher {

    private Path directoryPath;
    private WatchService watchService;
    private final Processor processor;
    private List<Path> paths;

    public Watcher(Processor processor) throws IOException {
        this.watchService = FileSystems.getDefault().newWatchService();
        this.processor = processor;
        this.paths = new ArrayList<>();
    }


    public void watch(String path, FileType fileType) throws InterruptedException,
        IOException {
        initDirectoryWatcher(path);
        while (true) {
            WatchKey key;
            while ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    if (event.context().toString()
                        .endsWith(fileType.getFileType())) {
                        paths.add(Paths.get(directoryPath.getParent().toString(),
                            directoryPath.getFileName().toString(),
                            event.context().toString()));
                    }
                }
                key.reset();
                if (paths.size() == 2) {
                    processor.process(paths);
                    paths = new ArrayList<>();
                }
            }
        }
    }

    private void initDirectoryWatcher(String path) throws IOException {

        this.directoryPath = Paths.get(path);
        this.directoryPath.
            register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
    }
}
