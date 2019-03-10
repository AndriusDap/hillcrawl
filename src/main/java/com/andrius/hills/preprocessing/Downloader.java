package com.andrius.hills.preprocessing;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javatuples.Pair;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static java.util.stream.IntStream.range;

public class Downloader {

    private final static String folder = "tiles";
    private final static String pattern =
            "http://srtm.csi.cgiar.org/wp-content/uploads/files/srtm_5x5/ASCII/srtm_{x}_0{y}.zip";

    private Logger logger = LogManager.getLogger();

    public List<File> download() {
        return download(41, 43, 1, 3);
    }

    public List<File> download(int startInclusive, int endExclusive, int startInclusive1, int endExclusive1) {
        File file = new File(folder);
        if(!file.exists()) {
            var result = file.mkdirs();
            logger.info("Creating output directory {}, success: {}", folder, result);
            if(!result) {
                return Collections.emptyList();
            }
        }

        return range(startInclusive, endExclusive)
                .mapToObj(x -> range(startInclusive1, endExclusive1).mapToObj(y -> Pair.with(x, y)))
                .flatMap(identity())
                .parallel()
                .flatMap(t -> download(t.getValue0(), t.getValue1()).stream()).collect(Collectors.toList());
    }

    private Optional<File> download(int x, int y) {
        logger.info("Downloading {} {}", x, y);

        var link = pattern.replace("{x}", Integer.toString(x)).replace("{y}", Integer.toString(y));
        var sections = link.split("/");
        var filename = sections[sections.length - 1];
        var destination = new File(folder + File.separator + filename);

        if (destination.exists()) {
            logger.info("File {} already exists, skipping", destination);
            return Optional.of(destination);
        }

        try {
            logger.info("Downloading {}", link);
            FileUtils.copyURLToFile(new URL(link), destination);
            logger.info("Finished downloading {}, size: {} MB", destination, destination.length() / (1024 * 1024));
            return Optional.of(destination);
        } catch (IOException e) {
            logger.error("Failed to download {} when saving to {}, exception: {}", link, filename, e.getClass());
            return Optional.empty();
        }
    }

}
