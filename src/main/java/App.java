import com.andrius.hills.model.AscFile;
import com.andrius.hills.preprocesing.Unzipper;
import com.andrius.hills.preprocesing.asc.AscReader;
import com.andrius.hills.preprocesing.asc.Downloader;
import com.andrius.hills.preprocesing.asc.ImageFactory;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class App {

    public static void main(String[] args) {
        var logger = LogManager.getLogger("Main");
        List<File> download = new Downloader().download();
        logger.info("Downloaded {} files", download.size());
        List<File> unzip = new Unzipper("asc/unzipped").unzip(download);
        logger.info("Unzipped {} files", unzip.size());

        var ascReader = new AscReader();
        var images = new ImageFactory();

        List<AscFile> parts = unzip.parallelStream()
                .map(ascReader::read)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());

        parts.forEach(images::toImage);

        var file = new com.andrius.hills.preprocesing.osm.Downloader().download();
        Unzipper osmUnzipper = new Unzipper("osm/unzipped");
        List<File> osm = osmUnzipper.unzip(file.stream().collect(Collectors.toList()));
    }
}