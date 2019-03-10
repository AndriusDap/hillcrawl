import com.andrius.hills.preprocessing.AscReader;
import com.andrius.hills.preprocessing.Downloader;
import com.andrius.hills.preprocessing.ImageFactory;
import com.andrius.hills.preprocessing.Unzipper;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class App {

    public static void main(String[] args) {
        var logger = LogManager.getLogger("Main");
        List<File> download = new Downloader().download();
        logger.info("Downloaded {} files", download.size());
        List<File> unzip = new Unzipper().unzip(download);
        logger.info("Unzipped {} files", unzip.size());

        var ascReader = new AscReader();
        var images = new ImageFactory();

        unzip.parallelStream().map(ascReader::read).flatMap(Optional::stream).forEach(images::toImage);
    }
}
