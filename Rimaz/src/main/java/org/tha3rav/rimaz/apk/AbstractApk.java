package org.tha3rav.rimaz.apk;

import org.tha3rav.rimaz.core.SootLoader;
import org.tha3rav.rimaz.exceptions.ErroneousManifestFileException;
import org.tha3rav.rimaz.exceptions.UnfoundManifestFileException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import soot.SootClass;


import static org.tha3rav.rimaz.utils.EXCEPTIONS.ExceptionMessages
        .UNFOUND_MANIFEST_FILE_ERROR_MESSAGE;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.getBlackListLibraries;

public abstract class AbstractApk
{
    //region Fields
    /**
     * Object containing all entries of the current APK.
     */
    private List<ApkEntry> entries;

    /**
     * Object containing the path of the file of the current APK
     */
    private Path           apkPath;

    /**
     *  Object representing the Manifest file contained within the current APK file.
     */
    private ManifestFile manifestFile;
    //endregion

    //region Constructors
    public AbstractApk(String path, SootLoader sootLoader) throws IOException, ParserConfigurationException, UnfoundManifestFileException, ErroneousManifestFileException
    {
        this.apkPath = Paths.get(path);
        this.entries = new ArrayList<>();

        ApkUnzipper apkUnzipper = new ApkUnzipper();
        List<ApkEntry> allEntries = apkUnzipper.unzip(this.apkPath);

        this.entries.addAll(allEntries);

        manifestFile = getManifestFile();

        List<String> exludedPackages = sootLoader.getExcludedPackages();

        List<SootClass> sootEntries = sootLoader.initialize(true, true,
                                                            true, true)
                                                .LoadApk(this.apkPath)
                                                .getClasses()
                                                .stream()
                                                .filter(sootClass -> !exludedPackages.stream().anyMatch(s -> sootClass.getPackageName().startsWith(s)))
                                                .collect(Collectors.toList());

        List<ApkTypeEntry> apkEntries = sootEntries.stream()
                                                   .map(sootClass -> new ApkTypeEntry(sootClass.getName(),sootClass))
                                                   .collect(Collectors.toList());
        ApkTypeEntryFilter apkTypeEntryFilter = new ApkTypeEntryFilter(apkEntries);

        entries.addAll(apkTypeEntryFilter.Filter());
    }
    //endregion

    //region Methods

    //endregion

    //region Properties
    /**
     * @return all entries of the current APK.
     */
    public List<ApkEntry> getEntries()
    {
        return this.entries;
    }

    /**
     * @return the path of the file of the current APK
     */
    public Path getApkPath()
    {
        return this.apkPath;
    }

    /**
     * @return the Manifest file of the current APK file.
     */
    public ManifestFile getManifestFile() throws UnfoundManifestFileException
    {
        ManifestFile manifestFile = entries.stream()
                                           .filter(apkEntry -> apkEntry instanceof ManifestFile)
                                           .map(apkEntry -> (ManifestFile) apkEntry)
                                           .findAny()
                                           .orElseThrow(() -> new UnfoundManifestFileException(UNFOUND_MANIFEST_FILE_ERROR_MESSAGE));

        return manifestFile;
    }
    //endregion
}

