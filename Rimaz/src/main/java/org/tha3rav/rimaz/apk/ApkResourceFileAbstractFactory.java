package org.tha3rav.rimaz.apk;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

public abstract class ApkResourceFileAbstractFactory
{
    public abstract ApkResourceFile getApkResourceFile(ApkBinaryXmlFile binaryXmlFile) throws IOException, ParserConfigurationException;

    protected abstract boolean isLayoutFile(ApkBinaryXmlFile binaryXmlFile);
    protected abstract boolean isManifestFile(ApkBinaryXmlFile binaryXmlFile);
    protected abstract boolean isAnimationFile(ApkBinaryXmlFile binaryXmlFile);
}
