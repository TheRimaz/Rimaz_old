package org.tha3rav.rimaz.core.ArchitecturalLayers.EventHandling;

import org.tha3rav.rimaz.apk.ApkClass;
import org.tha3rav.rimaz.apk.ApkTypeEntry;
import org.tha3rav.rimaz.core.OOP.AbstractMethod;
import org.tha3rav.rimaz.core.OOP.Method;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import soot.Body;
import soot.RefType;
import soot.SootClass;
import soot.SootMethodRef;
import soot.Unit;
import soot.ValueBox;
import soot.VoidType;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.internal.ImmediateBox;
import soot.jimple.internal.JInvokeStmt;
import soot.jimple.internal.JVirtualInvokeExpr;
import soot.jimple.internal.JimpleLocal;
import soot.jimple.internal.JimpleLocalBox;

import static org.tha3rav.rimaz.utils.ANDROID.AndroidHelpers.isOrExtendingInnerClassOfUIElement;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidHelpers.isUIElement;
import static org.tha3rav.rimaz.utils.OOP.OOPConstants.Numbers.ONE;
import static org.tha3rav.rimaz.utils.OOP.OOPConstants.Numbers.ZERO;
import static org.tha3rav.rimaz.utils.OOP.OOPConstants.Symbols.EMPTY_STRING;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.fieldSignatureContainsType;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.getExpectedParameterOuterTypeName;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.getMethodBody;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.getUpperHierarchy;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.isExpectedParameterTypeInner;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.isRefType;

public class InputEventScanner implements IInputEventScanner
{
    private List<ApkClass> allApkClasses;

    public InputEventScanner(List<ApkClass> allApkClasses)
    {
        this.allApkClasses = allApkClasses;
    }

    public List<InputEvent> scanForInputEvents(ApkTypeEntry eventfulType)
    {
        List<InputEvent> inputEvents = new ArrayList<>();

        if (eventfulType.getAllMethods() == null)
        {
            return inputEvents;
        }

        List<AbstractMethod> methodsToScan = eventfulType.getAllMethods()
                .stream()
                .filter(method -> !method.getSootMethod().isAbstract())
                .collect(Collectors.toList());

        if (methodsToScan == null)
        {
            return inputEvents;
        }

        for (AbstractMethod method : methodsToScan)
        {
            inputEvents.addAll(scanMethod(method, eventfulType));
        }

        return inputEvents;
    }

