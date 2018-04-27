package org.tha3rav.rimaz.core.ArchitecturalLayers.Model;

import org.tha3rav.rimaz.apk.ApkClass;
import org.tha3rav.rimaz.apk.ApkTypeEntry;
import org.tha3rav.rimaz.core.ArchitecturalLayers.IArchitecturalLayer;
import org.tha3rav.rimaz.core.OOP.AbstractMethod;
import org.tha3rav.rimaz.core.OOP.Field;

import java.util.List;
import java.util.Optional;

import soot.SootClass;

public class Model extends ApkClass implements IArchitecturalLayer
{
    public Model(ApkTypeEntry apkTypeEntry)
    {
        super(apkTypeEntry);
    }

    public Model(ApkClass apkClass)
    {
        super(apkClass);
        this.fields = apkClass.getFields();
        this.getters = apkClass.getGetters();
        this.setters = apkClass.getSetters();
        this.constructors = apkClass.getConstructors();
        this.methods = apkClass.getMethods();
        this.enclosingMethod = apkClass.getEnclosingMethod();
        this.isAnonymous = apkClass.isAnonymous();
    }
}
