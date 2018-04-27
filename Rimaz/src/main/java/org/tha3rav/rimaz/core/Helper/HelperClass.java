package org.tha3rav.rimaz.core.Helper;

import org.tha3rav.rimaz.apk.ApkClass;
import org.tha3rav.rimaz.apk.ApkTypeEntry;
import org.tha3rav.rimaz.core.OOP.AbstractMethod;
import org.tha3rav.rimaz.core.OOP.Field;

import java.util.List;
import java.util.Optional;

import soot.SootClass;

public class HelperClass extends ApkClass
{

    public HelperClass(ApkTypeEntry apkTypeEntry)
    {
        super(apkTypeEntry);
    }

    public HelperClass(ApkClass apkClass)
    {
        super(apkClass);
        this.getters = apkClass.getGetters();
        this.setters = apkClass.getSetters();
        this.constructors = apkClass.getConstructors();
        this.enclosingMethod = apkClass.getEnclosingMethod();
        this.isAnonymous = apkClass.isAnonymous();
    }
}
