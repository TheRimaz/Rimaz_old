package org.tha3rav.rimaz.core.Statements;

import org.tha3rav.rimaz.apk.ApkTypeEntry;
import org.tha3rav.rimaz.core.OOP.AbstractMethod;
import org.tha3rav.rimaz.core.OOP.Field;

import soot.SootClass;
import soot.Unit;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JInstanceFieldRef;

public class InstanceFieldAssignmentStatement extends AbstractStatement
{
    protected Field field;

    public InstanceFieldAssignmentStatement(Unit sootStatement)
    {
        super(sootStatement);

        SootClass sootClass = retrieveSootClass(sootStatement);
        this.invokedObjectType = new ApkTypeEntry(sootClass.getName(), sootClass);
        field = retrieveField(sootStatement);
    }

    public InstanceFieldAssignmentStatement(Unit sootStatement,
                                            ApkTypeEntry declaringType)
    {
        super(sootStatement,
              declaringType);

        SootClass sootClass = retrieveSootClass(sootStatement);
        this.invokedObjectType = new ApkTypeEntry(sootClass.getName(), sootClass);
        field = retrieveField(sootStatement);
    }

    public InstanceFieldAssignmentStatement(Unit sootStatement,
                                            AbstractMethod containingMethod)
    {
        super(sootStatement,
              containingMethod);

        SootClass sootClass = retrieveSootClass(sootStatement);
        this.invokedObjectType = new ApkTypeEntry(sootClass.getName(), sootClass);
        field = retrieveField(sootStatement);
    }

    public InstanceFieldAssignmentStatement(Unit sootStatement,
                                            AbstractMethod containingMethod,
                                            ApkTypeEntry declaringType)
    {
        super(sootStatement,
              containingMethod,
              declaringType);

        SootClass sootClass = retrieveSootClass(sootStatement);
        this.invokedObjectType = new ApkTypeEntry(sootClass.getName(), sootClass);
        field = retrieveField(sootStatement);
    }

    private Field retrieveField(Unit sootStatement)
    {
        return new Field(((JInstanceFieldRef)((JAssignStmt)sootStatement).getLeftOpBox().getValue()).getField());
    }

    private SootClass retrieveSootClass(Unit sootStatement)
    {
        return ((JInstanceFieldRef)((JAssignStmt)sootStatement).getLeftOpBox()
                .getValue())
                .getFieldRef()
                .declaringClass();
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
