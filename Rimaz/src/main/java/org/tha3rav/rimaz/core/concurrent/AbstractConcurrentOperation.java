package org.tha3rav.rimaz.core.concurrent;

import org.tha3rav.rimaz.apk.ApkTypeEntry;
import org.tha3rav.rimaz.core.OOP.AbstractMethod;
import org.tha3rav.rimaz.core.OOP.Method;

import java.util.List;

import soot.SootClass;

import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.CONCURRENCY_TYPES.ASYNC_TASK;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.CONCURRENCY_TYPES.CALLABLE;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.CONCURRENCY_TYPES.RUNNABLE;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.getAllUpperTypes;

public abstract class AbstractConcurrentOperation
{
    protected ApkTypeEntry invokingObject;
    protected List<Method> executionMethods;

    public AbstractConcurrentOperation(ApkTypeEntry invokingObject, List<Method> executionMethods)
    {
        this.invokingObject = invokingObject;
        this.executionMethods = executionMethods;
    }

    public ApkTypeEntry getInvokingObject()
    {
        return invokingObject;
    }

    public void setInvokingObject(ApkTypeEntry invokingObject)
    {
        this.invokingObject = invokingObject;
    }

    public List<Method> getExecutionMethods()
    {
        return executionMethods;
    }

    public void setExecutionMethods(List<Method> executionMethods)
    {
        this.executionMethods = executionMethods;
    }

    public static boolean isConcurrentOperationClass(ApkTypeEntry concurrentType)
    {
        List<SootClass> invokedObjectTypeAndHierarchy = getAllUpperTypes(concurrentType.getSootClass());
        boolean implementsType = invokedObjectTypeAndHierarchy
                .stream()
                .anyMatch(sootClass -> (sootClass.getName().contains(RUNNABLE))||
                                       (sootClass.getName().contains(CALLABLE))||
                                       (sootClass.getName().contains(ASYNC_TASK)));

        return implementsType;
    }
}
