import com.andrius.hills.Base;
import com.andrius.hills.model.AscFile;
import com.andrius.hills.preprocesing.FileManager;
import com.andrius.hills.preprocesing.Unzipper;
import com.andrius.hills.preprocesing.asc.AscReader;
import com.andrius.hills.preprocesing.asc.AscDownloader;
import com.andrius.hills.preprocesing.asc.ImageFactory;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.andrius.hills.preprocesing.FileManager.forDirectory;

public class App implements Base {

    public static void main(String[] args) {
        List<File> download = new AscDownloader(forDirectory("data/asc")).download();
        logger.info("Downloaded {} files", download.size());

        var ascReader = new AscReader();
        var images = new ImageFactory(forDirectory("data/images"));

        List<AscFile> parts = download.parallelStream()
                .map(ascReader::read)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());

        parts.forEach(images::toImage);
/*
        var file = new com.andrius.hills.preprocesing.osm.Downloader(forDirectory("data/osm")).download();
        Unzipper osmUnzipper = new Unzipper("osm/unzipped");
        List<File> osm = osmUnzipper.unzip(file.stream().collect(Collectors.toList()));*/
    }
}