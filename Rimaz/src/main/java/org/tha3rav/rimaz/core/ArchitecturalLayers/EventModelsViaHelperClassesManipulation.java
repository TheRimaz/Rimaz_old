package org.tha3rav.rimaz.core.ArchitecturalLayers;


import org.tha3rav.rimaz.apk.ApkTypeEntry;
import org.tha3rav.rimaz.core.ArchitecturalLayers.EventHandling.Event;
import org.tha3rav.rimaz.core.OOP.Tuple;
import org.tha3rav.rimaz.core.Statements.AbstractStatement;

import java.util.List;

public class EventModelsViaHelperClassesManipulation extends AbstractEventModelsManipulation
{
    protected List<Tuple<List<AbstractStatement>,List<ApkTypeEntry>>> manipulationStatements;

    public EventModelsViaHelperClassesManipulation()
    {
        super();
    }

    public EventModelsViaHelperClassesManipulation(Event event, List<Tuple<List<AbstractStatement>,List<ApkTypeEntry>>> manipulationStatements)
    {
        super(event);
        this.manipulationStatements = manipulationStatements;
    }

    public List<Tuple<List<AbstractStatement>,List<ApkTypeEntry>>> getManipulationStatements()
    {
        return manipulationStatements;
    }

    public void setManipulationStatements(List<Tuple<List<AbstractStatement>,List<ApkTypeEntry>>> manipulationStatements)
    {
        this.manipulationStatements = manipulationStatements;
    }
}
