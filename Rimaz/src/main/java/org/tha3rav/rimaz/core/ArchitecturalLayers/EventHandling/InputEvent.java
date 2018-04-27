package org.tha3rav.rimaz.core.ArchitecturalLayers.EventHandling;

import org.tha3rav.rimaz.apk.ApkClass;
import org.tha3rav.rimaz.apk.ApkTypeEntry;
import org.tha3rav.rimaz.core.OOP.AbstractMethod;
import org.tha3rav.rimaz.core.OOP.Method;

import java.util.List;
import java.util.stream.Collectors;

import soot.RefType;
import soot.SootClass;

import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.getAllUpperTypes;

public class InputEvent extends Event
{
    private RefType        uiElementType;
    private String         uiElementVariableName;
    private String         setListenerMethodName;
    private AbstractMethod containingMethod;
    private ApkClass       listenerClass;
    private SootClass      listenerType;

    public InputEvent(RefType uiElementType,
                      String uiElementVariableName,
                      String setListenerMethodName,
                      AbstractMethod containingMethod,
                      ApkTypeEntry containingType,
                      SootClass listenerType,
                      ApkClass listenerClass)
    {
        super(containingType);
        this.uiElementType = uiElementType;
        this.uiElementVariableName = uiElementVariableName;
        this.setListenerMethodName = setListenerMethodName;
        this.containingMethod = containingMethod;
        this.listenerClass = listenerClass;
        this.listenerType = listenerType;
    }

    public RefType getUiElementType()
    {
        return uiElementType;
    }

    public void setUiElementType(RefType uiElementType)
    {
        this.uiElementType = uiElementType;
    }

    public String getUiElementVariableName()
    {
        return uiElementVariableName;
    }

    public void setUiElementVariableName(String uiElementVariableName)
    {
        this.uiElementVariableName = uiElementVariableName;
    }

    public AbstractMethod getContainingMethod()
    {
        return containingMethod;
    }

    public void setContainingMethod(AbstractMethod containingMethod)
    {
        this.containingMethod = containingMethod;
    }

    public ApkClass getListenerClass()
    {
        return listenerClass;
    }

    public void setListenerClass(ApkClass listenerClass)
    {
        this.listenerClass = listenerClass;
    }

    public String getSetListenerMethodName()
    {
        return setListenerMethodName;
    }

    public void setSetListenerMethodName(String setListenerMethodName)
    {
        this.setListenerMethodName = setListenerMethodName;
    }

    @Override
    public List<Method> getExecutionMethods()
    {
        return listenerClass.getMethods()
                .stream()
                .filter(implementedMethod -> getAllUpperTypes(listenerType)
                        .stream()
                        .map(upperType -> upperType.getMethods())
                        .flatMap(upperTypes -> upperTypes.stream())
                        .anyMatch(upperMethod -> (upperMethod.getName().equals(implementedMethod.getSootMethod().getName()))))
                .collect(Collectors.toList());
    }

    public SootClass getListenerType()
    {
        return listenerType;
    }

    public void setListenerType(SootClass listenerType)
    {
        this.listenerType = listenerType;
    }
}
