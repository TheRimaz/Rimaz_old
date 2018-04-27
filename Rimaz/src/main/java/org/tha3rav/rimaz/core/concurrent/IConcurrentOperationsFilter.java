package org.tha3rav.rimaz.core.concurrent;

import org.tha3rav.rimaz.core.OOP.Getter;

import java.util.List;

public interface IConcurrentOperationsFilter
{
    List<AbstractConcurrentOperation> filter();

    //Doesn't work properly, soot doesn't identify implemented interfaces in some cases
    List<RunnableConcurrentOperation> getRunnableConcurrentOperations();
    List<CallableConcurrentOperation> getCallableConcurrentOperations();
    List<AsyncTaskConcurrentOperation> getAsyncTaskConcurrentOperations();
}
