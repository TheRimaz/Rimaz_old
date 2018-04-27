package org.tha3rav.rimaz.core.ArchitecturalLayers;

import org.tha3rav.rimaz.apk.ApkClass;

public abstract class ActiveClassAbstractFactory
{
    public abstract ActiveClass getActiveClass(ApkClass apkClass);

    public abstract boolean isApplicationClass(ApkClass apkClass);

    public abstract boolean isActivityClass(ApkClass apkClass);

    public abstract boolean isFragmentClass(ApkClass apkClass);

    public abstract boolean isUIElementClass(ApkClass apkClass);

    public abstract boolean isDialogClass(ApkClass apkClass);
}
