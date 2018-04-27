package org.tha3rav.rimaz.core.ArchitecturalLayers;

import org.tha3rav.rimaz.apk.ApkClass;
import org.tha3rav.rimaz.apk.ApkResourceFile;
import org.tha3rav.rimaz.apk.ApkTypeEntry;
import org.tha3rav.rimaz.core.AndroidSpecific.AndroidGeneratedClass;
import org.tha3rav.rimaz.core.AndroidSpecific.BRClass;
import org.tha3rav.rimaz.core.AndroidSpecific.BuildConfigClass;
import org.tha3rav.rimaz.core.AndroidSpecific.DataBindingClass;
import org.tha3rav.rimaz.core.AndroidSpecific.RClass;
import org.tha3rav.rimaz.core.AndroidSpecific.RInnerClass;
import org.tha3rav.rimaz.apk.AnimationFile;
import org.tha3rav.rimaz.core.ArchitecturalLayers.EventHandling.Event;
import org.tha3rav.rimaz.core.ArchitecturalLayers.EventHandling.EventHandler;
import org.tha3rav.rimaz.core.ArchitecturalLayers.EventHandling.EventsFilter;
import org.tha3rav.rimaz.core.ArchitecturalLayers.EventHandling.InputEvent;
import org.tha3rav.rimaz.core.ArchitecturalLayers.EventHandling.NonUIElementEvent;
import org.tha3rav.rimaz.core.ArchitecturalLayers.Model.Model;
import org.tha3rav.rimaz.core.Helper.HelperClass;
import org.tha3rav.rimaz.apk.LayoutFile;
import org.tha3rav.rimaz.core.OOP.AbstractMethod;
import org.tha3rav.rimaz.core.OOP.Field;
import org.tha3rav.rimaz.core.OOP.Method;
import org.tha3rav.rimaz.core.OOP.Tuple;
import org.tha3rav.rimaz.core.Statements.AbstractStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import soot.SootClass;
import soot.SootField;
import soot.Unit;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JInvokeStmt;
import soot.jimple.internal.JSpecialInvokeExpr;
import soot.jimple.internal.JStaticInvokeExpr;

import static org.tha3rav.rimaz.core.concurrent.AbstractConcurrentOperation
        .isConcurrentOperationClass;
import static org.tha3rav.rimaz.utils.OOP.OOPConstants.Numbers.THREE;
import static org.tha3rav.rimaz.utils.OOP.OOPConstants.Numbers.ZERO;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.getAllUpperTypes;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.isAssignmentStatement;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.isIndesirableGeneratedClass;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.isInvocationStatement;


public class ArchitecturalStyleIdentifier extends AbstractArchitecturalStyleIdentifier
{
    private List<ApkClass>     allClassesExceptModels;
    private List<ApkClass>     modelClasses;
    private List<ApkTypeEntry> allApkTypeEntries;
    private List<ApkResourceFile> resourceFiles;
    private List<ApkClass>     filteredClasses;
    private List<Event>        events;

    public ArchitecturalStyleIdentifier(List<Model> modelClasses,
                                        List<ApkClass> filteredClasses,
                                        List<ApkTypeEntry> allApkTypeEntries,
                                        List<ApkResourceFile> resourceFiles)
    {
        this.modelClasses = modelClasses.stream().map(model -> (ApkClass)model).collect(Collectors.toList());

        this.filteredClasses = filteredClasses.stream()
                .collect(Collectors.toList());
        this.allApkTypeEntries = allApkTypeEntries;
        this.resourceFiles = resourceFiles;

        this.allClassesExceptModels =  this.filteredClasses.stream()
                .filter(apkClass -> !modelClasses.contains(apkClass))
                .collect(Collectors.toList());

        List<ApkClass> eventfulClasses = getEventfulClasses();
        EventsFilter eventsFilter = new EventsFilter(this.filteredClasses, eventfulClasses);
        events = eventsFilter.filter();
    }

    @Override
    public Tuple<Boolean, List<AbstractStatement>> manipulatesModelsDirectly(List<Method> executionMethods)
    {
        Tuple<Boolean,List<AbstractStatement>> modelsDirectManipulation = new Tuple<>(false, new ArrayList<>());

        boolean manipulatesModels;
        List<AbstractStatement> manipulatesModelsStatements =  new ArrayList<>();

        manipulatesModels = executionMethods.stream()
                .anyMatch(method -> method.hasAssignmentOrReadingManipulations(modelClasses).getFirstParameter());

        executionMethods.stream()
                .filter(method -> method.hasAssignmentOrReadingManipulations(modelClasses).getFirstParameter())
                .forEach(method -> manipulatesModelsStatements.addAll(method.hasAssignmentOrReadingManipulations(modelClasses).getSecondParameter()));

        modelsDirectManipulation.setFirstParameter(manipulatesModels);
        modelsDirectManipulation.setSecondParameter(manipulatesModelsStatements);

        return modelsDirectManipulation;
    }

    @Override
    public Tuple<Boolean, Tuple<List<AbstractStatement>, List<AbstractMethod>>> manipulatesModelsViaSiblingMethods(Event event, List<Method> executionMethods)
    {
        Tuple<Boolean, Tuple<List<AbstractStatement>,List<AbstractMethod>>> modelsManipulation = new Tuple<>(false, new Tuple<>(new ArrayList<>(), new ArrayList<>()));

        boolean manipulatesModels;
        List<AbstractStatement> manipulatesModelsStatements = new ArrayList<>();
        List<MethodIncomingHierarchy> calledMethods = new ArrayList<>();
        List<ApkTypeEntry> containingClasses = new ArrayList<>();

        for (Method method: executionMethods)
        {
            if (event instanceof EventHandler)
            {
                containingClasses = Arrays.asList((ApkClass)event.getContainingType());
            }
            else
            {
                if(event instanceof InputEvent)
                {
                    containingClasses = Arrays.asList((ApkClass)event.getContainingType(), ((InputEvent)event).getListenerClass());
                }
            }

            calledMethods.addAll(method.findAllCalledMethods(containingClasses,
                                                             filteredClasses)
                                         .stream()
                                         .distinct()
                                         .map(abstractMethod -> new MethodIncomingHierarchy(abstractMethod, method))
                                         .collect(Collectors.toList()));
        }

        calledMethods = getCalledMethodsTree(calledMethods, containingClasses,
                                             filteredClasses, events);

        manipulatesModels = calledMethods.stream()
                .anyMatch(method -> method.actualMethod.hasAssignmentOrReadingManipulations(modelClasses).getFirstParameter());

        List<AbstractMethod> calledMethodsManipulatingModels = calledMethods.stream()
                .map(methodIncomingHierarchy -> methodIncomingHierarchy.actualMethod)
                .filter(method -> method.hasAssignmentOrReadingManipulations(modelClasses).getFirstParameter())
                .collect(Collectors.toList());

        calledMethodsManipulatingModels.forEach(method -> manipulatesModelsStatements.addAll(method.hasAssignmentOrReadingManipulations(modelClasses).getSecondParameter()));

        modelsManipulation.setFirstParameter(manipulatesModels);
        modelsManipulation.setSecondParameter(new Tuple<>(manipulatesModelsStatements, calledMethodsManipulatingModels));

        return modelsManipulation;
    }

