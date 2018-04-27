package org.tha3rav.rimaz.core.ArchitecturalLayers;

import org.tha3rav.rimaz.apk.ApkTypeEntry;
import org.tha3rav.rimaz.core.ArchitecturalLayers.EventHandling.Event;
import org.tha3rav.rimaz.core.OOP.Tuple;
import org.tha3rav.rimaz.core.Statements.AbstractStatement;

import java.util.List;

public class EventModelsDirectManipulation extends AbstractEventModelsManipulation
{
    protected List<AbstractStatement> directStatements;

    public EventModelsDirectManipulation()
    {
        super();
    }

    public EventModelsDirectManipulation(Event event, List<AbstractStatement> directStatements)
    {
        super(event);
        this.directStatements = directStatements;
    }

    public List<AbstractStatement> getDirectStatements()
    {
        return directStatements;
    }

    public void setDirectStatements(List<AbstractStatement> directStatements)
    {
        this.directStatements = directStatements;
    }
}
