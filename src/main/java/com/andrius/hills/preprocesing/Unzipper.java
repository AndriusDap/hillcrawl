package com.andrius.hills.preprocesing;

import com.andrius.hills.Base;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Unzipper implements Base {
    private final String folder;

    public Unzipper(String folder) {
        this.folder = folder;
    }

    public List<File> unzip(List<File> download) {
        File directory = new File(folder);
        if (directory.exists() && directory.listFiles() != null && directory.listFiles().length > 0) {
            logger.info("Returning contents of directory {}", folder);
            File[] files = directory.listFiles();
            return Arrays.asList(files);
        }

        var result = directory.mkdirs();
        logger.info("Creating output directory {}, success: {}", folder, result);

        download.parallelStream().forEach(this::unzip);

        File[] files = directory.listFiles();
        if (files == null) {
            logger.error("No files found after unzipping.");
            return Collections.emptyList();
        } else {
            return Arrays.asList(files);
        }
    }

    public void unzip(File zip) {
        try {
            logger.info("Unzipping file {}", zip);
            ZipFile f = new ZipFile(zip);
            f.extractAll(folder);
        } catch (ZipException e) {
            logger.error("Failed to unzip {} error {}", zip, e.getClass());
        }
    }
}
