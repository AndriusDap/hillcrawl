package com.andrius.hills.preprocesing.osm;

import com.andrius.hills.Base;
import com.andrius.hills.preprocesing.FileManager;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class Downloader implements Base {

    private static final String server = "http://download.geofabrik.de/europe/lithuania-latest.osm.bz2";
    private static final String lt = "lt.osm.bz2";

    private FileManager fileManager;

    public Downloader(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public Optional<File> download() {
        return fileManager.produce(lt, file -> {
            try {
                logger.info("Downloading {} into {}", server, file);
                FileUtils.copyURLToFile(new URL(server), file);
            } catch (IOException e) {
                logger.error("OSM Url is not valid {} {}", server, e.getClass());
                throw new RuntimeException(e);
            }
        });
    }
}
