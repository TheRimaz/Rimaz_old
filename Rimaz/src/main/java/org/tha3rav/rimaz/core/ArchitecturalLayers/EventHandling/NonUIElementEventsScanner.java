package org.tha3rav.rimaz.core.ArchitecturalLayers.EventHandling;

import org.tha3rav.rimaz.apk.ApkClass;
import org.tha3rav.rimaz.apk.ApkTypeEntry;
import org.tha3rav.rimaz.core.OOP.AbstractMethod;
import org.tha3rav.rimaz.core.OOP.Method;
import org.tha3rav.rimaz.core.Statements.MethodCallStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import soot.RefType;
import soot.SootClass;

import static org.tha3rav.rimaz.utils.ANDROID.AndroidHelpers.isOrExtendingInnerClassOfUIElement;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidHelpers.isUIElement;
import static org.tha3rav.rimaz.utils.OOP.OOPConstants.Numbers.ONE;
import static org.tha3rav.rimaz.utils.OOP.OOPConstants.Numbers.ZERO;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.fieldSignatureContainsType;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.getAllUpperTypes;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.isRefType;

public class NonUIElementEventsScanner implements INonUIElementEventsScanner
{
    private List<ApkClass> allClasses;

    public NonUIElementEventsScanner(List<ApkClass> allClasses)
    {
        this.allClasses = allClasses;
    }

    @Override
    public List<NonUIElementEvent> scanForNonUIElementEvents(ApkTypeEntry targetedType)
    {
        List<NonUIElementEvent> nonUIElementEvents = new ArrayList<>();

        List<AbstractMethod> allImplementedMethods = targetedType.getAllMethods()
                .stream()
                .filter(abstractMethod -> !abstractMethod.getSootMethod().isAbstract())
                .collect(Collectors.toList());

        List<MethodCallStatement> methodCallStatements = new ArrayList<>();
        Optional<NonUIElementEvent> nonUIElementEvent = Optional.empty();

        for (AbstractMethod abstractMethod: allImplementedMethods)
        {
            abstractMethod.loadStatements();
            methodCallStatements = abstractMethod.getMethodCallStatements();

            for (MethodCallStatement methodCallStatement: methodCallStatements)
            {
                nonUIElementEvent = isNonUIElementEventInvocation(methodCallStatement, allClasses);

                if (nonUIElementEvent.isPresent())
                {
                    nonUIElementEvents.add(nonUIElementEvent.get());
                }
            }
        }

        return nonUIElementEvents;
    }

