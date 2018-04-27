package org.tha3rav.rimaz.core.Statements;

import org.tha3rav.rimaz.apk.ApkTypeEntry;
import org.tha3rav.rimaz.core.OOP.AbstractMethod;

import soot.Unit;

public abstract class AbstractStatement
{
    protected Unit         sootStatement;
    protected ApkTypeEntry invokedObjectType;
    protected ApkTypeEntry declaringType;
    protected AbstractMethod containingMethod;

    public AbstractStatement(Unit sootStatement)
    {
        this.sootStatement = sootStatement;
    }

    public AbstractStatement(Unit sootStatement, ApkTypeEntry declaringType)
    {
        this.sootStatement = sootStatement;
        this.declaringType = declaringType;
    }

    public AbstractStatement(Unit sootStatement, AbstractMethod containingMethod)
    {
        this.sootStatement = sootStatement;
        this.containingMethod = containingMethod;
    }

    public AbstractStatement(Unit sootStatement, AbstractMethod containingMethod, ApkTypeEntry declaringType)
    {
        this.sootStatement = sootStatement;
        this.containingMethod = containingMethod;
        this.declaringType = declaringType;
    }

    public Unit getSootStatement()
    {
        return sootStatement;
    }

    public void setSootStatement(Unit sootStatement)
    {
        this.sootStatement = sootStatement;
    }

    public ApkTypeEntry getInvokedObjectType()
    {
        return invokedObjectType;
    }

    public void setInvokedObjectType(ApkTypeEntry invokedObjectType)
    {
        this.invokedObjectType = invokedObjectType;
    }

    public ApkTypeEntry getDeclaringType()
    {
        return declaringType;
    }

    public void setDeclaringType(ApkTypeEntry declaringType)
    {
        this.declaringType = declaringType;
    }

    public AbstractMethod getContainingMethod()
    {
        return containingMethod;
    }

    public void setContainingMethod(AbstractMethod containingMethod)
    {
        this.containingMethod = containingMethod;
    }
}
