package com.andrius.hills.preprocessing;

import com.andrius.hills.model.AscFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AscReader {
    private Logger logger = LogManager.getLogger();

    public Optional<AscFile> read(File f) {
        if (!f.getAbsolutePath().endsWith(".asc")) {
            logger.info("File {} has a wrong extension for an asc file", f);
            return Optional.empty();
        }

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(f));
            var ncols = Integer.parseInt(getHeaderValue(reader, "ncols"));
            var nrows = Integer.parseInt(getHeaderValue(reader, "nrows"));
            var xllcorner = Double.parseDouble(getHeaderValue(reader, "xllcorner"));
            var yllcorner = Double.parseDouble(getHeaderValue(reader, "yllcorner"));
            var cellsize = Double.parseDouble(getHeaderValue(reader, "cellsize"));
            var nodata = Integer.parseInt(getHeaderValue(reader, "NODATA_value"));

            AscFile value = new AscFile(f.getName(), xllcorner, yllcorner, cellsize, readCells(reader, nodata, ncols, nrows));
            logger.info("Read file {} as {}", f, value);
            return Optional.of(value);
        } catch (FileNotFoundException e) {
            logger.error("File {} does not exists {}", f, e.getClass());
        } catch (IOException e) {
            logger.error("Unexpected error when reading file {} {}", f, e.getClass());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                logger.error("Failed to close reader for {}, {}", f, e.getClass());
            }
        }
        return Optional.empty();
    }

    private Map<String, Short> memoized = new HashMap<>();


    private short[][] readCells(BufferedReader reader, int nodata, int ncols, int nrows) throws IOException {
        long start = System.currentTimeMillis();
        var rows = new short[nrows][];
        for (var rowCounter = 0; rowCounter < nrows; rowCounter++) {
            String[] line = reader.readLine().split("\\s");
            rows[rowCounter] = new short[ncols];
            for (int i = 0; i < ncols; i++) {
                rows[rowCounter][i] = Short.parseShort(line[i]);
            }
        }

        long end = System.currentTimeMillis();
        logger.info("read cells took {} ms", end - start);

        return rows;
    }

    private String getHeaderValue(BufferedReader reader, String name) throws IOException {
        String line = reader.readLine();
        if (line.startsWith(name)) {
            String[] parts = line.split("\\s+");
            if (parts.length != 2) {
                throw new IOException("Invalid header format expected 2 parts, got " + parts.length + " in " + line);
            }
            return parts[1];
        } else {
            throw new IOException("Invalid header format " + name + " not found in " + line);
        }

    }
}
