package org.tha3rav.rimaz.core.ArchitecturalLayers;

import org.tha3rav.rimaz.apk.ApkClass;
import org.tha3rav.rimaz.apk.ApkTypeEntry;

import soot.SootClass;

public class Presenter extends MiddleArchitecturalLayer
{
    public Presenter(ApkTypeEntry apkTypeEntry)
    {
        super(apkTypeEntry);
    }

    public Presenter(ApkClass apkClass)
    {
        super(apkClass);
    }
}
