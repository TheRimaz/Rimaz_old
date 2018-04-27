package org.tha3rav.rimaz.core.OOP;

import org.tha3rav.rimaz.apk.ApkTypeEntry;

import soot.SootMethod;

public class Getter extends AbstractMethod
{
    private Field field;

    public Getter(SootMethod sootMethod)
    {
        super(sootMethod);
    }

    public Getter(SootMethod sootMethod, Field field)
    {
        super(sootMethod);
        this.field = field;
    }

    public Getter(SootMethod sootMethod,
                  ApkTypeEntry containingType)
    {
        super(sootMethod,
              containingType);
    }

    public Getter(SootMethod sootMethod, ApkTypeEntry containingType, Field field)
    {
        super(sootMethod, containingType);
        this.field = field;
    }

    public Field getField()
    {
        return field;
    }

    public void setField(Field field)
    {
        this.field = field;
    }
}
