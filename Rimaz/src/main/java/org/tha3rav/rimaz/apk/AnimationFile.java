package org.tha3rav.rimaz.apk;

import org.tha3rav.rimaz.apk.ApkBinaryXmlFile;
import org.tha3rav.rimaz.apk.ApkResourceFile;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

public class AnimationFile extends ApkResourceFile
{
    public AnimationFile(ApkBinaryXmlFile binaryXmlFile) throws IOException, ParserConfigurationException
    {
        super(binaryXmlFile);
    }
}
