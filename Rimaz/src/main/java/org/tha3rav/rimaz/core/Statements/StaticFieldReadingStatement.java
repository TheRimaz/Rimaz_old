package org.tha3rav.rimaz.core.Statements;

import org.tha3rav.rimaz.apk.ApkTypeEntry;
import org.tha3rav.rimaz.core.OOP.AbstractMethod;
import org.tha3rav.rimaz.core.OOP.Field;

import soot.SootClass;
import soot.Unit;
import soot.jimple.StaticFieldRef;
import soot.jimple.internal.JAssignStmt;

public class StaticFieldReadingStatement extends AbstractStatement
{
    protected Field field;

    public StaticFieldReadingStatement(Unit sootStatement)
    {
        super(sootStatement);

        SootClass sootClass = retrieveSootClass(sootStatement);
        this.invokedObjectType = new ApkTypeEntry(sootClass.getName(), sootClass);
        field = retrieveField(sootStatement);
    }

    public StaticFieldReadingStatement(Unit sootStatement,
                                       ApkTypeEntry declaringType)
    {
        super(sootStatement,
              declaringType);

        SootClass sootClass = retrieveSootClass(sootStatement);
        this.invokedObjectType = new ApkTypeEntry(sootClass.getName(), sootClass);
        field = retrieveField(sootStatement);
    }

    public StaticFieldReadingStatement(Unit sootStatement,
                                       AbstractMethod containingMethod)
    {
        super(sootStatement,
              containingMethod);

        SootClass sootClass = retrieveSootClass(sootStatement);
        this.invokedObjectType = new ApkTypeEntry(sootClass.getName(), sootClass);
        field = retrieveField(sootStatement);
    }

    public StaticFieldReadingStatement(Unit sootStatement,
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
        return new Field(((StaticFieldRef)((JAssignStmt)sootStatement).getRightOpBox().getValue()).getField());
    }

    private SootClass retrieveSootClass(Unit sootStatement)
    {
        return ((StaticFieldRef)((JAssignStmt)sootStatement).getRightOpBox()
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
