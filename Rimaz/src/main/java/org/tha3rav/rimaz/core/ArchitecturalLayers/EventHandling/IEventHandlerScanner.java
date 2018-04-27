package org.tha3rav.rimaz.core.ArchitecturalLayers.EventHandling;

import java.util.List;

import soot.SootClass;

public interface IEventHandlerScanner
{
    List<EventHandler> scanForEventHandlers(List<SootClass> eventListenerTypes, List<SootClass> nonUIElementEventListenerTypes);
}