    @Override
    public Tuple<Boolean, List<Tuple<List<AbstractStatement>, List<ApkTypeEntry>>>> manipulatesModelsViaHelperClassesMethods(Event event)
    {
        boolean manipulatesModels;
        Tuple<Boolean, List<Tuple<List<AbstractStatement>, List<ApkTypeEntry>>>> modelsManipulation = new Tuple<>(false, new ArrayList<>());
        List<Tuple<List<AbstractStatement>, List<ApkTypeEntry>>> manipulatesModelsStatements =  new ArrayList<>();

        List<MethodIncomingHierarchy> calledMethods = getDirectCalledMethodsOfEvent(event);

        calledMethods =  getCalledMethodsTree(calledMethods,
                                              allClassesExceptModels.stream().map(apkClass -> (ApkTypeEntry)apkClass).collect(Collectors.toList()),
                                              filteredClasses, events);

        List<MethodIncomingHierarchy> calledMethodsManipulatingModels = calledMethods.stream()
                .filter(method -> method.actualMethod.hasAssignmentOrReadingManipulations(modelClasses).getFirstParameter())
                .collect(Collectors.toList());

        //Remove called methods if they belong to the same class of the Event
        //calledMethodsManipulatingModels.removeIf(calledMethod -> calledMethod.actualMethod.getContainingType().getSootClass().equals(event.getContainingType().getSootClass()));
        //Removing is actually a bad idea

        calledMethodsManipulatingModels
                .forEach(method -> manipulatesModelsStatements.add(new Tuple<List<AbstractStatement>, List<ApkTypeEntry>> (method.actualMethod.hasAssignmentOrReadingManipulations(modelClasses).getSecondParameter(),
                         method.getPredecessorCallers().stream().map(abstractMethod -> abstractMethod.getContainingType()).collect(Collectors.toList()))));

        manipulatesModels = calledMethodsManipulatingModels.size() != ZERO;

        modelsManipulation.setFirstParameter(manipulatesModels);
        modelsManipulation.setSecondParameter(manipulatesModelsStatements);

        return modelsManipulation;
    }

    private List<MethodIncomingHierarchy> getDirectCalledMethodsOfEvent(Event event)
    {
        List<MethodIncomingHierarchy> calledMethods = new ArrayList<>();

        List<MethodIncomingHierarchy> executionMethods = event.getExecutionMethods().stream()
                .map(method -> (AbstractMethod)method)
                .map(abstractMethod ->{
                    MethodIncomingHierarchy methodIncomingHierarchy = null;
                    if(event instanceof EventHandler)
                    {

                        methodIncomingHierarchy = new MethodIncomingHierarchy(abstractMethod);
                    }
                    else
                    {
                        if(event instanceof InputEvent)
                        {
                            methodIncomingHierarchy = new MethodIncomingHierarchy(abstractMethod,((InputEvent) event).getContainingMethod());
                        }
                    }
                    return methodIncomingHierarchy;
                })
                .collect(Collectors.toList());

        for (MethodIncomingHierarchy executionMethod : executionMethods)
        {
            calledMethods.addAll(executionMethod.getActualMethod().findAllCalledMethods(filteredClasses.stream().map(apkClass -> (ApkTypeEntry)apkClass).collect(Collectors.toList()),
                                                                                        filteredClasses)
                                         .stream()
                                         .distinct()
                                         .map(abstractMethod -> new MethodIncomingHierarchy(abstractMethod, executionMethod.getPredecessorCallers()))
                                         .collect(Collectors.toList()));
        }

        /*List<MethodIncomingHierarchy> invokedEventsExecutionMethods = events.stream()
                .filter(invokedEvent -> invokedEvent instanceof InputEvent)
                .filter(invokedEvent -> calledMethods.stream()
                        .anyMatch(calledMethod -> calledMethod.getActualMethod().getSootMethod().equals(((InputEvent) invokedEvent).getContainingMethod().getSootMethod())))
                .map(invokedEvent -> invokedEvent.getExecutionMethods()
                    .stream().map(method -> new MethodIncomingHierarchy(method,calledMethods.stream()
                                .filter(calledMethod -> calledMethod.getActualMethod().getSootMethod().equals(((InputEvent) invokedEvent).getContainingMethod().getSootMethod()))
                                .findFirst().get().getPredecessorCallers())).collect(Collectors.toList()))
                .flatMap(invokedEventExecutionMethods -> invokedEventExecutionMethods.stream())
                .collect(Collectors.toList());*/

        List<MethodIncomingHierarchy> concurrentOperationsExecutionMethods = executionMethods.stream()
                .map(executionMethod -> executionMethod.actualMethod.retrieveConcurrentOperations(filteredClasses)
                    .stream().map(concurrentOperation -> concurrentOperation.getExecutionMethods().stream()
                                 .map(method -> new MethodIncomingHierarchy(method, executionMethod.predecessorCallers))
                                 .collect(Collectors.toList()))
                        .flatMap(methodIncomingHierarchies -> methodIncomingHierarchies.stream())
                    .collect(Collectors.toList()))
                .flatMap(abstractConcurrentOperations -> abstractConcurrentOperations.stream())
                .collect(Collectors.toList());

        List<MethodIncomingHierarchy> nonUIElementsEventsExecutionMethods = events.stream()
                .filter(invokedEvent -> invokedEvent instanceof NonUIElementEvent)
                .filter(invokedEvent -> executionMethods.stream()
                        .anyMatch(executionMethod -> executionMethod.getActualMethod().getSootMethod().equals(((NonUIElementEvent) invokedEvent).getContainingMethod().getSootMethod())))
                .map(invokedEvent -> invokedEvent.getExecutionMethods()
                        .stream().map(method -> new MethodIncomingHierarchy(method, executionMethods.stream()
                                .filter(calledMethod -> calledMethod.getActualMethod().getSootMethod().equals(((NonUIElementEvent) invokedEvent).getContainingMethod().getSootMethod()))
                                .findFirst().get().getPredecessorCallers())).collect(Collectors.toList()))
                .flatMap(invokedEventExecutionMethods -> invokedEventExecutionMethods.stream())
                .collect(Collectors.toList());


        //calledMethods.addAll(invokedEventsExecutionMethods);
        calledMethods.addAll(concurrentOperationsExecutionMethods);
        calledMethods.addAll(nonUIElementsEventsExecutionMethods);
        return calledMethods;
    }