    private List<InputEvent> scanMethod(AbstractMethod abstractMethod, ApkTypeEntry eventfulType)
    {
        List<InputEvent> inputEvents = new ArrayList<>();

        List<Unit> statementsOfUIElementsInvokingMethods;
        RefType    uiElementType;
        String     uiElementVariableName;
        String     invokedMethodName;

        //Get all statements of UIElements that invoke methods
        statementsOfUIElementsInvokingMethods = findStatementsOfUIElementsInvokingMethods(abstractMethod);

        //Foreach statement of a UIElement invoking a method returning Void and having only one
        // single reference-type parameter
        for (Unit statement : statementsOfUIElementsInvokingMethods)
        {
            //Get concerned UIElement type
            uiElementType = getUIElementInvoker((JInvokeStmt) statement);

            //Get its representation name in JIMPLE
            uiElementVariableName = statement.getUseBoxes()
                    .stream()
                    .filter(valueBox -> valueBox instanceof JimpleLocalBox)
                    .map(valueBox -> ((JimpleLocal) valueBox.getValue()).getName())
                    .findFirst()
                    .orElse(EMPTY_STRING);

            //Get invoked method name
            invokedMethodName = getInvokedMethod((JInvokeStmt) statement).name();

            //If the expected parameter is an INTERFACE or an ABSTRACT CLASS
            if (isInvokedMethodParameterInterfaceOrAbstractClass(statement))
            {
                //Get expected parameter type
                SootClass expectedParameterType = getExpectedParameterType((JInvokeStmt)statement);

                //Get the real passed parameter type
                SootClass passedParameterType = getPassedParameterType(statement);

                //If the type of the passed argument is not null
                if (passedParameterType != null)
                {
                    //Get its name
                    String passedParameterTypeName = passedParameterType.getName();

                    InputEvent scannedInputEvent = null;

                    //If the expected parameter is an Inner type
                    if (isExpectedParameterTypeInner(expectedParameterType))
                    {
                        scannedInputEvent = findInputEventOfInnerListeners(abstractMethod,
                                                                           eventfulType,
                                                                           uiElementType,
                                                                           uiElementVariableName,
                                                                           invokedMethodName,
                                                                           expectedParameterType,
                                                                           passedParameterTypeName);

                    }
                    else
                    {
                        List<SootClass> uiElementAndHierarchy = getUpperHierarchy(uiElementType.getSootClass());
                        boolean uiElementTypeHasFieldWithSameType = uiElementAndHierarchy.stream()
                                .anyMatch(sootClass -> sootClass.getFields()
                                        .stream()
                                        .anyMatch(sootField -> sootField.getType().equals(expectedParameterType.getType())));

                        //Probably a collection or any generic type
                        boolean uiElementTypeHasFieldThatWrapsSameType = uiElementAndHierarchy.stream()
                                .anyMatch(sootClass -> sootClass.getFields()
                                        .stream()
                                        .anyMatch(sootField -> fieldSignatureContainsType(sootField, expectedParameterType.getName())));

                        if (uiElementTypeHasFieldWithSameType ||
                            uiElementTypeHasFieldThatWrapsSameType)
                        {
                            //If the type of the passed argument is one of the APK classes
                            if (allApkClasses.stream().anyMatch(apkC -> apkC.getName()
                                    .equals(passedParameterTypeName)))
                            {
                                //
                                //Case where the listener is an Non anonymous class
                                // or an anonymous class not declared as a field
                                //

                                //Find the APK class representing the passed argument
                                // type (And that implement at least one interface)
                                Optional<ApkClass> equivalentApkClass = allApkClasses
                                        .stream()
                                        .filter(apkC1 -> apkC1.getName().equals
                                                (passedParameterTypeName))
                                        .filter(apkC1 -> (apkC1.getSootClass().getInterfaceCount() >= ONE) ||
                                                         (apkC1.getSootClass().getSuperclass() != null))
                                        .findFirst();

                                //If the APK class representing the passed argument type
                                // exists and
                                // the outer type of the parameter type is a UIElement type
                                if (equivalentApkClass.isPresent())
                                {
                                    scannedInputEvent = new InputEvent(uiElementType,
                                                                       uiElementVariableName,
                                                                       invokedMethodName,
                                                                       abstractMethod,
                                                                       eventfulType,
                                                                       expectedParameterType,
                                                                       equivalentApkClass
                                                                               .get());
                                }
                            }
                            else
                            {
                                //
                                //Case where the listener is an anonymous class declared as
                                // Field
                                //
                                scannedInputEvent = getInputEventFromUnidentifiedListener
                                        (eventfulType,
                                         uiElementType,
                                         uiElementVariableName,
                                         invokedMethodName,
                                         abstractMethod,
                                         expectedParameterType,
                                         passedParameterTypeName);
                            }
                        }
                    }
                    if (scannedInputEvent != null)
                    {
                        inputEvents.add(scannedInputEvent);
                    }
                }
            }
        }
        return inputEvents;
    }

