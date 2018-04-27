package org.tha3rav.rimaz.core.Statements;

import org.tha3rav.rimaz.apk.ApkTypeEntry;
import org.tha3rav.rimaz.core.OOP.AbstractMethod;

import soot.RefType;
import soot.SootClass;
import soot.Type;
import soot.Unit;
import soot.jimple.internal.JAssignStmt;

public class InitializationStatement extends AbstractStatement
{


    public InitializationStatement(Unit sootStatement)
    {
        super(sootStatement);
        SootClass sootClass = retrieveSootClass((JAssignStmt) sootStatement);
        this.invokedObjectType = new ApkTypeEntry(sootClass.getName(), sootClass);
    }

    public InitializationStatement(Unit sootStatement,
                                   ApkTypeEntry declaringType)
    {
        super(sootStatement,
              declaringType);
        SootClass sootClass = retrieveSootClass((JAssignStmt) sootStatement);
        this.invokedObjectType = new ApkTypeEntry(sootClass.getName(), sootClass);
    }

    public InitializationStatement(Unit sootStatement,
                                   AbstractMethod containingMethod)
    {
        super(sootStatement,
              containingMethod);
        SootClass sootClass = retrieveSootClass((JAssignStmt) sootStatement);
        this.invokedObjectType = new ApkTypeEntry(sootClass.getName(), sootClass);
    }

    public InitializationStatement(Unit sootStatement,
                                   AbstractMethod containingMethod,
                                   ApkTypeEntry declaringType)
    {
        super(sootStatement,
              containingMethod,
              declaringType);
        SootClass sootClass = retrieveSootClass((JAssignStmt) sootStatement);
        this.invokedObjectType = new ApkTypeEntry(sootClass.getName(), sootClass);
    }

    private SootClass retrieveSootClass(JAssignStmt sootStatement)
    {
        return ((RefType) sootStatement
                .getLeftOpBox()
                .getValue()
                .getType())
                .getSootClass();
    }

    public Type getInitializingType()
    {
        return ((JAssignStmt)this.getSootStatement()).getRightOpBox().getValue().getType();
    }
}
