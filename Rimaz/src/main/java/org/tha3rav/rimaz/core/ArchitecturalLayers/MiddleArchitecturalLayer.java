package org.tha3rav.rimaz.core.ArchitecturalLayers;

import org.tha3rav.rimaz.apk.ApkClass;
import org.tha3rav.rimaz.apk.ApkTypeEntry;
import org.tha3rav.rimaz.core.OOP.AbstractMethod;
import org.tha3rav.rimaz.core.OOP.Field;

import java.util.List;
import java.util.Optional;

import soot.SootClass;

public class MiddleArchitecturalLayer extends ApkClass implements IArchitecturalLayer
{
    public MiddleArchitecturalLayer(ApkTypeEntry apkTypeEntry)
    {
        super(apkTypeEntry);
    }

    public MiddleArchitecturalLayer(ApkClass apkClass)
    {
        super(apkClass);
        this.getters = apkClass.getGetters();
        this.setters = apkClass.getSetters();
        this.constructors = apkClass.getConstructors();
        this.isAnonymous = apkClass.isAnonymous();
        this.enclosingMethod = apkClass.getEnclosingMethod();
    }
}
