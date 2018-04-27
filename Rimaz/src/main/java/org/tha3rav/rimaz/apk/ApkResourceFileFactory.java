package org.tha3rav.rimaz.apk;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import static org.tha3rav.rimaz.utils.ANIMATION.AnimationConstants.ANIMATION;
import static org.tha3rav.rimaz.utils.APK.APKConstants.ANDROID_MANIFEST_FILE_NAME;
import static org.tha3rav.rimaz.utils.IO.IOUtils.getFileDirectory;
import static org.tha3rav.rimaz.utils.IO.IOUtils.getFileName;
import static org.tha3rav.rimaz.utils.LAYOUT.LayoutFileConstants.LAYOUT;

public class ApkResourceFileFactory extends ApkResourceFileAbstractFactory
{
    @Override
    public ApkResourceFile getApkResourceFile(ApkBinaryXmlFile binaryXmlFile) throws IOException, ParserConfigurationException
    {
        if (isLayoutFile(binaryXmlFile))
        {
            return new LayoutFile(binaryXmlFile);
        }
        else
        {
            if (isManifestFile(binaryXmlFile))
            {
                return new ManifestFile(binaryXmlFile);
            }
            else
            {
                if (isAnimationFile(binaryXmlFile))
                {
                    return new AnimationFile(binaryXmlFile);
                }
            }
            return new ApkResourceFile(binaryXmlFile);
        }
    }

    @Override
    protected boolean isLayoutFile(ApkBinaryXmlFile binaryXmlFile)
    {
        return getFileDirectory(binaryXmlFile.getName()).contains(LAYOUT);
    }

    @Override
    protected boolean isManifestFile(ApkBinaryXmlFile binaryXmlFile)
    {
        return getFileName(binaryXmlFile.getName()).equals(ANDROID_MANIFEST_FILE_NAME);
    }

    @Override
    protected boolean isAnimationFile(ApkBinaryXmlFile binaryXmlFile)
    {
        return getFileDirectory(binaryXmlFile.getName()).contains(ANIMATION);
    }
}
