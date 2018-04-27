package org.tha3rav.rimaz.core.AndroidSpecific;

import org.tha3rav.rimaz.apk.ApkTypeEntry;

public abstract class AndroidGeneratedTypesAbstractFactory
{
    public abstract AndroidGeneratedClass getAndroidSpecificClass(ApkTypeEntry apkClass);

    public abstract boolean isRClass(ApkTypeEntry apkClass);
    public abstract boolean isBRClass(ApkTypeEntry apkClass);
    public abstract boolean isRInnerClass(ApkTypeEntry apkClass);
    public abstract boolean isBuildConfigClass(ApkTypeEntry apkClass);
    public abstract boolean isDataBindingClass(ApkTypeEntry apkClass);
}