    private InputEvent findInputEventOfInnerListeners(AbstractMethod abstractMethod,
                                                      ApkTypeEntry eventfulType,
                                                      RefType uiElementType,
                                                      String uiElementVariableName,
                                                      String invokedMethodName,
                                                      SootClass expectedParameterType,
                                                      String passedParameterTypeName)
    {
        InputEvent scannedInputEvent;//Get its outer type name, it should be something like this :
        // OuterType$InterfaceName
        String expectedParameterOuterTypeName =
                getExpectedParameterOuterTypeName(expectedParameterType);

        SootClass outerType = null;

        //Get the name of the expected parameter interface
        String expectedParameterInterfaceName =
                expectedParameterType.getName();

        //Get the supper Class of the UIElement, which defines a method
        // similar to the invoked one
        Optional<SootClass> potentialSupperClass =
                getSupperClassDefiningSimilarMethod
                        (invokedMethodName,
                         uiElementType,
                         expectedParameterInterfaceName);

        if (potentialSupperClass.isPresent())
        {
            //Get the potential supper class and its upper hierarchy
            List<SootClass> potentialSupperClassUpperHierarchy =
                    getUpperHierarchy(potentialSupperClass.get());

            //Find which one has a similar name to the outer type
            // defining the parameter interface type
            Optional<SootClass> potentialOuterTypeOfArgumentType =
                    potentialSupperClassUpperHierarchy
                            .stream()
                            .filter(sootClass -> sootClass.getName().equals
                                    (expectedParameterOuterTypeName))
                            .findFirst();

            //If that type does exist
            if (potentialOuterTypeOfArgumentType.isPresent())
            {
                //Then confirm that, that's the outer type of the
                // parameter interface type
                outerType = potentialOuterTypeOfArgumentType.get();
            }

        }

        //If the type of the passed argument is one of the APK classes
        if (allApkClasses.stream().anyMatch(apkC -> apkC.getName().equals
                (passedParameterTypeName)))
        {
            //
            //Case where the listener is an Non anonymous class
            // or an anonymous class not declared as a field
            //

            scannedInputEvent = getInputEventFromIdentifiedListener
                    (eventfulType,
                     uiElementType,
                     uiElementVariableName,
                     invokedMethodName,
                     abstractMethod,
                     outerType,
                     expectedParameterType,
                     passedParameterTypeName);
        }
        else
        {
            //
            //Case where the listener is an anonymous class declared as
            // Field
            //
            scannedInputEvent = getInputEventFromUnidentifiedListener
                    (eventfulType,
                     uiElementType,
                     uiElementVariableName,
                     invokedMethodName,
                     abstractMethod,
                     expectedParameterType,
                     passedParameterTypeName);
        }
        return scannedInputEvent;
    }





    private RefType getUIElementInvoker(JInvokeStmt statement)
    {
        return (RefType) ((VirtualInvokeExpr) statement
                .getInvokeExpr()).getBaseBox().getValue().getType();
    }

    private InputEvent getInputEventFromIdentifiedListener(ApkTypeEntry eventfulType,
                                                           RefType uiElementType,
                                                           String uiElementVariableName,
                                                           String invokedMethodName,
                                                           AbstractMethod abstractMethod,
                                                           SootClass outerType,
                                                           SootClass expectedParameterType,
                                                           String passedArgumentTypeName)
    {
        InputEvent scannedInputEvent = null;

        //Find the APK class representing the passed argument type (And that implement at least one interface)
        Optional<ApkClass> equivalentApkClass = allApkClasses.stream()
                .filter(apkC1 -> apkC1.getName().equals(passedArgumentTypeName))
                .filter(apkC1 -> (apkC1.getSootClass().getInterfaceCount() >= ONE) ||
                                 (apkC1.getSootClass().getSuperclass() != null))
                .findFirst();

        //If the APK class representing the passed argument type exists and
        // the outer type of the parameter type is a UIElement type

        if (equivalentApkClass.isPresent())
        {
            if (outerType != null)
            {
                if ((isUIElement(outerType))||
                    (isOrExtendingInnerClassOfUIElement(outerType)))
                {
                    scannedInputEvent = new InputEvent(uiElementType,
                                                       uiElementVariableName,
                                                       invokedMethodName,
                                                       abstractMethod,
                                                       eventfulType,
                                                       expectedParameterType,
                                                       equivalentApkClass.get());
                }
            }
            else
            {
                scannedInputEvent = new InputEvent(uiElementType,
                                                   uiElementVariableName,
                                                   invokedMethodName,
                                                   abstractMethod,
                                                   eventfulType,
                                                   expectedParameterType,
                                                   equivalentApkClass.get());
            }
        }
        return scannedInputEvent;
    }


