package com.andrius.hills.preprocesing;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Unzipper {
    private Logger logger = LogManager.getLogger();
    private final String folder;

    public Unzipper(String folder) {
        this.folder = folder;
    }

    public List<File> unzip(List<File> download) {
        File directory = new File(folder);
        if (directory.exists() && directory.listFiles() != null) {
            logger.info("Returning contents of directory {}", folder);
            File[] files = directory.listFiles();
            return Arrays.asList(files);
        }

        var result = directory.mkdirs();
        logger.info("Creating output directory {}, success: {}", folder, result);
        if (!result) {
            return Collections.emptyList();
        }

        download.parallelStream().forEach(this::unzip);

        File[] files = directory.listFiles();
        if (files == null) {
            logger.error("No files found after unzipping.");
            return Collections.emptyList();
        } else {
            return Arrays.asList(files);
        }
    }

    private void unzip(File zip) {
        try {
            logger.info("Unzipping file {}", zip);
            ZipFile f = new ZipFile(zip);
            f.extractAll(folder);
        } catch (ZipException e) {
            logger.error("Failed to unzip {} error {}", zip, e.getClass());
        }
    }
}
