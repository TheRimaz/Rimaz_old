package org.tha3rav.rimaz.core.ArchitecturalLayers;

import org.tha3rav.rimaz.apk.ApkClass;
import org.tha3rav.rimaz.apk.ApkTypeEntry;

import soot.SootClass;

public class Controller extends MiddleArchitecturalLayer
{
    public Controller(ApkTypeEntry apkTypeEntry)
    {
        super(apkTypeEntry);
    }

    public Controller(ApkClass apkClass)
    {
        super(apkClass);
    }
}
