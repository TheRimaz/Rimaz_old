package org.tha3rav.rimaz.core.Statements;

import org.tha3rav.rimaz.apk.ApkTypeEntry;
import org.tha3rav.rimaz.core.OOP.AbstractMethod;

import soot.Unit;

public abstract class StatementAbstractFactory
{
    public abstract AbstractStatement getStatement(Unit statement);
    public abstract AbstractStatement getStatement(Unit statement, AbstractMethod containingMethod, ApkTypeEntry containingType);

    public abstract boolean isInitializationStatement(Unit statement);
    public abstract boolean isInstanceFieldAssignmentStatement(Unit statement);
    public abstract boolean isInstanceFieldReadingStatement(Unit statement);
    public abstract boolean isStaticFieldAssignmentStatement(Unit statement);
    public abstract boolean isStaticFieldReadingStatement(Unit statement);
    public abstract boolean isAnyMethodCallStatement(Unit statement);
    public abstract boolean isMethodCallStatement(Unit statement);
    public abstract boolean isGetterCallStatement(Unit statement);
    public abstract boolean isSetterCallStatement(Unit statement);


}
