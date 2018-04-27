package org.tha3rav.rimaz.apk;

public abstract class ApkTypeEntryAbstractFactory
{
    public abstract ApkTypeEntry getApkTypeEntry(ApkTypeEntry apkTypeEntry);

    public abstract boolean isApkClass(ApkTypeEntry apkTypeEntry);
    public abstract boolean isApkInterface(ApkTypeEntry apkTypeEntry);
}
