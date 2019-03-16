package com.andrius.hills.preprocesing.asc;

import com.andrius.hills.Base;
import com.andrius.hills.preprocesing.FileManager;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.javatuples.Pair;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static java.util.stream.IntStream.range;

public class AscDownloader implements Base {

    private final static String serverPattern =
            "http://srtm.csi.cgiar.org/wp-content/uploads/files/srtm_5x5/ASCII/srtm_{x}_0{y}.zip";
    private final static String filePattern = "srtm_{x}_0{y}.asc";

    private FileManager fileManager;

    public AscDownloader(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public List<File> download() {
        return download(41, 43, 1, 3);
    }

    private List<File> download(int startInclusive, int endExclusive, int startInclusive1, int endExclusive1) {
        //Adding intermediate list allows efficient par stream
        var intermediateList = range(startInclusive, endExclusive)
                .mapToObj(x -> range(startInclusive1, endExclusive1).mapToObj(y -> Pair.with(x, y)))
                .flatMap(identity()).collect(Collectors.toList());

        return intermediateList.parallelStream()
                .flatMap(t -> download(t.getValue0(), t.getValue1()).stream())
                .collect(Collectors.toList());
    }

    private Optional<File> download(int x, int y) {
        logger.info("Downloading {} {}", x, y);
        var link = inject(serverPattern, x, y);
        var filename = inject(filePattern, x, y);
        return fileManager.produce(filename, destination -> {
            try {
                var temp = fileManager.tempFile();
                logger.info("Downloading {}", link);
                FileUtils.copyURLToFile(new URL(link), temp);
                ZipFile f = new ZipFile(temp);
                f.extractFile(filename, destination.getParent());
            } catch (IOException | ZipException e) {
                logger.error("Failed to download {}, exception: {}", link, e.getClass());
            }
        });
    }

    private static String inject(String pattern, int x, int y) {
        return pattern.replace("{x}", Integer.toString(x)).replace("{y}", Integer.toString(y));
    }
}
