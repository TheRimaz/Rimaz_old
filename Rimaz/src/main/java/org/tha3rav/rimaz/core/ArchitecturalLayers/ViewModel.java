package org.tha3rav.rimaz.core.ArchitecturalLayers;

import org.tha3rav.rimaz.apk.ApkClass;
import org.tha3rav.rimaz.apk.ApkTypeEntry;

public class ViewModel extends MiddleArchitecturalLayer
{
    public ViewModel(ApkTypeEntry apkTypeEntry)
    {
        super(apkTypeEntry);
    }

    public ViewModel(ApkClass apkClass)
    {
        super(apkClass);
    }
}
