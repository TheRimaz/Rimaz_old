package org.tha3rav.rimaz.apk;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

public interface IApkUnzipper
{
    List<ApkEntry> unzip(Path path) throws IOException, ParserConfigurationException;

    void unzip(Path var1, Path path) throws IOException;
}
