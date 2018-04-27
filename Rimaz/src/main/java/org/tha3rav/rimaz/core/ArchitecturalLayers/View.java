package org.tha3rav.rimaz.core.ArchitecturalLayers;

import org.tha3rav.rimaz.apk.ApkEntry;

public class View<T extends ApkEntry> implements IArchitecturalLayer
{
    private T view;

    public View(T apkEntry)
    {
        this.view = apkEntry;
    }
    public void set(T view) { this.view = view; }
    public T get() { return view; }
}
