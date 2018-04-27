package org.tha3rav.rimaz.core.ArchitecturalLayers.Model;

import org.tha3rav.rimaz.apk.ApkClass;

import soot.SootClass;

public class ParcelableModel extends Model
{
    public ParcelableModel(Model model)
    {
        super(model);
    }

    public ParcelableModel(ApkClass apkClass)
    {
        super(apkClass);
    }
}
