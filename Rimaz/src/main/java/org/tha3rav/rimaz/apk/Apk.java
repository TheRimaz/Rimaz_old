package org.tha3rav.rimaz.apk;

import org.tha3rav.rimaz.core.SootLoader;
import org.tha3rav.rimaz.exceptions.ErroneousManifestFileException;
import org.tha3rav.rimaz.exceptions.UnfoundManifestFileException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

public class Apk extends AbstractApk
{
    public Apk(String path, SootLoader sootLoader) throws IOException, ParserConfigurationException, UnfoundManifestFileException, ErroneousManifestFileException
    {
        super(path, sootLoader);
    }
}