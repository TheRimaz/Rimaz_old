package org.tha3rav.rimaz.core.concurrent;

import org.tha3rav.rimaz.apk.ApkClass;
import org.tha3rav.rimaz.apk.ApkTypeEntry;
import org.tha3rav.rimaz.core.OOP.Method;
import org.tha3rav.rimaz.core.Statements.AbstractStatement;
import org.tha3rav.rimaz.core.Statements.InitializationStatement;
import org.tha3rav.rimaz.core.Statements.MethodCallStatement;

import java.util.List;
import java.util.Optional;

import soot.RefType;
import soot.SootClass;

import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.CONCURRENCY_TYPES.ASYNC_TASK;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.CONCURRENCY_TYPES.CALLABLE;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.CONCURRENCY_TYPES.RUNNABLE;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants
        .CONCURRENCY_TYPES_INVOCATION_METHODS.EXECUTE;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants
        .CONCURRENCY_TYPES_INVOCATION_METHODS.EXECUTE_ON_EXECUTOR;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.geInterfaceImplementationsHierarchically;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.getAllUpperTypes;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.getUpperHierarchy;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.isRefType;

public class ConcurrentOperationFactory extends ConcurrentOperationAbstractFactory
{

    private List<ApkClass> allClasses;

    public ConcurrentOperationFactory(List<ApkClass> allClasses)
    {
        this.allClasses = allClasses;
    }

    @Override
    public AbstractConcurrentOperation getConcurrentOperation(AbstractStatement abstractStatement)
    {
        AbstractConcurrentOperation concurrentOperation = null;

        if (isRunnableOperation(abstractStatement))
        {
            if (isRefType(((InitializationStatement) abstractStatement).getInitializingType()))
            {
                Optional<ApkClass> invokingObject = allClasses.stream()
                        .filter(apkClass -> apkClass
                                .getSootClass()
                                .equals(((RefType) ((InitializationStatement) abstractStatement).getInitializingType()).getSootClass()))

                        .findAny();

                if (invokingObject.isPresent())
                {
                    List<Method> executionMethods = invokingObject.get().getMethods();
                    concurrentOperation = new RunnableConcurrentOperation(invokingObject.get(),
                                                                          executionMethods);
                }
            }
        }
        else
        {
            if (isCallableOperation(abstractStatement))
            {
                if (isRefType(((InitializationStatement) abstractStatement).getInitializingType()))
                {
                    Optional<ApkClass> invokingObject = allClasses.stream().filter(apkClass ->
                                                                                           apkClass

                            .getSootClass().equals(((RefType) ((InitializationStatement) abstractStatement).getInitializingType()).getSootClass()))
                            .findAny();

                    if (invokingObject.isPresent())
                    {
                        List<Method> executionMethods = invokingObject.get().getMethods();
                        concurrentOperation = new CallableConcurrentOperation(invokingObject
                                                                                      .get(),
                                                                              executionMethods);
                    }
                }
            }
            else
            {
                if (isAsyncTaskOperation(abstractStatement))
                {
                    ApkTypeEntry invokingObject   = ((MethodCallStatement) abstractStatement)
                            .getInvokedObjectType();
                    List<Method> executionMethods = invokingObject.getMethods();
                    concurrentOperation = new AsyncTaskConcurrentOperation(invokingObject,
                                                                           executionMethods);
                }
            }
        }
        return concurrentOperation;
    }

    //Doesn't work properly, soot doesn't identify implemented interfaces in some cases
    @Override
    public boolean isRunnableOperation(AbstractStatement abstractStatement)
    {
        boolean isRunnableOperation;

        isRunnableOperation = isInstantiationOperation(abstractStatement, RUNNABLE);

        return isRunnableOperation;
    }

    @Override
    public boolean isCallableOperation(AbstractStatement abstractStatement)
    {
        boolean isCallableOperation;

        isCallableOperation = isInstantiationOperation(abstractStatement, CALLABLE);

        return isCallableOperation;
    }

    @Override
    public boolean isAsyncTaskOperation(AbstractStatement abstractStatement)
    {
        boolean isAsyncTaskOperation = false;

        boolean isMethodCallStatement = abstractStatement instanceof MethodCallStatement;

        if (isMethodCallStatement)
        {
            MethodCallStatement methodCallStatement = (MethodCallStatement)abstractStatement;

            ApkTypeEntry invokedObject = methodCallStatement.getInvokedObjectType();
            List<SootClass> invokedObjectAndHierarchy = getUpperHierarchy(invokedObject.getSootClass());
            boolean isAsyncTaskObject = invokedObjectAndHierarchy.stream()
                    .anyMatch(sootClass -> sootClass.getName().contains(ASYNC_TASK));

            if (isAsyncTaskObject)
            {
                boolean isCallingAsyncTaskExecuteMethod = methodCallStatement.getCalledMethod().getSootMethod().getName().equals(EXECUTE)||
                                            methodCallStatement.getCalledMethod().getSootMethod().getName().equals(EXECUTE_ON_EXECUTOR);

                if(isCallingAsyncTaskExecuteMethod)
                {
                    isAsyncTaskOperation = true;
                }
            }
        }
        return isAsyncTaskOperation;
    }

    private boolean isInstantiationOperation(AbstractStatement abstractStatement,
                                             String typeName)
    {
        boolean returnValue = false;
        if (abstractStatement instanceof InitializationStatement)
        {
            InitializationStatement initializationStatement       = (InitializationStatement)abstractStatement;
            ApkTypeEntry            invokedObjectType             = initializationStatement.getInvokedObjectType();
            List<SootClass>         invokedObjectTypeAndHierarchy = getAllUpperTypes(invokedObjectType.getSootClass());
            boolean implementsType = invokedObjectTypeAndHierarchy
                                            .stream()
                                            .anyMatch(sootClass -> sootClass.getName().contains(typeName));
            if(implementsType)
            {
                returnValue = true;
            }
        }
        return returnValue;
    }
}
