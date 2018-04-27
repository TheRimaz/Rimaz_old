package org.tha3rav.rimaz.core.Statements;

import org.tha3rav.rimaz.apk.ApkTypeEntry;
import org.tha3rav.rimaz.core.OOP.AbstractMethod;
import org.tha3rav.rimaz.core.OOP.Getter;

import soot.SootClass;
import soot.Unit;
import soot.jimple.internal.AbstractInvokeExpr;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JInvokeStmt;

import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.isAssignmentStatement;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.isInvocationStatement;

public class GetterCallStatement extends AnyMethodCallStatement
{
    public GetterCallStatement(Unit sootStatement)
    {
        super(sootStatement);

        this.calledMethod = retrieveCalledMethod(sootStatement);
        SootClass sootClass = retrieveSootClass(sootStatement);
        this.invokedObjectType = new ApkTypeEntry(sootClass.getName(), sootClass);
    }

    public GetterCallStatement(Unit sootStatement,
                               ApkTypeEntry declaringType)
    {
        super(sootStatement,
              declaringType);

        this.calledMethod = retrieveCalledMethod(sootStatement);
        SootClass sootClass = retrieveSootClass(sootStatement);
        this.invokedObjectType = new ApkTypeEntry(sootClass.getName(), sootClass);

    }

    public GetterCallStatement(Unit sootStatement,
                               AbstractMethod containingMethod)
    {
        super(sootStatement,
              containingMethod);

        this.calledMethod = retrieveCalledMethod(sootStatement);
        SootClass sootClass = retrieveSootClass(sootStatement);
        this.invokedObjectType = new ApkTypeEntry(sootClass.getName(), sootClass);
    }

    public GetterCallStatement(Unit sootStatement,
                               AbstractMethod containingMethod,
                               ApkTypeEntry declaringType)
    {
        super(sootStatement,
              containingMethod,
              declaringType);

        this.calledMethod = retrieveCalledMethod(sootStatement);
        SootClass sootClass = retrieveSootClass(sootStatement);
        this.invokedObjectType = new ApkTypeEntry(sootClass.getName(), sootClass);
    }

    private AbstractMethod retrieveCalledMethod(Unit sootStatement)
    {
        AbstractMethod calledMethod = null;
        if (isInvocationStatement(sootStatement))
        {
            calledMethod = new Getter(((AbstractInvokeExpr)(((JInvokeStmt)sootStatement).getInvokeExprBox().getValue())).getMethod());
        }
        else
        {
            if (isAssignmentStatement(sootStatement))
            {
                calledMethod = new Getter(((AbstractInvokeExpr)(((JAssignStmt)sootStatement).getRightOpBox()).getValue()).getMethod());
            }
        }
        return calledMethod;
    }

    private SootClass retrieveSootClass(Unit sootStatement)
    {
        SootClass sootClass = null;
        if (isInvocationStatement(sootStatement))
        {
            sootClass = ((AbstractInvokeExpr)(((JInvokeStmt)sootStatement).getInvokeExprBox()
                    .getValue()))
                    .getMethodRef()
                    .declaringClass();
        }
        else
        {
            if (isAssignmentStatement(sootStatement))
            {
                sootClass = ((AbstractInvokeExpr)(((JAssignStmt)sootStatement).getRightOpBox()).getValue()).getMethodRef().declaringClass();
            }
        }
        return sootClass;
    }
}