    private InputEvent getInputEventFromUnidentifiedListener(ApkTypeEntry eventfulType,
                                                             RefType uiElementType,
                                                             String uiElementVariableName,
                                                             String invokedMethodName,
                                                             AbstractMethod abstractMethod,
                                                             SootClass expectedParameterType,
                                                             String passedParameterTypeName)
    {
        InputEvent scannedInputEvent = null;

        Optional<ApkClass> equivalentApkClass = allApkClasses.stream()
                .filter(apkC1 -> {
                    if (apkC1.getSootClass().getInterfaceCount() >= ONE)
                    {
                        return apkC1.getSootClass().getInterfaces().getFirst().getName().equals(expectedParameterType.getName());
                    }
                    else
                    {
                        if(apkC1.getSootClass().getSuperclass() != null)
                        {
                            return apkC1.getSootClass().getSuperclass().getName().equals(expectedParameterType.getName());
                        }
                        return false;
                    }
                })
                .findFirst();

        if (equivalentApkClass.isPresent())
        {
            scannedInputEvent = new InputEvent(uiElementType,
                                               uiElementVariableName,
                                               invokedMethodName,
                                               abstractMethod,
                                               eventfulType,
                                               expectedParameterType,
                                               equivalentApkClass.get());
        }
        return scannedInputEvent;
    }


    private Optional<SootClass> getSupperClassDefiningSimilarMethod(String invokedMethodName,
                                                     RefType uiElementType,
                                                     String expectedParameterInterfaceName)
    {
        Optional<SootClass> potentialSupperClass = null;
        if (uiElementType.hasSootClass())
        {

            //Get upper type hierarchy of concerned UIElement type
            List<SootClass> uiElementUpperHierarchy = getUpperHierarchy(uiElementType.getSootClass());
            String finalInvokedMethodName = invokedMethodName;
            potentialSupperClass = uiElementUpperHierarchy
                    .stream()
                    .filter(supperClass -> supperClass.getMethods()
                            .stream()
                            .filter(sootMethod -> sootMethod.getName().equals(finalInvokedMethodName) &&
                                                  sootMethod.getParameterCount() == ONE)
                            .filter(sootMethod -> sootMethod.getParameterType(ZERO) instanceof RefType)
                            .filter(sootMethod -> ((RefType) sootMethod.getParameterType(ZERO)).hasSootClass())
                            .anyMatch(sootMethod -> ((RefType) sootMethod.getParameterType(ZERO)).getSootClass().getName().equals(expectedParameterInterfaceName)))
                    .findFirst();
        }
        return potentialSupperClass;
    }


    private boolean isSingleRefTypeParameter(Unit statement)
    {
        boolean isSingleRefTypeParameter = false;
        //Accept invocation of methods with only ONE parameter
        int invokedMethodArgumentSize = ((JVirtualInvokeExpr) ((JInvokeStmt)statement).getInvokeExprBox().getValue()).getArgs().size();
        if (invokedMethodArgumentSize == ONE)
        {
            //Accept only a reference type parameter
            boolean isParameterRefType = isRefType(getInvokedMethod((JInvokeStmt) statement).parameterType(ZERO));
            if (isParameterRefType)
            {
                isSingleRefTypeParameter = true;
            }
        }
        return isSingleRefTypeParameter;
    }


