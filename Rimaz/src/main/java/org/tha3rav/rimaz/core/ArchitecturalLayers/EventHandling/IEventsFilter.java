package org.tha3rav.rimaz.core.ArchitecturalLayers.EventHandling;

import java.util.List;

import soot.SootClass;

public interface IEventsFilter
{
    List<Event> filter();

    List<InputEvent> getInputEvents();
    List<NonUIElementEvent> getNonUIElementEvents();
    List<EventHandler> getEventHandlers(List<SootClass> eventListenerTypes, List<SootClass> nonUIElementEvents);
}
