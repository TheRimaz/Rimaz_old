package org.tha3rav.rimaz.core.ArchitecturalLayers;

import org.tha3rav.rimaz.apk.ApkEntry;

public abstract class ViewAbstractFactory
{
    public abstract View<ApkEntry> getView(ApkEntry apkEntry);

    protected abstract boolean isXmlFileView(ApkEntry apkEntry);

    protected abstract boolean isApkClassFileView(ApkEntry apkEntry);
}
