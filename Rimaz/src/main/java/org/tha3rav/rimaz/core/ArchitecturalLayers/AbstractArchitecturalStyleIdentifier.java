package org.tha3rav.rimaz.core.ArchitecturalLayers;

import org.tha3rav.rimaz.apk.ApkTypeEntry;
import org.tha3rav.rimaz.core.ArchitecturalLayers.EventHandling.Event;
import org.tha3rav.rimaz.core.OOP.AbstractMethod;
import org.tha3rav.rimaz.core.OOP.Method;
import org.tha3rav.rimaz.core.OOP.Tuple;
import org.tha3rav.rimaz.core.Statements.AbstractStatement;

import java.util.List;

public abstract class AbstractArchitecturalStyleIdentifier
{
    public abstract Tuple<Boolean, List<AbstractStatement>> manipulatesModelsDirectly(List<Method> executionMethods);

    public abstract Tuple<Boolean, Tuple<List<AbstractStatement>, List<AbstractMethod>>> manipulatesModelsViaSiblingMethods(Event event, List<Method> executionMethods);

    public abstract Tuple<Boolean, List<Tuple<List<AbstractStatement>, List<ApkTypeEntry>>>> manipulatesModelsViaHelperClassesMethods(Event event);

    public abstract AbstractArchitecturalStyle identifyArchitecturalStyle();

}
