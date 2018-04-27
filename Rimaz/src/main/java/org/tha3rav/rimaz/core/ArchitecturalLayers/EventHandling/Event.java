package org.tha3rav.rimaz.core.ArchitecturalLayers.EventHandling;

import org.tha3rav.rimaz.apk.ApkTypeEntry;
import org.tha3rav.rimaz.core.OOP.Method;

import java.util.List;

public abstract class Event
{
    protected ApkTypeEntry containingType;

    public Event(ApkTypeEntry containingType)
    {
        this.containingType = containingType;
    }

    public ApkTypeEntry getContainingType()
    {
        return containingType;
    }

    public void setContainingType(ApkTypeEntry containingType)
    {
        this.containingType = containingType;
    }

    public abstract List<Method> getExecutionMethods();
}
