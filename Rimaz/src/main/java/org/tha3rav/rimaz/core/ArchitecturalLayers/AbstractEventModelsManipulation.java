package org.tha3rav.rimaz.core.ArchitecturalLayers;

import org.tha3rav.rimaz.apk.ApkTypeEntry;
import org.tha3rav.rimaz.core.ArchitecturalLayers.EventHandling.Event;
import org.tha3rav.rimaz.core.OOP.Tuple;
import org.tha3rav.rimaz.core.Statements.AbstractStatement;

import java.util.List;

public abstract class AbstractEventModelsManipulation
{
    protected Event                                             event;


    public AbstractEventModelsManipulation()
    {

    }

    public AbstractEventModelsManipulation(Event event)
    {
        this.event = event;
    }

    public Event getEvent()
    {
        return event;
    }

    public void setEvent(Event event)
    {
        this.event = event;
    }
}