    //Also supports execution methods of set event listeners
    private List<MethodIncomingHierarchy> getCalledMethodsTree(List<MethodIncomingHierarchy> calledMethods,
                                                               List<ApkTypeEntry> targetedClasses,
                                                               List<ApkClass> allClasses,
                                                               List<Event> events)
    {
        List<MethodIncomingHierarchy> currentLevelOfCalledMethods = calledMethods;

        List<MethodIncomingHierarchy> nextLevelOfCalledMethods    = new ArrayList<>();

        // Construct/Explore the outer call graph of methods by doing A Breadth-First-Search
        // Breadth-First-Search was chosen to anticipate the methods that can be called again by
        // other methods in a deeper level.

        do
        {
            calledMethods.addAll(nextLevelOfCalledMethods);

            //Notice here the effectiveness of Functional programming !!!

            nextLevelOfCalledMethods = currentLevelOfCalledMethods.stream()
                    .map(abstractMethod -> Stream.of(abstractMethod.actualMethod.findAllCalledMethods(targetedClasses, allClasses).stream()
                                                     .map(calledMethod -> new MethodIncomingHierarchy(calledMethod, abstractMethod.predecessorCallers)),

                                                     /*findInvokedInputEventsExecutionMethods(abstractMethod.actualMethod,events).stream()
                                                     .map(method -> new MethodIncomingHierarchy(method,abstractMethod.predecessorCallers)),*/

                                                     findConcurrentOperationsExecutionMethods(abstractMethod.actualMethod).stream()
                                                     .map(method -> new MethodIncomingHierarchy(method,abstractMethod.predecessorCallers)),

                                                     findNonUIElementsEventsExecutionMethods(abstractMethod.actualMethod,events).stream()
                                                             .map(method -> new MethodIncomingHierarchy(method,abstractMethod.predecessorCallers)))
                            .reduce(Stream::concat)
                            .orElseGet(Stream::empty)
                            .distinct()
                            .collect(Collectors.toList()))
                    .flatMap(abstractMethods -> abstractMethods.stream())
                    .filter(calledMethod -> !calledMethods.stream().anyMatch(methodIncomingHierarchy -> methodIncomingHierarchy
                            .getActualMethod()
                            .getSootMethod()
                            .equals(calledMethod.getActualMethod().getSootMethod())))
                    .distinct()
                    .collect(Collectors.toList());

            currentLevelOfCalledMethods = nextLevelOfCalledMethods;

        }while (currentLevelOfCalledMethods.size() != ZERO);

        return calledMethods;
    }

    private List<Tuple<Event, List<AbstractEventModelsManipulation>>> getAllModelsManipulations()
    {
        List<Tuple<Event, List<AbstractEventModelsManipulation>>> eventModelsManipulations = new ArrayList<>();
        List<Event> InputEventsAndEventHandlersOnly = events.stream().filter(event -> !(event instanceof NonUIElementEvent)).collect(Collectors.toList());

        InputEventsAndEventHandlersOnly
                .stream()
                .forEach(event ->
                         {
                             List<AbstractEventModelsManipulation> eventManipulations = getEventModelsManipulations(event);

                             if (eventManipulations.size() != 0)
                             {
                                 eventModelsManipulations.add(new Tuple<>(event, eventManipulations));
                             }
                         });

        return eventModelsManipulations;
    }

    private List<AbstractEventModelsManipulation> getEventModelsManipulations(Event event)
    {
        List<AbstractEventModelsManipulation> eventManipulations;
        List<Method> executionMethods;
        Tuple<Boolean, List<AbstractStatement>> eventModelsDirectManipulation;
        Tuple<Boolean, Tuple<List<AbstractStatement>, List<AbstractMethod>>> eventModelsManipulationViaSiblingMethods;
        Tuple<Boolean, List<Tuple<List<AbstractStatement>, List<ApkTypeEntry>>>> eventModelsManipulationViaHelperClassesMethods;
        eventManipulations = new ArrayList<>();
        executionMethods = event.getExecutionMethods();

        eventModelsDirectManipulation = manipulatesModelsDirectly(executionMethods);

        if (eventModelsDirectManipulation.getFirstParameter())
        {
            eventManipulations.add(new EventModelsDirectManipulation(event, eventModelsDirectManipulation.getSecondParameter()));
        }

        eventModelsManipulationViaSiblingMethods = manipulatesModelsViaSiblingMethods(event, executionMethods);

        if (eventModelsManipulationViaSiblingMethods.getFirstParameter())
        {
            eventManipulations.add(new EventModelsViaSiblingMethodsManipulation(event,
                                                                                eventModelsManipulationViaSiblingMethods.getSecondParameter().getFirstParameter(),
                                                                                eventModelsManipulationViaSiblingMethods.getSecondParameter().getSecondParameter()));
        }

        eventModelsManipulationViaHelperClassesMethods = manipulatesModelsViaHelperClassesMethods(event);

        if (eventModelsManipulationViaHelperClassesMethods.getFirstParameter())
        {
            eventManipulations.add(new EventModelsViaHelperClassesManipulation(event, eventModelsManipulationViaHelperClassesMethods.getSecondParameter()));
        }
        return eventManipulations;
    }

