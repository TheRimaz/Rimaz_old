package org.tha3rav.rimaz.core.ArchitecturalLayers;

import org.tha3rav.rimaz.apk.ApkClass;

import java.util.List;

public interface IModelHelperClassFilter
{
    List<ApkClass> Filter();
    boolean isModel(ApkClass apkClass);
    boolean isHelperClass(ApkClass apkClass);
}

