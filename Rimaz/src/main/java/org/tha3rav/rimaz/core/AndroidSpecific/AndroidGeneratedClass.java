package org.tha3rav.rimaz.core.AndroidSpecific;

import org.tha3rav.rimaz.apk.ApkClass;
import org.tha3rav.rimaz.apk.ApkTypeEntry;

public class AndroidGeneratedClass extends ApkClass
{
    public AndroidGeneratedClass(ApkTypeEntry apkTypeEntry)
    {
        super(apkTypeEntry);
    }

    public AndroidGeneratedClass(ApkClass apkClass)
    {
        super(apkClass);
        this.getters = apkClass.getGetters();
        this.setters = apkClass.getSetters();
        this.constructors = apkClass.getConstructors();
        this.enclosingMethod = apkClass.getEnclosingMethod();
        this.isAnonymous = apkClass.isAnonymous();
    }
}