    @Override
    public AbstractArchitecturalStyle identifyArchitecturalStyle()
    {
        List<ApkClass> interactionClasses = filteredClasses.stream()
                .filter(apkClass -> apkClass instanceof ActiveClass)
                .filter(apkClass -> !(apkClass instanceof ApplicationClass))
                .collect(Collectors.toList());

        List<View> views =  Stream.concat(interactionClasses.stream()
                                                  .map(apkTypeEntry -> new View(apkTypeEntry)),
                                          resourceFiles.stream()
                                                  .filter(apkResourceFile -> (apkResourceFile instanceof LayoutFile)||
                                                                             (apkResourceFile instanceof AnimationFile))
                                                  .map(apkResourceFile -> new View(apkResourceFile)))
                .collect(Collectors.toList());

        if (modelClasses == null)
        {
            return new NoArchitecturalStyle(views, null, null);
        }
        if (modelClasses.size() == 0)
        {
            return new NoArchitecturalStyle(views, null, null);
        }

        //Architectural style to be identified and returned by this method
        AbstractArchitecturalStyle abstractArchitecturalStyle = null;

        //Get list of event, each with with its model manipulations
        List<Tuple<Event, List<AbstractEventModelsManipulation>>> allEventsModelsManipulations =
                getAllModelsManipulations();

        //Get list of model manipulations of input events only
        List<Tuple<Event, List<AbstractEventModelsManipulation>>> inputEventsModelsManipulations = allEventsModelsManipulations.stream()
                .filter(eventListTuple -> eventListTuple.getFirstParameter() instanceof InputEvent)
                .collect(Collectors.toList());

        //Filter input events manipulations in all Activity, Fragment, Dialog or Application class
        List<Tuple<Event, List<AbstractEventModelsManipulation>>> activeClassesWithEventsManipulatingModels = inputEventsModelsManipulations
                .parallelStream()
                .filter(event -> (event.getFirstParameter().getContainingType() instanceof ActiveClass))
                .collect(Collectors.toList());

        //Filter input events manipulations in all helper classes and databinding classes
        List<Tuple<Event, List<AbstractEventModelsManipulation>>> helperAndDataBindingClassesWithEventsManipulatingModels = inputEventsModelsManipulations
                .parallelStream()
                .filter(event -> ((event.getFirstParameter().getContainingType() instanceof HelperClass)&&
                                  !(isIndesirableGeneratedClass(event.getFirstParameter().getContainingType().getSootClass())))||
                                 (event.getFirstParameter().getContainingType() instanceof DataBindingClass))
                .collect(Collectors.toList());

        int numberOfModelsManipulationsByActiveClasses = activeClassesWithEventsManipulatingModels.size();
        int numberOfModelsManipulationsByHelperAndDataBindingClasses = helperAndDataBindingClassesWithEventsManipulatingModels.size();

        if (numberOfModelsManipulationsByActiveClasses >= numberOfModelsManipulationsByHelperAndDataBindingClasses)
        {

            // Get all manipulations by active classes
            List<AbstractEventModelsManipulation> allManipulationsByActiveClasses = allEventsModelsManipulations
                    .parallelStream()
                    .filter(event -> (event.getFirstParameter().getContainingType() instanceof ActiveClass))
                    .map(eventListTuple -> eventListTuple.getSecondParameter())
                    .flatMap(abstractEventModelsManipulations -> abstractEventModelsManipulations.stream())
                    .collect(Collectors.toList());

            // Get all direct manipulations
            List<AbstractEventModelsManipulation> directManipulations = allManipulationsByActiveClasses
                    .parallelStream()
                    .filter(abstractEventModelsManipulation -> abstractEventModelsManipulation instanceof EventModelsDirectManipulation)
                    .collect(Collectors.toList());

            // Get all via-siblingMethods manipulations
            List<AbstractEventModelsManipulation> viaSiblingMethodsManipulations = allManipulationsByActiveClasses
                    .parallelStream()
                    .filter(abstractEventModelsManipulation -> abstractEventModelsManipulation instanceof EventModelsViaSiblingMethodsManipulation)
                    .collect(Collectors.toList());

            // Get all via-helperClasses manipulations
            List<AbstractEventModelsManipulation> viaHelperClassesManipulations = allManipulationsByActiveClasses
                    .parallelStream()
                    .filter(abstractEventModelsManipulation -> abstractEventModelsManipulation instanceof EventModelsViaHelperClassesManipulation)
                    .collect(Collectors.toList());

            List<ApkClass> helperCandidates = viaHelperClassesManipulations.parallelStream()
                    .map(abstractEventModelsManipulation -> ((EventModelsViaHelperClassesManipulation)abstractEventModelsManipulation))
                    .map(eventModelsViaHelperClassesManipulation -> eventModelsViaHelperClassesManipulation.getManipulationStatements())
                    .flatMap(tuples -> tuples.stream())
                    .map(listListTuple -> listListTuple.getSecondParameter())
                    .flatMap(apkTypeEntries -> apkTypeEntries.stream())
                    .filter(apkTypeEntry -> (apkTypeEntry instanceof HelperClass)&&
                                            !isListenerClassOfEvent(apkTypeEntry, events)&&
                                            !isConcurrentOperationClass(apkTypeEntry))
                    .map(apkTypeEntry -> (ApkClass)apkTypeEntry)
                    .distinct()
                    .collect(Collectors.toList());

            int numberOfDirectManipulations = directManipulations.size();
            int numberOfViaSiblingMethodsManipulations = viaSiblingMethodsManipulations.size();
            int numberOfViaHelperClassesManipulations = viaHelperClassesManipulations.size();

            if (numberOfDirectManipulations + numberOfViaSiblingMethodsManipulations > numberOfViaHelperClassesManipulations)
            {

                List<Controller> controllers =
                        Stream.concat(interactionClasses.stream()
                                              .map(activeClass -> new Controller(activeClass)),
                                      helperCandidates.stream()
                                              .map(apkTypeEntry -> new Controller(apkTypeEntry)))
                        .collect(Collectors.toList());


                views =  Stream.concat(allApkTypeEntries.stream()
                                                          .filter(apkTypeEntry -> (apkTypeEntry instanceof UIElementClass))
                                                          .map(apkTypeEntry -> new View(apkTypeEntry)),
                                                  resourceFiles.stream()
                                                          .filter(apkResourceFile -> (apkResourceFile instanceof LayoutFile)||
                                                                                     (apkResourceFile instanceof AnimationFile))
                                                          .map(apkResourceFile -> new View(apkResourceFile)))
                                          .collect(Collectors.toList());

                abstractArchitecturalStyle = new MVCArchitecturalStyle(views,
                                                                      controllers,
                                                                      modelClasses.stream().map(model -> (Model)model).collect(Collectors.toList()));
            }
            else
            {

                List<ApkClass> potentialPresenters = filteredClasses.parallelStream()
                               .filter(apkClass -> apkClass instanceof HelperClass)
                               .filter(presenterCandidate -> interactionClasses.stream()
                                      .anyMatch(interractionClass -> classSatisfiesPresenterForm(interractionClass,presenterCandidate)))
                               .collect(Collectors.toList());

                /*List<EventModelsViaHelperClassesManipulation> viaPresenterClassesManipulations = viaHelperClassesManipulations.stream()
                        .map(abstractEventModelsManipulation -> ((EventModelsViaHelperClassesManipulation)abstractEventModelsManipulation))
                        .filter(eventModelsViaHelperClassesManipulation -> eventModelsViaHelperClassesManipulation.getManipulationStatements()
                               .stream()
                               .anyMatch(listListTuple -> listListTuple.getSecondParameter()
                                       .stream()
                                       .filter(apkTypeEntry -> apkTypeEntry instanceof HelperClass)
                                       .anyMatch(apkTypeEntry -> classSatisfiesPresenterForm(eventModelsViaHelperClassesManipulation
                                                                                                     .getEvent()
                                                                                                     .getContainingType(),apkTypeEntry))))
                        .collect(Collectors.toList());*/

                if ((potentialPresenters.size() > 0)&&(potentialPresenters.size() >= interactionClasses.size() / THREE))
                {
                    //MVP
                    /*List<Presenter> presenters = filteredClasses.stream()
                    .filter(apkClass -> apkClass instanceof HelperClass)
                    .filter(apkClass -> filteredClasses.stream()
                           .filter(activeClass -> activeClass instanceof ActiveClass)
                            .filter(activeClass -> !(activeClass instanceof ApplicationClass))
                            .filter(activeClass -> !(activeClass instanceof UIElementClass))
                            .anyMatch(activeClass -> classSatisfiesPresenterForm(activeClass, apkClass)))
                    .map(apkClass -> new Presenter(apkClass))
                    .collect(Collectors.toList());*/

                    /*List<Presenter> presenters = viaPresenterClassesManipulations.stream()
                            .map(eventModelsViaHelperClassesManipulation -> eventModelsViaHelperClassesManipulation.getManipulationStatements().stream()
                                    .map(listListTuple -> listListTuple.getSecondParameter()
                                            .stream()
                                            .filter(apkTypeEntry -> apkTypeEntry instanceof HelperClass)
                                            .filter(apkTypeEntry -> classSatisfiesPresenterForm(eventModelsViaHelperClassesManipulation
                                                                                                          .getEvent()
                                                                                                          .getContainingType(),apkTypeEntry)))
                                    .flatMap(apkTypeEntryStream -> apkTypeEntryStream)
                                    .collect(Collectors.toList()))
                            .flatMap(apkTypeEntries -> apkTypeEntries.stream())
                            .distinct()
                            .map(apkClass -> new Presenter(apkClass))
                            .collect(Collectors.toList());*/

                    List<Presenter> presenters = potentialPresenters.stream()
                                    .map(potentialPresenter -> new Presenter(potentialPresenter))
                                    .collect(Collectors.toList());

                    views =  Stream.concat(interactionClasses.stream()
                                                              .map(apkTypeEntry -> new View(apkTypeEntry)),
                                                      resourceFiles.stream()
                                                              .filter(apkResourceFile -> (apkResourceFile instanceof LayoutFile)||
                                                                                         (apkResourceFile instanceof AnimationFile))
                                                              .map(apkResourceFile -> new View(apkResourceFile)))
                            .collect(Collectors.toList());

                    abstractArchitecturalStyle = new MVPArchitecturalStyle(views,
                                                                          presenters,
                                                                          modelClasses.stream().map(model -> (Model)model).collect(Collectors.toList()));
                }
                else
                {
                    //MVC

                    /*List<ApkTypeEntry> helperControllers  = viaHelperClassesManipulations.stream()
                            .map(abstractEventModelsManipulation -> ((EventModelsViaHelperClassesManipulation)abstractEventModelsManipulation))
                            .map(abstractEventModelsManipulation -> abstractEventModelsManipulation.getManipulationStatements()
                                    .stream()
                                    .map(manipulationStatement -> manipulationStatement.getSecondParameter()
                                            .stream()
                                            .filter(apkTypeEntry -> apkTypeEntry instanceof HelperClass)
                                            .filter(apkTypeEntry -> (!isListenerClassOfEvent(apkTypeEntry, events))&&
                                                                    (!isConcurrentOperationClass(apkTypeEntry)))
                                            .distinct()
                                            .collect(Collectors.toList()))
                                    .flatMap(apkTypeEntries -> apkTypeEntries.stream())
                                    .distinct()
                                    .collect(Collectors.toList()))
                            .flatMap(potentialPresenters -> potentialPresenters.stream())
                            .distinct()
                            .collect(Collectors.toList());*/

                    List<Controller> controllers =
                            Stream.concat(interactionClasses.stream()
                                                  .map(activeClass -> new Controller(activeClass)),
                                          helperCandidates
                                                  .stream()
                                                  .map(apkTypeEntry -> new Controller(apkTypeEntry)))
                                    .collect(Collectors.toList());


                    views =  Stream.concat(allApkTypeEntries.stream()
                                                              .filter(apkTypeEntry -> (apkTypeEntry instanceof UIElementClass))
                                                              .map(apkTypeEntry -> new View(apkTypeEntry)),
                                                      resourceFiles.stream()
                                                              .filter(apkResourceFile -> (apkResourceFile instanceof LayoutFile)||
                                                                                         (apkResourceFile instanceof AnimationFile))
                                                              .map(apkResourceFile -> new View(apkResourceFile)))
                            .collect(Collectors.toList());

                    abstractArchitecturalStyle = new MVCArchitecturalStyle(views,
                                                                          controllers,
                                                                          modelClasses.stream().map(model -> (Model)model).collect(Collectors.toList()));
                }

            }
        }
        else
        {
            //Filter input events manipulations in databinding classes
            List<Tuple<Event, List<AbstractEventModelsManipulation>>> dataBindingClassesWithEventsManipulatingModels = helperAndDataBindingClassesWithEventsManipulatingModels
                    .parallelStream()
                    .filter(event -> (event.getFirstParameter().getContainingType() instanceof DataBindingClass))
                    .collect(Collectors.toList());

            List<Tuple<Event, List<AbstractEventModelsManipulation>>> helperClassesWithEventsManipulatingModels = helperAndDataBindingClassesWithEventsManipulatingModels
                    .parallelStream()
                    .filter(event -> (event.getFirstParameter().getContainingType() instanceof HelperClass))
                    .collect(Collectors.toList());

            int numberOfModelsManipulationsByDataBindingClasses = dataBindingClassesWithEventsManipulatingModels.size();
            int numberOfModelsManipulationsByHelperClasses = helperClassesWithEventsManipulatingModels.size();


            if (numberOfModelsManipulationsByDataBindingClasses > numberOfModelsManipulationsByHelperClasses)
            {
                //MVVM
                List<ViewModel> viewModels = allClassesExceptModels
                        .parallelStream()
                    .filter(apkTypeEntry -> apkTypeEntry instanceof HelperClass)
                    .filter(apkTypeEntry -> (!isListenerClassOfInputEvent(apkTypeEntry, events))&&
                                            (!isConcurrentOperationClass(apkTypeEntry)))
                    .filter(apkTypeEntry -> allClassesExceptModels.stream()
                            .filter(apkClass -> apkClass instanceof DataBindingClass)
                            .map(apkClass -> (DataBindingClass)apkClass)
                            .anyMatch(dataBindingClass -> dataBindingClass
                                    .getFields()
                                    .stream()
                                    .filter(field -> field.getSootField().isPrivate())
                                    .anyMatch(field -> field.getSootField().getType().equals(apkTypeEntry.getSootClass().getType()))))
                    .distinct()
                    .map(apkClass -> new ViewModel(apkClass))
                    .collect(Collectors.toList());

                views =  Stream.concat(allApkTypeEntries.stream()
                                                          .filter(apkTypeEntry -> (apkTypeEntry instanceof ActiveClass))
                                                          .filter(apkTypeEntry -> !(apkTypeEntry instanceof ApplicationClass))
                                                          .map(apkTypeEntry -> new View(apkTypeEntry)),
                                                  resourceFiles.stream()
                                                          .filter(apkResourceFile -> (apkResourceFile instanceof LayoutFile)||
                                                                                     (apkResourceFile instanceof AnimationFile))
                                                          .map(apkResourceFile -> new View(apkResourceFile)))
                        .collect(Collectors.toList());

                abstractArchitecturalStyle = new MVVMArchitecturalStyle(views,
                                                                      viewModels,
                                                                      modelClasses.stream().map(model -> (Model)model).collect(Collectors.toList()));

            }
            else
            {
                //MVC

                List<ApkTypeEntry> helperControllers  = helperClassesWithEventsManipulatingModels
                        .parallelStream()
                        .map(eventListTuple -> eventListTuple.getSecondParameter())
                        .flatMap(abstractEventModelsManipulations -> abstractEventModelsManipulations.stream())
                        .filter(abstractEventModelsManipulation -> abstractEventModelsManipulation instanceof EventModelsViaHelperClassesManipulation)
                        .map(abstractEventModelsManipulation -> ((EventModelsViaHelperClassesManipulation)abstractEventModelsManipulation))
                        .map(abstractEventModelsManipulation -> abstractEventModelsManipulation.getManipulationStatements()
                                .stream()
                                .map(manipulationStatement -> manipulationStatement.getSecondParameter()
                                        .stream()
                                        .filter(apkTypeEntry -> apkTypeEntry instanceof HelperClass)
                                        .filter(apkTypeEntry -> !isConcurrentOperationClass(apkTypeEntry))
                                        .distinct()
                                        .collect(Collectors.toList()))
                                .flatMap(apkTypeEntries -> apkTypeEntries.stream())
                                .distinct()
                                .collect(Collectors.toList()))
                        .flatMap(potentialController -> potentialController.stream())
                        .distinct()
                        .collect(Collectors.toList());

                List<Controller> controllers =
                        Stream.concat(filteredClasses.stream()
                                              .filter(activeClass -> activeClass instanceof ActiveClass)
                                              .map(activeClass -> new Controller(activeClass)),
                                      helperControllers
                                              .stream()
                                              .map(apkTypeEntry -> new Controller(apkTypeEntry)))
                                .collect(Collectors.toList());


                views =  Stream.concat(allApkTypeEntries.stream()
                                                          .filter(apkTypeEntry -> (apkTypeEntry instanceof UIElementClass))
                                                          .map(apkTypeEntry -> new View(apkTypeEntry)),
                                                  resourceFiles.stream()
                                                          .filter(apkResourceFile -> (apkResourceFile instanceof LayoutFile)||
                                                                                     (apkResourceFile instanceof AnimationFile))
                                                          .map(apkResourceFile -> new View(apkResourceFile)))
                        .collect(Collectors.toList());

                abstractArchitecturalStyle = new MVCArchitecturalStyle(views,
                                                                      controllers,
                                                                      modelClasses.stream().map(model -> (Model)model).collect(Collectors.toList()));
            }
        }

        return abstractArchitecturalStyle;
    }

