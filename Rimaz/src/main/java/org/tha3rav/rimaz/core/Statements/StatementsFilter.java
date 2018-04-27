package org.tha3rav.rimaz.core.Statements;

import org.tha3rav.rimaz.apk.ApkTypeEntry;
import org.tha3rav.rimaz.core.OOP.AbstractMethod;
import org.tha3rav.rimaz.core.OOP.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import soot.Unit;

public class StatementsFilter implements IStatementsFilter
{
    private List<AbstractStatement> allStatements;

    public StatementsFilter(List<Unit> sootStatements)
    {
        StatementAbstractFactory statementFactory = new StatementFactory();
        allStatements = new ArrayList<>();
        allStatements.addAll(sootStatements.stream()
                                  .map(sootStatement -> statementFactory.getStatement(sootStatement))
                                  .collect(Collectors.toList()));
    }

    public StatementsFilter(List<Unit> sootStatements, AbstractMethod containingMethod, ApkTypeEntry containingType)
    {
        StatementAbstractFactory statementFactory = new StatementFactory();
        allStatements = new ArrayList<>();
        allStatements.addAll(sootStatements.stream()
                                     .map(sootStatement -> statementFactory.getStatement(sootStatement, containingMethod, containingType))
                                     .collect(Collectors.toList()));
    }

    @Override
    public List<AbstractStatement> filter()
    {
        List<AbstractStatement> filteredStatements = new ArrayList<>();

        List<InitializationStatement> initializationStatements = getInitializationStatements();
        List<InstanceFieldAssignmentStatement> instanceFieldAssignmentStatements = getInstanceFieldAssignmentStatements();
        List<InstanceFieldReadingStatement> instanceFieldReadingStatements = getInstanceFieldReadingStatements();
        List<StaticFieldAssignmentStatement> staticFieldAssignmentStatements = getStaticFieldAssignmentStatements();
        List<StaticFieldReadingStatement> staticFieldReadingStatements = getStaticFieldReadingStatements();
        List<MethodCallStatement> methodCallStatements = getMethodCallStatements();
        List<GetterCallStatement> getterCallStatements = getGetterCallStatements();
        List<SetterCallStatement> setterCallStatements = getSetterCallStatements();

        filteredStatements.addAll(initializationStatements);
        filteredStatements.addAll(instanceFieldAssignmentStatements);
        filteredStatements.addAll(instanceFieldReadingStatements);
        filteredStatements.addAll(staticFieldAssignmentStatements);
        filteredStatements.addAll(staticFieldReadingStatements);
        filteredStatements.addAll(methodCallStatements);
        filteredStatements.addAll(getterCallStatements);
        filteredStatements.addAll(setterCallStatements);


        return filteredStatements;
    }

    @Override
    public List<InitializationStatement> getInitializationStatements()
    {
        return allStatements.stream()
                .filter(abstractStatement -> abstractStatement instanceof InitializationStatement)
                .map(abstractStatement -> (InitializationStatement)abstractStatement)
                .collect(Collectors.toList());
    }

    @Override
    public List<InstanceFieldAssignmentStatement> getInstanceFieldAssignmentStatements()
    {
        return allStatements.stream()
                .filter(abstractStatement -> abstractStatement instanceof InstanceFieldAssignmentStatement)
                .map(abstractStatement -> (InstanceFieldAssignmentStatement)abstractStatement)
                .collect(Collectors.toList());
    }

    @Override
    public List<InstanceFieldReadingStatement> getInstanceFieldReadingStatements()
    {
        return allStatements.stream()
                .filter(abstractStatement -> abstractStatement instanceof InstanceFieldReadingStatement)
                .map(abstractStatement -> (InstanceFieldReadingStatement)abstractStatement)
                .collect(Collectors.toList());
    }

    @Override
    public List<StaticFieldAssignmentStatement> getStaticFieldAssignmentStatements()
    {
        return allStatements.stream()
                .filter(abstractStatement -> abstractStatement instanceof StaticFieldAssignmentStatement)
                .map(abstractStatement -> (StaticFieldAssignmentStatement)abstractStatement)
                .collect(Collectors.toList());
    }

    @Override
    public List<StaticFieldReadingStatement> getStaticFieldReadingStatements()
    {
        return allStatements.stream()
                .filter(abstractStatement -> abstractStatement instanceof StaticFieldReadingStatement)
                .map(abstractStatement -> (StaticFieldReadingStatement)abstractStatement)
                .collect(Collectors.toList());
    }

    @Override
    public List<MethodCallStatement> getMethodCallStatements()
    {
        return allStatements.stream()
                .filter(abstractStatement -> abstractStatement instanceof MethodCallStatement)
                .map(abstractStatement -> (MethodCallStatement)abstractStatement)
                .collect(Collectors.toList());
    }

    @Override
    public List<GetterCallStatement> getGetterCallStatements()
    {
        return allStatements.stream()
                .filter(abstractStatement -> abstractStatement instanceof GetterCallStatement)
                .map(abstractStatement -> (GetterCallStatement)abstractStatement)
                .collect(Collectors.toList());
    }

    @Override
    public List<SetterCallStatement> getSetterCallStatements()
    {
        return allStatements.stream()
                .filter(abstractStatement -> abstractStatement instanceof SetterCallStatement)
                .map(abstractStatement -> (SetterCallStatement)abstractStatement)
                .collect(Collectors.toList());
    }
}
