package com.andrius.hills.preprocesing;

import com.andrius.hills.Base;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;

public class FileManager implements Base {

    private File directory;

    private FileManager(String directory) {
        this.directory = new File(directory);

        if (!this.directory.exists()) {
            var resultCode = this.directory.mkdirs();
            logger.info("Initializing cache, creating directory {}, result: {}", this.directory, resultCode);
        } else {
            logger.info("Initializing cache, reusing directory {}", this.directory);
        }
    }

    public File tempFile() {
        try {
            var tempFile = File.createTempFile("temp", "tmp");
            tempFile.deleteOnExit();
            return tempFile;
        } catch (IOException e) {
            logger.error("unable to create a temp file", e);
            throw new RuntimeException(e);
        }
    }

    public Optional<File> produce(String name, Consumer<File> producer) {
        var file = new File(directory + File.separator + name);
        if (!file.exists()) {
            logger.info("Content for {} is is missing, recalculating", file);
            producer.accept(file);
            if (file.exists()) {
                logger.info("Content for {} produced, size is {}MB", file, sizeMb(file));
            } else {
                logger.info("No content for {} produced", file);
                return Optional.empty();
            }
        } else {
            logger.info("Content for {} is already available, returning cached.", file);
        }

        return Optional.of(file);
    }

    private long sizeMb(File file) {
        return file.length() / (1024 * 1024);
    }

    public static FileManager forDirectory(String directory) {
        return new FileManager(directory);
    }
}