    private boolean isListenerClassOfEvent(ApkTypeEntry apkClass, List<Event> events)
    {
        return  isListenerClassOfInputEvent(apkClass, events) ||
                isListenerClassOfNonUIElementEvent(apkClass, events);
    }

    private boolean isListenerClassOfInputEvent(ApkTypeEntry apkClass, List<Event> events)
    {
        return events.parallelStream()
                       .filter(event -> event instanceof InputEvent)
                       .map(event -> (InputEvent)event)
                       .anyMatch(event -> event.getListenerClass().getSootClass().equals(apkClass.getSootClass()));
    }

    private boolean isListenerClassOfNonUIElementEvent(ApkTypeEntry apkClass, List<Event> events)
    {
        return events.parallelStream()
                       .filter(event -> event instanceof NonUIElementEvent)
                       .map(event -> (NonUIElementEvent)event)
                       .anyMatch(event -> event.getListenerClass().getSootClass().equals(apkClass.getSootClass()));
    }

    public boolean classSatisfiesPresenterForm(ApkTypeEntry invokingClass, ApkTypeEntry potentialPresenter)
    {
        //boolean classSatisfiesPresenterForm;
        if (!(invokingClass instanceof ActiveClass) ||
            !(potentialPresenter instanceof HelperClass)||
            potentialPresenter.getSootClass().isPrivate()||
            potentialPresenter.getSootClass().isProtected()||
            potentialPresenter.getSootClass().isInterface()||
            potentialPresenter.getSootClass().isStatic()||
            isListenerClassOfEvent(potentialPresenter, events)||
            isConcurrentOperationClass(potentialPresenter)||
            ((HelperClass) potentialPresenter).isAnonymous()||
            potentialPresenter.getSootClass().isInnerClass())
        {
            return false;
        }

        List<SootClass> invokingClassAndHierarchy = getAllUpperTypes(invokingClass.getSootClass());
        List<AbstractMethod> potentialPresenterMethods = potentialPresenter.getAllMethods();
        List<Field> potentialPresenterFields = potentialPresenter.getFields();

        // fields with type same as invoking classes (Activity, Fragments, ...)
        List<SootField> fieldsWitheSameTypeAsInvokingClass = getAllUpperTypes(potentialPresenter.getSootClass())
                .stream()
                .map(sootClass -> sootClass.getFields())
                .flatMap(sootFields -> sootFields.stream())
                .filter(sootField -> invokingClassAndHierarchy.stream()
                                .anyMatch(invokingSootClass -> invokingSootClass.getType().equals(sootField.getType())))
                .collect(Collectors.toList());

        /*List<AbstractMethod> methodsHavingParameterWithSameTypeAsInvokingClass = potentialPresenterMethods
                .stream()
                .filter(abstractMethod -> abstractMethod.getSootMethod()
                        .getParameterTypes()
                        .stream()
                        .anyMatch(type -> invokingClassAndHierarchy.stream()
                                .anyMatch(sootClass -> sootClass.getType().equals(type))))
                .collect(Collectors.toList());*/

        /*List<AbstractMethod> methodsInitializingFieldsWithParameterHavingSameTypeAsInvokingClass = methodsHavingParameterWithSameTypeAsInvokingClass.stream()
                .filter(abstractMethod -> {
                    abstractMethod.loadStatements();
                    return abstractMethod.getInstanceFieldAssignmentStatements() != null;
                })
                .filter(abstractMethod -> abstractMethod.getInstanceFieldAssignmentStatements()
                        .stream()
                        .anyMatch(instanceFieldAssignmentStatement -> invokingClassAndHierarchy.stream()
                                .anyMatch(sootClass -> sootClass.getType().equals(instanceFieldAssignmentStatement.getField().getSootField().getType()))))
                .collect(Collectors.toList());*/

        List<AbstractMethod> methodsCallingInstanceMethodsOfInvokingClass = potentialPresenterMethods.stream()
                .filter(abstractMethod -> {
                    abstractMethod.loadStatements();
                    return abstractMethod.getMethodCallStatements() != null;
                })
                .filter(abstractMethod -> abstractMethod.getMethodCallStatements()
                        .stream()
                        .filter(methodCallStatement -> {
                            boolean isInstanceCall = false;
                            Unit    sootStatement = methodCallStatement.getSootStatement();
                            if (isInvocationStatement(sootStatement))
                            {
                                if ((((JInvokeStmt)sootStatement).getInvokeExprBox().getValue() instanceof JStaticInvokeExpr)||
                                    (((JInvokeStmt)sootStatement).getInvokeExprBox().getValue() instanceof JSpecialInvokeExpr))
                                {
                                    return isInstanceCall;
                                }
                            }
                            else
                            {
                                if (isAssignmentStatement(sootStatement))
                                {
                                    if ((((JAssignStmt)sootStatement).getRightOpBox().getValue() instanceof JStaticInvokeExpr)||
                                        (((JAssignStmt)sootStatement).getRightOpBox().getValue() instanceof JSpecialInvokeExpr))
                                    {
                                        return isInstanceCall;
                                    }
                                }
                            }
                            return true;
                        })
                        .anyMatch(
                                methodCallStatement -> invokingClassAndHierarchy.stream()
                                        .anyMatch(sootClass -> sootClass.equals(methodCallStatement.getInvokedObjectType().getSootClass()))))
                .collect(Collectors.toList());


        List<SootClass> potentialPresentationAndHierarchy = getAllUpperTypes(potentialPresenter.getSootClass());
        List<AbstractMethod> invokingClassMethods = invokingClass.getAllMethods();
        List<Field> invokingClassFields = invokingClass.getFields();

        // fields with type same as invoking classes (Activity, Fragments, ...)
        /*List<Field> fieldsWitheSameTypeAsPotentialPresenterClass = potentialPresenter.getFields()
                .stream()
                .filter(field -> potentialPresentationAndHierarchy.stream()
                        .anyMatch(sootClass -> sootClass.getType().equals(field.getSootField().getType())))
                .collect(Collectors.toList());*/


        List<SootField> fieldsWitheSameTypeAsPotentialPresenterClass = getAllUpperTypes(invokingClass.getSootClass())
                .stream()
                .map(sootClass -> sootClass.getFields())
                .flatMap(sootFields -> sootFields.stream())
                .filter(sootField -> potentialPresentationAndHierarchy.stream()
                        .anyMatch(potentialSootClass -> potentialSootClass.getType().equals(sootField.getType())))
                .collect(Collectors.toList());

        /*List<AbstractMethod> methodsInitializingFieldsWithSameTypeAsPotentialClass = invokingClassMethods.stream()
                .filter(abstractMethod -> {
                    abstractMethod.loadStatements();
                    return abstractMethod.getInstanceFieldAssignmentStatements() != null;
                })
                .filter(abstractMethod -> abstractMethod.getInstanceFieldAssignmentStatements()
                        .stream()
                        .anyMatch(instanceFieldAssignmentStatement -> potentialPresentationAndHierarchy.stream()
                                .anyMatch(sootClass -> sootClass.getType().equals(instanceFieldAssignmentStatement.getField().getSootField().getType()))))
                .collect(Collectors.toList());*/

        List<AbstractMethod> methodsCallingInstanceMethodsOfPotentialClass = invokingClassMethods.stream()
                .filter(abstractMethod -> {
                    abstractMethod.loadStatements();
                    return abstractMethod.getMethodCallStatements() != null;
                })
                .filter(abstractMethod -> abstractMethod.getMethodCallStatements()
                        .stream()
                        .filter(methodCallStatement -> {
                            boolean isInstanceCall = false;
                            Unit    sootStatement = methodCallStatement.getSootStatement();
                            if (isInvocationStatement(sootStatement))
                            {
                                if ((((JInvokeStmt)sootStatement).getInvokeExprBox().getValue() instanceof JStaticInvokeExpr)||
                                    (((JInvokeStmt)sootStatement).getInvokeExprBox().getValue() instanceof JSpecialInvokeExpr))
                                {
                                    return isInstanceCall;
                                }
                            }
                            else
                            {
                                if (isAssignmentStatement(sootStatement))
                                {
                                    if ((((JAssignStmt)sootStatement).getRightOpBox().getValue() instanceof JStaticInvokeExpr)||
                                        (((JAssignStmt)sootStatement).getRightOpBox().getValue() instanceof JSpecialInvokeExpr))
                                    {
                                        return isInstanceCall;
                                    }
                                }
                            }
                            return true;
                        })
                        .anyMatch(
                                methodCallStatement -> potentialPresentationAndHierarchy.stream()
                                        .anyMatch(sootClass -> sootClass.equals(methodCallStatement.getInvokedObjectType().getSootClass()))))
                .collect(Collectors.toList());

        return ((fieldsWitheSameTypeAsInvokingClass.size() > ZERO) &&
               (fieldsWitheSameTypeAsPotentialPresenterClass.size() > ZERO)) &&
               (methodsCallingInstanceMethodsOfInvokingClass.size() > ZERO) &&
               (methodsCallingInstanceMethodsOfPotentialClass.size() > ZERO);

        /*return (methodsCallingInstanceMethodsOfInvokingClass.size() > ZERO) &&
               (methodsCallingInstanceMethodsOfPotentialClass.size() > ZERO);*/

    }

