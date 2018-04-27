package org.tha3rav.rimaz.core.Statements;

import org.tha3rav.rimaz.apk.ApkTypeEntry;
import org.tha3rav.rimaz.core.OOP.AbstractMethod;

import soot.Unit;

public class AnyMethodCallStatement extends AbstractStatement
{
    protected AbstractMethod calledMethod;

    public AnyMethodCallStatement(Unit sootStatement)
    {
        super(sootStatement);
    }

    public AnyMethodCallStatement(Unit sootStatement,
                                  ApkTypeEntry declaringType)
    {
        super(sootStatement,
              declaringType);
    }

    public AnyMethodCallStatement(Unit sootStatement,
                                  AbstractMethod containingMethod)
    {
        super(sootStatement,
              containingMethod);
    }

    public AnyMethodCallStatement(Unit sootStatement,
                                  AbstractMethod containingMethod,
                                  ApkTypeEntry declaringType)
    {
        super(sootStatement,
              containingMethod,
              declaringType);
    }


    public AbstractMethod getCalledMethod()
    {
        return calledMethod;
    }

    public void setCalledMethod(AbstractMethod calledMethod)
    {
        this.calledMethod = calledMethod;
    }
}
