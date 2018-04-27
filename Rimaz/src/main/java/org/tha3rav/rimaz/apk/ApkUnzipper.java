package org.tha3rav.rimaz.apk;

import org.tha3rav.rimaz.utils.IO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.ParserConfigurationException;

import static org.tha3rav.rimaz.utils.IO.IOUtils.createFolderIfNotExist;

public class ApkUnzipper implements IApkUnzipper
{
    public ApkUnzipper()
    {
    }

    public List<ApkEntry> unzip(Path apkPath) throws IOException, ParserConfigurationException
    {
        List<ApkEntry>                entries = new ArrayList<>();
        ZipFile                       apkAsZip   = new ZipFile(apkPath.toString());
        ArrayList<? extends ZipEntry> apkEntries = Collections.list(apkAsZip.entries());
        ApkResourceFileAbstractFactory apkResourceFileFactory = new ApkResourceFileFactory();

        for (ZipEntry zipEntry : apkEntries)
        {
            if (zipEntry.getName().toLowerCase().endsWith(IO.IOConstants.XML_EXTENSION))
            {
                ApkBinaryXmlFile apkBinaryXmlFile = new ApkBinaryXmlFile(apkAsZip.getInputStream(zipEntry), zipEntry.getName());
                ApkResourceFile apkResourceFile = apkResourceFileFactory.getApkResourceFile(apkBinaryXmlFile);
                entries.add(apkResourceFile);
            }
            else
            {
                entries.add(new ConcreteApkEntry(zipEntry.getName()));
            }
        }
        return entries;
    }

    public void unzip(Path apkPath, Path outputFolderPath) throws IOException {
        byte[] buffer = new byte[1024];
        createFolderIfNotExist(outputFolderPath);
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(apkPath.toString()));

        for(ZipEntry zipEntry = zipInputStream.getNextEntry(); zipEntry != null; zipEntry = zipInputStream.getNextEntry()) {
            String fileName = zipEntry.getName();
            File   newFile  = outputFolderPath.resolve(fileName).toFile();
            (new File(newFile.getParent())).mkdirs();
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);

            int len;
            while((len = zipInputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, len);
            }

            fileOutputStream.close();
        }

        zipInputStream.closeEntry();
        zipInputStream.close();
    }
}