    private List<ApkClass> getEventfulClasses()
    {
        List<HelperClass>    helperClasses    = filteredClasses.stream()
                .filter(apkClass -> apkClass instanceof HelperClass)
                .map(apkClass -> (HelperClass)apkClass)
                .collect(Collectors.toList());

        List<ActivityClass>    activityClasses    = filteredClasses.stream()
                .filter(apkClass -> apkClass instanceof ActivityClass)
                .map(apkClass -> (ActivityClass)apkClass)
                .collect(Collectors.toList());

        List<AndroidGeneratedClass> androidGeneratedClasses =  filteredClasses.stream()
                .filter(apkClass -> apkClass instanceof AndroidGeneratedClass)
                .map(apkClass -> (AndroidGeneratedClass)apkClass)
                .collect(Collectors.toList());

        List<FragmentClass>    fragmentClasses    = filteredClasses.stream()
                .filter(apkClass -> apkClass instanceof FragmentClass)
                .map(apkClass -> (FragmentClass)apkClass)
                .collect(Collectors.toList());

        List<DialogClass>    dialogClasses    = filteredClasses.stream()
                .filter(apkClass -> apkClass instanceof DialogClass)
                .map(apkClass -> (DialogClass)apkClass)
                .collect(Collectors.toList());

        List<ApplicationClass> applicationClasses = filteredClasses.stream()
                .filter(apkClass -> apkClass instanceof ApplicationClass)
                .map(apkClass -> (ApplicationClass)apkClass)
                .collect(Collectors.toList());

        List<ApkClass> eventfulClasses = new ArrayList<>();
        eventfulClasses.addAll(helperClasses);
        eventfulClasses.addAll(androidGeneratedClasses.stream().filter(androidGeneratedClass -> androidGeneratedClass instanceof DataBindingClass).collect(Collectors.toList()));
        eventfulClasses.addAll(activityClasses);
        eventfulClasses.addAll(fragmentClasses);
        eventfulClasses.addAll(dialogClasses);
        eventfulClasses.addAll(applicationClasses);

        return eventfulClasses;
    }

