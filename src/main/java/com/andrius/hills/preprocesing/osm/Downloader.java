package com.andrius.hills.preprocesing.osm;

import com.andrius.hills.model.AscFile;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class Downloader {
    private Logger logger = LogManager.getLogger();
    private static final String folder = "osm";
    private static final String server = "http://download.geofabrik.de/europe/lithuania-latest.osm.bz2";
    private static final String lt = "lt.shp.zip";

    public Optional<File> download() {
        File targetDirectory = new File(folder);
        if (!targetDirectory.exists()) {
            var result = targetDirectory.mkdirs();
            logger.info("Creating output directory {}, success: {}", folder, result);
            if (!result) {
                return Optional.empty();
            }
        }
        File file = new File(folder + File.separator + lt);
        if (file.exists()) {
            logger.info("{} already exists, returning a cached version");
            return Optional.of(file);
        }
        try {
            logger.info("Downloading {} into {}", server, file);
            FileUtils.copyURLToFile(new URL(server), file);
            logger.info("Received OSM, size {} MB", file.length() / (1024 * 1024));
            return Optional.of(file);
        } catch (IOException e) {
            logger.error("OSM Url is not valid {} {}", server, e.getClass());
            return Optional.empty();
        }
    }
}
