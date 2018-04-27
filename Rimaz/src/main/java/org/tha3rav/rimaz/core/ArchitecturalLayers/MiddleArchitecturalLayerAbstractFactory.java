package org.tha3rav.rimaz.core.ArchitecturalLayers;

import org.tha3rav.rimaz.apk.ApkClass;

public abstract class MiddleArchitecturalLayerAbstractFactory
{
    public abstract MiddleArchitecturalLayer getMiddleArchitecturalLayer(ApkClass apkClass);

    protected abstract boolean isController(ApkClass apkClass);

    protected abstract boolean isPresenter(ApkClass apkClass);

    protected abstract boolean isViewModel(ApkClass apkClass);
}
