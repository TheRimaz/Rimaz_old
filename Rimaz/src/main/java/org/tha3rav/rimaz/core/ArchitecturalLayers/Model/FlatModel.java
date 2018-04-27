package org.tha3rav.rimaz.core.ArchitecturalLayers.Model;

import org.tha3rav.rimaz.apk.ApkClass;
import org.tha3rav.rimaz.apk.ApkTypeEntry;

public class FlatModel extends Model
{
    public FlatModel(ApkTypeEntry apkTypeEntry)
    {
        super(apkTypeEntry);
    }

    public FlatModel(ApkClass apkClass)
    {
        super(apkClass);
    }

    public FlatModel(Model model)
    {
        super(model);
    }
}