    private List<AbstractMethod> findNonUIElementsEventsExecutionMethods(AbstractMethod calledMethod,
                                                                         List<Event> events)
    {
        List<AbstractMethod> returnValue = events.parallelStream()
                .filter(invokedEvent -> invokedEvent instanceof NonUIElementEvent)
                .filter(invokedEvent -> !classSatisfiesPresenterForm(((NonUIElementEvent)invokedEvent).getListenerClass(), ((NonUIElementEvent)invokedEvent).getInvokedObject()))
                .filter(invokedEvent -> ((NonUIElementEvent) invokedEvent).getContainingMethod().getSootMethod().equals(calledMethod.getSootMethod()))
                .map(invokedEvent -> invokedEvent.getExecutionMethods())
                .flatMap(invokedEventExecutionMethods -> invokedEventExecutionMethods.stream())
                .collect(Collectors.toList());

        return returnValue;
    }

    private List<AbstractMethod> findConcurrentOperationsExecutionMethods(AbstractMethod calledMethod)
    {
        return calledMethod.retrieveConcurrentOperations(filteredClasses)
                .parallelStream()
                .map(abstractConcurrentOperation -> abstractConcurrentOperation.getExecutionMethods())
                .flatMap(methods -> methods.stream())
                .collect(Collectors.toList());
    }

