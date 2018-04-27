package org.tha3rav.rimaz.core.ArchitecturalLayers.EventHandling;

import org.tha3rav.rimaz.apk.ApkClass;
import org.tha3rav.rimaz.core.AndroidSpecific.BuildConfigClass;
import org.tha3rav.rimaz.core.AndroidSpecific.RClass;
import org.tha3rav.rimaz.core.AndroidSpecific.RInnerClass;
import org.tha3rav.rimaz.exceptions.NotEventHandlerOverriderException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import soot.SootClass;

public class EventsFilter implements IEventsFilter
{
    private List<ApkClass> apkClasses;
    private List<ApkClass> eventfulTypes;
    private InputEventScanner  inputEventScanner;
    private NonUIElementEventsScanner nonUIElementEventScanner;
    private EventHandlerScanner  eventHandlerScanner;


    public EventsFilter(List<ApkClass> apkClasses, List<ApkClass> eventfulTypes)
    {
        this.apkClasses = apkClasses;
        this.eventfulTypes = eventfulTypes;
        inputEventScanner = new InputEventScanner(apkClasses);
        nonUIElementEventScanner = new NonUIElementEventsScanner(apkClasses);
        eventHandlerScanner = new EventHandlerScanner(eventfulTypes);
    }

    @Override
    public List<Event> filter()
    {
        List<Event> events = new ArrayList<>();
        List<InputEvent> inputEvents = getInputEvents();
        List<NonUIElementEvent> nonUIElementEvents = getNonUIElementEvents();
        List<SootClass> eventListenerTypes = inputEvents.parallelStream().map(inputEvent -> inputEvent.getListenerType()).collect(Collectors.toList());
        List<SootClass> nonUIElementEventListenerTypes = nonUIElementEvents.parallelStream().map(nonUIElementEvent -> nonUIElementEvent.getListenerType()).collect(Collectors.toList());
        List<EventHandler> eventHandlers = getEventHandlers(eventListenerTypes, nonUIElementEventListenerTypes);

        events.addAll(inputEvents);
        events.addAll(nonUIElementEvents);
        events.addAll(eventHandlers);

        return events;
    }

    @Override
    public List<InputEvent> getInputEvents()
    {
        List<InputEvent> inputEvents = new ArrayList<>();

        eventfulTypes.forEach(eventfulType -> inputEvents.addAll(inputEventScanner.scanForInputEvents(eventfulType)));

        return inputEvents;
    }

    @Override
    public List<NonUIElementEvent> getNonUIElementEvents()
    {
        List<NonUIElementEvent> nonUIElementEvents = new ArrayList<>();

        apkClasses.forEach(targetedClass -> nonUIElementEvents.addAll(nonUIElementEventScanner.scanForNonUIElementEvents(targetedClass)));

        return nonUIElementEvents;
    }

    @Override
    public List<EventHandler> getEventHandlers(List<SootClass> eventListenerTypes, List<SootClass> nonUIElementEventListenerTypes)
    {
        return eventHandlerScanner.scanForEventHandlers(eventListenerTypes, nonUIElementEventListenerTypes);
    }
}
