package org.tha3rav.rimaz.core.ArchitecturalLayers.Model;

import org.tha3rav.rimaz.apk.ApkClass;
import org.tha3rav.rimaz.apk.ApkTypeEntry;
import org.tha3rav.rimaz.core.OOP.AbstractMethod;
import org.tha3rav.rimaz.core.OOP.Field;

import java.util.List;
import java.util.Optional;

import soot.SootClass;

public class AutoValueModel extends Model
{
    public AutoValueModel(Model model)
    {
        super(model);
    }

    public AutoValueModel(ApkClass apkClass)
    {
        super(apkClass);
    }
}