    public Optional<NonUIElementEvent> isNonUIElementEventInvocation(MethodCallStatement methodCallStatement, List<ApkClass> allClasses)
    {
        Optional<NonUIElementEvent> nonUIElementEvent = Optional.empty();

        boolean isInvokedObjectReferenceType = isRefType(methodCallStatement.getInvokedObjectType().getSootClass().getType());

        if (isInvokedObjectReferenceType)
        {
            boolean isInvokedObjectUIElement = isUIElement(methodCallStatement.getInvokedObjectType().getSootClass());
            boolean isOrExtendingInnerClassOfUIElement = isOrExtendingInnerClassOfUIElement(methodCallStatement.getInvokedObjectType().getSootClass());

            if ((!isInvokedObjectUIElement) &&
                (!isOrExtendingInnerClassOfUIElement))
            {
                ApkTypeEntry invokedObject = methodCallStatement.getInvokedObjectType();
                AbstractMethod calledMethod = methodCallStatement.getCalledMethod();

                if ((calledMethod instanceof Method) &&
                    (!calledMethod.getSootMethod().isStatic()) &&
                    (calledMethod.getSootMethod().getParameterCount() == ONE))
                {
                    if(isRefType(calledMethod.getSootMethod().getParameterType(ZERO)))
                    {
                        SootClass expectedParameter = ((RefType)methodCallStatement.getCalledMethod()
                                .getSootMethod()
                                .getParameterType(ZERO))
                                .getSootClass();

                        SootClass passedParameter = methodCallStatement.getPassedParameterType(ZERO);

                        List<SootClass> invokedObjectAndHierarchy = getAllUpperTypes(invokedObject.getSootClass());
                        boolean invokedObjectOrHierarchyHasFieldWithSameType = invokedObjectAndHierarchy.stream()
                                .anyMatch(sootClass -> sootClass.getFields()
                                        .stream()
                                        .anyMatch(sootField -> sootField.getType().equals(expectedParameter.getType())));

                        //Probably a collection or any generic type
                        boolean invokedObjectOrHierarchyHasFieldThatWrapsSameType = invokedObjectAndHierarchy.stream()
                                .anyMatch(sootClass -> sootClass.getFields()
                                        .stream()
                                        .anyMatch(sootField -> fieldSignatureContainsType(sootField, expectedParameter.getName())));

                        if (passedParameter != null)
                        {
                            if (invokedObjectOrHierarchyHasFieldWithSameType ||
                                invokedObjectOrHierarchyHasFieldThatWrapsSameType)
                            {
                                if (allClasses.stream().anyMatch(apkC -> apkC.getName()
                                        .equals(passedParameter.getName())))
                                {
                                    Optional<ApkClass> equivalentApkClass = allClasses
                                            .stream()
                                            .filter(apkC1 -> apkC1.getName().equals
                                                    (passedParameter.getName()))
                                            .filter(apkC1 -> (apkC1.getSootClass().getInterfaceCount() >= ONE) ||
                                                             (apkC1.getSootClass().getSuperclass() != null))
                                            .findFirst();
                                    if (equivalentApkClass.isPresent())
                                    {
                                        ApkClass listenerClass = equivalentApkClass.get();

                                        List<Method> executionMethods = listenerClass.getMethods()
                                                .stream()
                                                .filter(implementedMethod -> getAllUpperTypes(expectedParameter)
                                                        .stream()
                                                        .map(upperType -> upperType.getMethods())
                                                        .flatMap(upperTypes -> upperTypes.stream())
                                                        .anyMatch(upperMethod -> upperMethod.getSubSignature().equals(implementedMethod.getSootMethod().getSubSignature())))
                                                .collect(Collectors.toList());

                                        nonUIElementEvent = Optional.of(new NonUIElementEvent(methodCallStatement.getDeclaringType(),
                                                                                              methodCallStatement.getContainingMethod(),
                                                                                              listenerClass,
                                                                                              expectedParameter,
                                                                                              executionMethods,
                                                                                              invokedObject));
                                    }


                                }
                                else
                                {
                                    //Case where the listener is an anonymous class declared as
                                    // Field
                                    //
                                    Optional<ApkClass> equivalentApkClass = allClasses.stream()
                                            .filter(apkC1 -> {
                                                if (apkC1.getSootClass().getInterfaceCount() >= ONE)
                                                {
                                                    return apkC1.getSootClass().getInterfaces().getFirst().getName().equals(expectedParameter.getName());
                                                }
                                                else
                                                {
                                                    if(apkC1.getSootClass().getSuperclass() != null)
                                                    {
                                                        return apkC1.getSootClass().getSuperclass().getName().equals(expectedParameter.getName());
                                                    }
                                                    return false;
                                                }
                                            })
                                            .findFirst();

                                    if (equivalentApkClass.isPresent())
                                    {
                                        ApkClass listenerClass = equivalentApkClass.get();

                                        List<Method> executionMethods = listenerClass.getMethods()
                                                .stream()
                                                .filter(implementedMethod -> getAllUpperTypes(expectedParameter)
                                                        .stream()
                                                        .map(upperType -> upperType.getMethods())
                                                        .flatMap(upperTypes -> upperTypes.stream())
                                                        .anyMatch(upperMethod -> upperMethod.getSubSignature().equals(implementedMethod.getSootMethod().getSubSignature())))
                                                .collect(Collectors.toList());

                                        nonUIElementEvent = Optional.of(new NonUIElementEvent(methodCallStatement.getDeclaringType(),
                                                                                              methodCallStatement.getContainingMethod(),
                                                                                              listenerClass,
                                                                                              expectedParameter,
                                                                                              executionMethods,
                                                                                              invokedObject));
                                    }
                                }

                            }
                            else
                            {
                                //All other cases (isCertain = false)
                                //case of callbacks, observables ...
                                if (allClasses.stream().anyMatch(apkC -> apkC.getName()
                                        .equals(passedParameter.getName())))
                                {
                                    Optional<ApkClass> equivalentApkClass = allClasses
                                            .stream()
                                            .filter(apkC1 -> apkC1.getName().equals
                                                    (passedParameter.getName()))
                                            .filter(apkC1 -> (apkC1.getSootClass().getInterfaceCount() >= ONE) ||
                                                             (apkC1.getSootClass().getSuperclass() != null))
                                            .findFirst();

                                    if (equivalentApkClass.isPresent())
                                    {
                                        ApkClass listenerClass = equivalentApkClass.get();

                                        if (listenerClass.isAnonymous()||
                                            listenerClass.isSubClassOf(methodCallStatement.getDeclaringType().getSootClass()))
                                        {
                                            List<Method> executionMethods = listenerClass.getMethods()
                                                    .stream()
                                                    .filter(implementedMethod -> getAllUpperTypes(expectedParameter)
                                                            .stream()
                                                            .map(upperType -> upperType.getMethods())
                                                            .flatMap(upperTypes -> upperTypes.stream())
                                                            .anyMatch(upperMethod -> upperMethod.getSubSignature().equals(implementedMethod.getSootMethod().getSubSignature())))
                                                    .collect(Collectors.toList());

                                            nonUIElementEvent = Optional.of(new NonUIElementEvent
                                                                                    (methodCallStatement.getDeclaringType(),
                                                                                                  methodCallStatement.getContainingMethod(),
                                                                                                  listenerClass,
                                                                                                  expectedParameter,
                                                                                                  executionMethods,
                                                                                                  false,
                                                                                                  invokedObject));


                                        }
                                    }
                                }
                                else
                                {
                                    //Case where the listener is an anonymous class declared as
                                    // Field
                                    //
                                    Optional<ApkClass> equivalentApkClass = allClasses.stream()
                                            .filter(apkC1 -> {
                                                if (apkC1.getSootClass().getInterfaceCount() >= ONE)
                                                {
                                                    return apkC1.getSootClass().getInterfaces().getFirst().getName().equals(expectedParameter.getName());
                                                }
                                                else
                                                {
                                                    if(apkC1.getSootClass().getSuperclass() != null)
                                                    {
                                                        return apkC1.getSootClass().getSuperclass().getName().equals(expectedParameter.getName());
                                                    }
                                                    return false;
                                                }
                                            })
                                            .findFirst();

                                    if (equivalentApkClass.isPresent())
                                    {
                                        ApkClass listenerClass = equivalentApkClass.get();

                                        if (listenerClass.isAnonymous()||
                                                listenerClass.isSubClassOf(methodCallStatement.getDeclaringType().getSootClass()))
                                        {
                                            List<Method> executionMethods = listenerClass.getMethods()
                                                         .stream()
                                                         .filter(implementedMethod -> getAllUpperTypes(expectedParameter)
                                                                 .stream()
                                                                 .map(upperType -> upperType.getMethods())
                                                                 .flatMap(upperTypes -> upperTypes.stream())
                                                                 .anyMatch(upperMethod -> upperMethod.getSubSignature().equals(implementedMethod.getSootMethod().getSubSignature())))
                                                    .collect(Collectors.toList());

                                            nonUIElementEvent = Optional.of(new NonUIElementEvent(methodCallStatement.getDeclaringType(),
                                                                                                  methodCallStatement.getContainingMethod(),
                                                                                                  listenerClass,
                                                                                                  expectedParameter,
                                                                                                  executionMethods,
                                                                                                  false,
                                                                                                  invokedObject));
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return nonUIElementEvent;
    }
}
