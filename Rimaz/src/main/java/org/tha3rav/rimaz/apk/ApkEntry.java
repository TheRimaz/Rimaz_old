package org.tha3rav.rimaz.apk;

import static org.tha3rav.rimaz.utils.IO.IOUtils.getFileDirectory;
import static org.tha3rav.rimaz.utils.IO.IOUtils.getFileName;

public abstract class ApkEntry
{
    //region Fields
    private String fullName;
    private String name;
    private String directoryName;
    //endregion

    //region Constructors
    public ApkEntry(String fullName)
    {
        this.fullName = fullName;
        this.name = getFileName(fullName);
        this.directoryName = getFileDirectory(fullName);
    }
    //endregion

    //region Properties

    public String getFullName()
    {
        return this.fullName;
    }

    public String getName()
    {
        return name;
    }

    public String getDirectoryName()
    {
        return directoryName;
    }
    //endregion
}

