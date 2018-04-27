package org.tha3rav.rimaz.core.ArchitecturalLayers;

import org.tha3rav.rimaz.apk.ApkTypeEntry;
import org.tha3rav.rimaz.core.ArchitecturalLayers.EventHandling.Event;
import org.tha3rav.rimaz.core.OOP.AbstractMethod;
import org.tha3rav.rimaz.core.OOP.Tuple;
import org.tha3rav.rimaz.core.Statements.AbstractStatement;

import java.util.List;

public class EventModelsViaSiblingMethodsManipulation extends AbstractEventModelsManipulation
{
    protected List<AbstractMethod> calledSiblingMethods;
    protected List<AbstractStatement> manipulationStatements;

    public EventModelsViaSiblingMethodsManipulation()
    {
        super();
    }

    public EventModelsViaSiblingMethodsManipulation(Event event,
                                                    List<AbstractStatement> manipulationStatements,
                                                    List<AbstractMethod> calledSiblingMethods)
    {
        super(event);
        this.manipulationStatements = manipulationStatements;
        this.calledSiblingMethods = calledSiblingMethods;
    }

    public List<AbstractMethod> getCalledSiblingMethods()
    {
        return calledSiblingMethods;
    }

    public void setCalledSiblingMethods(List<AbstractMethod> calledSiblingMethods)
    {
        this.calledSiblingMethods = calledSiblingMethods;
    }

    public List<AbstractStatement> getManipulationStatements()
    {
        return manipulationStatements;
    }

    public void setManipulationStatements(List<AbstractStatement> manipulationStatements)
    {
        this.manipulationStatements = manipulationStatements;
    }
}
