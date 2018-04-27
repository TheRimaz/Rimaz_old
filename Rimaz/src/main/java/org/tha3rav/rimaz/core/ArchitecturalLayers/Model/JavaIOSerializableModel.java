package org.tha3rav.rimaz.core.ArchitecturalLayers.Model;

import org.tha3rav.rimaz.apk.ApkClass;
import org.tha3rav.rimaz.apk.ApkTypeEntry;

public class JavaIOSerializableModel extends Model
{
    public JavaIOSerializableModel(ApkTypeEntry apkTypeEntry)
    {
        super(apkTypeEntry);
    }

    public JavaIOSerializableModel(ApkClass apkClass)
    {
        super(apkClass);
    }

    public JavaIOSerializableModel(Model model)
    {
        super(model);
    }
}
