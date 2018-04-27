package org.tha3rav.rimaz.core.ArchitecturalLayers.EventHandling;

import org.tha3rav.rimaz.apk.ApkClass;
import org.tha3rav.rimaz.core.ArchitecturalLayers.ActiveClass;
import org.tha3rav.rimaz.core.ArchitecturalLayers.ActivityClass;
import org.tha3rav.rimaz.core.ArchitecturalLayers.UIElementClass;
import org.tha3rav.rimaz.core.OOP.Method;
import org.tha3rav.rimaz.exceptions.NotEventHandlerOverriderException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import soot.SootClass;

import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.EventHandlersPrefixes.ON;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidUtils.getEventHandlerInterfaces;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidUtils.getEventHandlerOverriders;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidUtils.getLifeCycleEventHandlers;
import static org.tha3rav.rimaz.utils.EXCEPTIONS.ExceptionMessages
        .NOT_EVENT_HANDLER_CLASS_ERROR_MESSAGE;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.getUpperHierarchy;

public class EventHandlerScanner implements IEventHandlerScanner
{
    private List<ApkClass> eventfulClasses;

    public EventHandlerScanner(List<ApkClass> eventfulClasses)
    {
        this.eventfulClasses = eventfulClasses.stream()
        .filter(apkClass -> apkClass instanceof ActiveClass)
        .filter(apkClass -> !(apkClass instanceof UIElementClass))
        .collect(Collectors.toList());
    }

    @Override
    public List<EventHandler> scanForEventHandlers(List<SootClass> eventListenerTypes, List<SootClass> nonUIElementEventListenerTypes)
    {
        List<EventHandler> eventHandlers = new ArrayList<>();

        List<Method> methods = new ArrayList<>();
        List<String> eventHandlerInterfaces = getEventHandlerInterfaces();
        List<String> eventHandlerOverriders = getEventHandlerOverriders();
        List<String> lifeCycleEventHandlersNames = getLifeCycleEventHandlers();

        List<Method> potentialMethods = new ArrayList<>();
        List<EventHandler> nonLifeCycleEventHandlers = new ArrayList<>();
        List<EventHandler> lifeCycleEventHandlers = new ArrayList<>();

        for (ApkClass eventfulClass: eventfulClasses)
        {
            methods = eventfulClass.getMethods();

            potentialMethods = methods.stream()
                    .filter(method -> isMethodNonPrivateNonStatic(method))
                    .filter(method -> isMethodInheritedFromEventHandlerInterface(method, eventHandlerInterfaces)||
                                      isJustAOnMethodInEventHandlerOverrider(method, eventHandlerOverriders))
                    .filter(method -> {
                                        if (method.overridesOrImplementsMethodInUpperType().getFirstParameter())
                                        {
                                            return (!eventListenerTypes.stream().anyMatch(sootClass -> sootClass.equals(method.overridesOrImplementsMethodInUpperType().getSecondParameter())))&&
                                                   (!nonUIElementEventListenerTypes.stream().anyMatch(sootClass -> sootClass.equals(method.overridesOrImplementsMethodInUpperType().getSecondParameter())));
                                        }
                                        else
                                        {
                                            return true;
                                        }
                    })
                    .collect(Collectors.toList());

            nonLifeCycleEventHandlers = potentialMethods.stream()
                    .filter(method -> !isLifeCycleEventHandler(method, lifeCycleEventHandlersNames))
                    .map(method -> new EventHandler(eventfulClass, method, false))
                    .collect(Collectors.toList());

            lifeCycleEventHandlers = potentialMethods.stream()
                    .filter(method -> isLifeCycleEventHandler(method, lifeCycleEventHandlersNames))
                    .map(method -> new EventHandler(eventfulClass, method, true))
                    .collect(Collectors.toList());

            eventHandlers.addAll(nonLifeCycleEventHandlers);
            eventHandlers.addAll(lifeCycleEventHandlers);
        }

        return eventHandlers;
    }

    protected boolean isMethodNonPrivateNonStatic(Method method)
    {
        return !method.getSootMethod().isPrivate() &&
               !method.getSootMethod().isStatic();
    }

    protected boolean isMethodInheritedFromEventHandlerInterface(Method method, List<String> eventHandlerInterfaces)
    {
        boolean isImplementedInterfaceAmongEventHandlerInterfaces = false;
        String implementedInterfaceName;
        if (method.overridesOrImplementsMethodInUpperType().getFirstParameter())
        {
            implementedInterfaceName = method.overridesOrImplementsMethodInUpperType()
                    .getSecondParameter()
                    .getName();
            isImplementedInterfaceAmongEventHandlerInterfaces = eventHandlerInterfaces.stream().anyMatch(interfaceName -> interfaceName.contains(implementedInterfaceName));
        }
        return isImplementedInterfaceAmongEventHandlerInterfaces;
    }

    protected boolean isJustAOnMethodInEventHandlerOverrider(Method method, List<String> eventHandlerOverriders)
    {
        boolean isMethodInEventHandlerOverrider = false;
        try
        {
            isMethodInEventHandlerOverrider = isMethodInEventHandlerOverrider(method, eventHandlerOverriders);
        }
        catch (NotEventHandlerOverriderException notEventHandlerOverriderException)
        {
            System.out.println(notEventHandlerOverriderException.getMessage());
        }

        if (!isMethodInEventHandlerOverrider)
        {
            return false;
        }
        return method.getSootMethod().getName().toLowerCase().startsWith(ON);
    }

    private boolean isMethodInEventHandlerOverrider(Method method ,List<String> eventHandlerOverriders) throws NotEventHandlerOverriderException
    {
        boolean isMethodInEventHandlerOverrider;

        isMethodInEventHandlerOverrider = eventHandlerOverriders.stream()
                .anyMatch(eventHandlerOverrider -> getUpperHierarchy(method.getSootMethod().getDeclaringClass())
                        .stream()
                        .anyMatch(superClass -> superClass.getName().contains(eventHandlerOverrider)));
        if (!isMethodInEventHandlerOverrider)
        {
            throw new NotEventHandlerOverriderException(String.format(NOT_EVENT_HANDLER_CLASS_ERROR_MESSAGE, method.getSootMethod().getDeclaringClass()));
        }
        return isMethodInEventHandlerOverrider;
    }

    protected boolean isLifeCycleEventHandler(Method method, List<String> lifeCycleEventHandlers)
    {
        return lifeCycleEventHandlers.stream().anyMatch(lifeCycleEventHandler -> method.getSootMethod().getName().equals(lifeCycleEventHandler));
    }
}
