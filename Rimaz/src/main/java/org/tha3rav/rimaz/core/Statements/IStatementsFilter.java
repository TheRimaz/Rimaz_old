package org.tha3rav.rimaz.core.Statements;

import java.util.List;

public interface IStatementsFilter
{
    List<AbstractStatement> filter();

    List<InitializationStatement> getInitializationStatements();

    List<InstanceFieldAssignmentStatement> getInstanceFieldAssignmentStatements();

    List<InstanceFieldReadingStatement> getInstanceFieldReadingStatements();

    List<StaticFieldAssignmentStatement> getStaticFieldAssignmentStatements();

    List<StaticFieldReadingStatement> getStaticFieldReadingStatements();

    List<MethodCallStatement> getMethodCallStatements();

    List<GetterCallStatement> getGetterCallStatements();

    List<SetterCallStatement> getSetterCallStatements();
}