    private boolean isInvokedMethodParameterInterfaceOrAbstractClass(Unit statement)
    {
        boolean isInvokedMethodParameterInterfaceOrAbstractClass = false;
        boolean invokedMethodParameterHasSootClass = ((RefType) getInvokedMethod((JInvokeStmt) statement).parameterType(ZERO)).hasSootClass();

        if (invokedMethodParameterHasSootClass)
        {
            isInvokedMethodParameterInterfaceOrAbstractClass = getExpectedParameterType((JInvokeStmt) statement).isInterface() ||
                                                               getExpectedParameterType((JInvokeStmt) statement).isAbstract() ;
        }

        return isInvokedMethodParameterInterfaceOrAbstractClass;
    }


    private SootClass getPassedParameterType(Unit statement)
    {
        SootClass passedArgumentType = null;

        ValueBox  argumentValueBox   = ((JVirtualInvokeExpr) ((JInvokeStmt) statement).getInvokeExprBox().getValue()).getArgBox(ZERO);
        if (argumentValueBox instanceof ImmediateBox)
        {
            if (isRefType(argumentValueBox.getValue().getType()))
            {
                boolean argumentTypeHasSootClass = ((RefType) argumentValueBox.getValue().getType()).hasSootClass();
                if (argumentTypeHasSootClass)
                {
                    passedArgumentType = ((RefType) argumentValueBox.getValue().getType()).getSootClass();
                }
            }
        }
        return passedArgumentType;
    }

    private SootClass getExpectedParameterType(JInvokeStmt statement)
    {
        return ((RefType) getInvokedMethod(statement).parameterType(ZERO)).getSootClass();
    }

    private SootMethodRef getInvokedMethod(JInvokeStmt statement)
    {
        return ((JVirtualInvokeExpr) statement.getInvokeExprBox().getValue()).getMethodRef();
    }

    private List<Unit> findStatementsOfUIElementsInvokingMethods(AbstractMethod abstractMethod)
    {
        Body body = getMethodBody(abstractMethod);

        List<Unit> statementsOfUIElementsInvokingMethods = new ArrayList<>();

        if(body != null)
        {
            if(body.getUnits() != null)
            {
                //Only statements that invoke methods returning Void and having one single
                // reference type parameter.
                List<Unit> statementsInvokingMethods = body.getUnits()
                        .stream()
                        .filter(unit -> unit instanceof JInvokeStmt)
                        .filter(unit -> ((JInvokeStmt)unit).getInvokeExpr() instanceof VirtualInvokeExpr)
                        .filter(unit -> ((VirtualInvokeExpr)((JInvokeStmt)unit).getInvokeExpr()).getBaseBox() instanceof JimpleLocalBox)
                        .filter(unit -> isRefType((((VirtualInvokeExpr)((JInvokeStmt)unit).getInvokeExpr()).getBaseBox()).getValue().getType()))
                        .filter(unit -> (getUIElementInvoker((JInvokeStmt) unit)).hasSootClass())
                        .filter(unit -> isReturnTypeVoid(unit))
                        .filter(unit -> isSingleRefTypeParameter(unit))
                        .collect(Collectors.toList());

                List<Unit> statementsOfUIElements = statementsInvokingMethods.stream()
                        .filter(unit ->  isUIElement((getUIElementInvoker((JInvokeStmt) unit)).getSootClass()))
                        .collect(Collectors.toList());

                List<Unit> statementsOfInnerTypesOfUIElements = statementsInvokingMethods
                        .stream()
                        .filter(unit -> isOrExtendingInnerClassOfUIElement((getUIElementInvoker((JInvokeStmt) unit)).getSootClass()))
                        .collect(Collectors.toList());

                statementsOfUIElementsInvokingMethods.addAll(statementsOfUIElements);
                statementsOfUIElementsInvokingMethods.addAll(statementsOfInnerTypesOfUIElements);
            }
        }

        return statementsOfUIElementsInvokingMethods;
    }

    private boolean isReturnTypeVoid(Unit statement)
    {
        return getInvokedMethod((JInvokeStmt) statement).returnType().equals(VoidType.v());
    }

}
