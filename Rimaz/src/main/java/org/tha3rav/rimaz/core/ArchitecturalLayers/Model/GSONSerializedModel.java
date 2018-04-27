package org.tha3rav.rimaz.core.ArchitecturalLayers.Model;

import org.tha3rav.rimaz.apk.ApkClass;
import org.tha3rav.rimaz.apk.ApkTypeEntry;

public class GSONSerializedModel extends Model
{
    public GSONSerializedModel(ApkTypeEntry apkTypeEntry)
    {
        super(apkTypeEntry);
    }

    public GSONSerializedModel(ApkClass apkClass)
    {
        super(apkClass);
    }

    public GSONSerializedModel(Model model)
    {
        super(model);
    }
}
