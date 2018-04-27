package org.tha3rav.rimaz.core.concurrent;

import org.tha3rav.rimaz.apk.ApkClass;
import org.tha3rav.rimaz.core.Statements.AbstractStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConcurrentOperationsFilter implements IConcurrentOperationsFilter
{
    private List<AbstractStatement> allStatements;
    private List<ApkClass> allClasses;
    private List<AbstractConcurrentOperation> concurrentOperations;

    public ConcurrentOperationsFilter(List<AbstractStatement> allStatements,  List<ApkClass> allClasses)
    {
        this.allStatements = allStatements;
        this.allClasses = allClasses;
        ConcurrentOperationAbstractFactory factory = new ConcurrentOperationFactory(allClasses);
        concurrentOperations = allStatements.stream()
                .map(abstractStatement -> factory.getConcurrentOperation(abstractStatement))
                .collect(Collectors.toList());
    }

    @Override
    public List<AbstractConcurrentOperation> filter()
    {
        List<AbstractConcurrentOperation> filteredConcurrentOperations = new ArrayList<>();

        List<RunnableConcurrentOperation> runnableConcurrentOperations;
        List<CallableConcurrentOperation> callableConcurrentOperations;
        List<AsyncTaskConcurrentOperation> asyncTaskConcurrentOperations;

        runnableConcurrentOperations = getRunnableConcurrentOperations();
        callableConcurrentOperations = getCallableConcurrentOperations();
        asyncTaskConcurrentOperations = getAsyncTaskConcurrentOperations();

        filteredConcurrentOperations.addAll(runnableConcurrentOperations);
        filteredConcurrentOperations.addAll(callableConcurrentOperations);
        filteredConcurrentOperations.addAll(asyncTaskConcurrentOperations);

        return filteredConcurrentOperations;
    }

    //Doesn't work properly, soot doesn't identify implemented interfaces in some cases
    @Override
    public List<RunnableConcurrentOperation> getRunnableConcurrentOperations()
    {
        return concurrentOperations.stream()
                .filter(abstractConcurrentOperation -> abstractConcurrentOperation instanceof RunnableConcurrentOperation)
                .map(abstractConcurrentOperation -> (RunnableConcurrentOperation)abstractConcurrentOperation)
                .collect(Collectors.toList());
    }

    @Override
    public List<CallableConcurrentOperation> getCallableConcurrentOperations()
    {
        return concurrentOperations.stream()
                .filter(abstractConcurrentOperation -> abstractConcurrentOperation instanceof CallableConcurrentOperation)
                .map(abstractConcurrentOperation -> (CallableConcurrentOperation)abstractConcurrentOperation)
                .collect(Collectors.toList());
    }

    @Override
    public List<AsyncTaskConcurrentOperation> getAsyncTaskConcurrentOperations()
    {
        return concurrentOperations.stream()
                .filter(abstractConcurrentOperation -> abstractConcurrentOperation instanceof AsyncTaskConcurrentOperation)
                .map(abstractConcurrentOperation -> (AsyncTaskConcurrentOperation)abstractConcurrentOperation)
                .collect(Collectors.toList());
    }
}
