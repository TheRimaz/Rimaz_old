package org.tha3rav.rimaz.core.OOP;

import org.tha3rav.rimaz.apk.ApkTypeEntry;

import soot.SootMethod;

public class Constructor extends AbstractMethod
{

    public Constructor(SootMethod sootMethod)
    {
        super(sootMethod);
    }

    public Constructor(SootMethod sootMethod,
                       ApkTypeEntry containingType)
    {
        super(sootMethod,
              containingType);
    }
}