    private List<AbstractMethod> findInvokedInputEventsExecutionMethods(AbstractMethod calledMethod,
                                                                        List<Event> events)
    {
        return events.stream()
                .filter(invokedEvent -> invokedEvent instanceof InputEvent)
                .filter(invokedEvent -> ((InputEvent) invokedEvent).getContainingMethod().getSootMethod().equals(calledMethod.getSootMethod()))
                .map(invokedEvent -> invokedEvent.getExecutionMethods())
                .flatMap(invokedEventExecutionMethods -> invokedEventExecutionMethods.stream())
                .collect(Collectors.toList());
    }

    public class MethodIncomingHierarchy
    {
        private AbstractMethod actualMethod;
        private List<AbstractMethod> predecessorCallers;

        public MethodIncomingHierarchy(AbstractMethod actualMethod)
        {
            this.actualMethod = actualMethod;
            this.predecessorCallers = new ArrayList<>();
            this.predecessorCallers.add(actualMethod);
        }

        public MethodIncomingHierarchy(AbstractMethod actualMethod, AbstractMethod firstPredecessor)
        {
            this.actualMethod = actualMethod;
            this.predecessorCallers = new ArrayList<>();
            this.predecessorCallers.add(firstPredecessor);
            this.predecessorCallers.add(actualMethod);
        }

        public MethodIncomingHierarchy(AbstractMethod actualMethod, List<AbstractMethod> predecessorCallers)
        {
            this.actualMethod = actualMethod;
            this.predecessorCallers = new ArrayList<>();
            this.predecessorCallers.addAll(predecessorCallers);
            this.predecessorCallers.add(actualMethod);
        }



        public AbstractMethod getActualMethod()
        {
            return actualMethod;
        }

        public void setActualMethod(AbstractMethod actualMethod)
        {
            this.actualMethod = actualMethod;
        }

        public List<AbstractMethod> getPredecessorCallers()
        {
            return predecessorCallers;
        }

        public void setPredecessorCallers(List<AbstractMethod> predecessorCallers)
        {
            this.predecessorCallers = predecessorCallers;
        }
    }
}
