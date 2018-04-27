package org.tha3rav.rimaz.core.ArchitecturalLayers.EventHandling;

import org.tha3rav.rimaz.apk.ApkClass;
import org.tha3rav.rimaz.apk.ApkTypeEntry;
import org.tha3rav.rimaz.core.OOP.AbstractMethod;
import org.tha3rav.rimaz.core.OOP.Method;

import java.util.List;

import soot.RefType;
import soot.SootClass;

public class NonUIElementEvent extends Event
{
    private AbstractMethod containingMethod;
    private ApkClass       listenerClass;
    private ApkTypeEntry   containingType;
    private SootClass listenerType;
    private List<Method> executionMethods;
    private ApkTypeEntry invokedObject;
    private boolean isCertain;

    public NonUIElementEvent(ApkTypeEntry containingType,
                             AbstractMethod containingMethod,
                             ApkClass listenerClass,
                             SootClass listenerType,
                             List<Method> executionMethods,
                             ApkTypeEntry invokedObject)
    {
        super(containingType);
        this.containingType = containingType;
        this.containingMethod = containingMethod;
        this.listenerType = listenerType;
        this.invokedObject = invokedObject;
        this.executionMethods = executionMethods;
        this.setListenerClass(listenerClass);
        this.isCertain = true;
    }

    public NonUIElementEvent(ApkTypeEntry containingType,
                             AbstractMethod containingMethod,
                             ApkClass listenerClass,
                             SootClass listenerType,
                             List<Method> executionMethods,
                             boolean isCertain,
                             ApkTypeEntry invokedObject)
    {
        super(containingType);
        this.containingType = containingType;
        this.containingMethod = containingMethod;
        this.listenerType = listenerType;
        this.executionMethods = executionMethods;
        this.invokedObject = invokedObject;
        this.setListenerClass(listenerClass);
        this.isCertain = isCertain;
    }

    @Override
    public List<Method> getExecutionMethods()
    {
        return executionMethods;
    }

    public AbstractMethod getContainingMethod()
    {
        return containingMethod;
    }

    public void setContainingMethod(AbstractMethod containingMethod)
    {
        this.containingMethod = containingMethod;
    }

    public ApkClass getListenerClass()
    {
        return listenerClass;
    }

    public void setListenerClass(ApkClass listenerClass)
    {
        this.listenerClass = listenerClass;
    }

    public SootClass getListenerType()
    {
        return listenerType;
    }

    public void setListenerType(SootClass listenerType)
    {
        this.listenerType = listenerType;
    }

    @Override
    public ApkTypeEntry getContainingType()
    {
        return containingType;
    }

    @Override
    public void setContainingType(ApkTypeEntry containingType)
    {
        this.containingType = containingType;
    }

    public boolean isCertain()
    {
        return isCertain;
    }

    public void setCertain(boolean certain)
    {
        isCertain = certain;
    }

    public ApkTypeEntry getInvokedObject()
    {
        return invokedObject;
    }

    public void setInvokedObject(ApkTypeEntry invokedObject)
    {
        this.invokedObject = invokedObject;
    }
}
