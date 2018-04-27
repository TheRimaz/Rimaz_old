package org.tha3rav.rimaz.core.ArchitecturalLayers.EventHandling;

import org.tha3rav.rimaz.apk.ApkTypeEntry;
import org.tha3rav.rimaz.core.OOP.Method;

import java.util.Arrays;
import java.util.List;

public class EventHandler extends Event
{
    protected boolean isLifeCycleEventHandler;
    private   Method  method;

    public EventHandler(ApkTypeEntry containingType)
    {
        super(containingType);
    }

    @Override
    public List<Method> getExecutionMethods()
    {
        return Arrays.asList(method);
    }

    public EventHandler(ApkTypeEntry containingType, Method method, boolean isLifeCycleEventHandler)
    {
        super(containingType);
        this.method = method;
        this.isLifeCycleEventHandler = isLifeCycleEventHandler;
    }

    public boolean getIsLifeCycleEventHandler()
    {
        return isLifeCycleEventHandler;
    }

    public void setIsLifeCycleEventHandler(boolean isLifeCycleEventHandler)
    {
        this.isLifeCycleEventHandler = isLifeCycleEventHandler;
    }


    public Method getMethod()
    {
        return method;
    }

    public void setMethod(Method method)
    {
        this.method = method;
    }
}
