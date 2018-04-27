package org.tha3rav.rimaz.utils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.tha3rav.rimaz.utils.IO.IOConstants.PATH_SEPARATOR;

public abstract class IO
{
    public static final class IOUtils
    {
        public static void createFolderIfNotExist(Path folderPath) {
            File folder = folderPath.toFile();
            if (!folder.exists()) {
                folder.mkdir();
            }

        }

        public static void createFolderIfNotExist(String folderPath) {
            createFolderIfNotExist(Paths.get(folderPath));
        }

        public static Path stringToPath(String path) {
            return Paths.get(path);
        }

        public static String getFileDirectory(String path)
        {
            int lastIndexOfPathSeparator = path.lastIndexOf(PATH_SEPARATOR);
            switch (lastIndexOfPathSeparator)
            {
                case -1 : return "";
                default : return path.substring(0, lastIndexOfPathSeparator);
            }
        }

        public static String getFileName(String path)
        {
            int lastIndexOfPathSeparator = path.lastIndexOf(PATH_SEPARATOR);
            switch (lastIndexOfPathSeparator)
            {
                case -1 : return path;
                default : return path.substring(lastIndexOfPathSeparator + 1, path.length());
            }
        }
    }

    public static final class IOConstants
    {
        public static final String PATH_SEPARATOR = "/";
        public static final String XML_EXTENSION = ".xml";
    }
}

