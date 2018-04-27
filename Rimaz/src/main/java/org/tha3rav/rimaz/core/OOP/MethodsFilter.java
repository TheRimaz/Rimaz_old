package org.tha3rav.rimaz.core.OOP;

import org.tha3rav.rimaz.apk.ApkTypeEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import soot.SootField;
import soot.SootMethod;

public class MethodsFilter implements IMethodsFilter
{
    private List<AbstractMethod> allMethods;

    public MethodsFilter(List<SootMethod> sootMethods, List<SootField> fields)
    {
        MethodFactory methodFactory = new MethodFactory();
        allMethods = new ArrayList<>();
        allMethods.addAll(sootMethods.stream()
                                     .map(sootMethod -> methodFactory.getMethod(sootMethod, fields))
                                     .collect(Collectors.toList()));
    }

    public MethodsFilter(ApkTypeEntry containingType, List<SootMethod> sootMethods, List<SootField> fields)
    {
        MethodFactory methodFactory = new MethodFactory();
        allMethods = new ArrayList<>();
        allMethods.addAll(sootMethods.stream()
                                  .map(sootMethod -> methodFactory.getMethod(containingType, sootMethod, fields))
                                  .collect(Collectors.toList()));
    }

    public List<Getter> getGetters()
    {
        return allMethods.stream()
                         .filter(abstractMethod -> abstractMethod instanceof Getter)
                         .map(abstractMethod -> (Getter)abstractMethod)
                         .collect(Collectors.toList());
    }

    public List<Setter> getSetters()
    {
        return allMethods.stream()
                         .filter(abstractMethod -> abstractMethod instanceof Setter)
                         .map(abstractMethod -> (Setter)abstractMethod)
                         .collect(Collectors.toList());
    }

    public List<Constructor> getConstructors()
    {
        return allMethods.stream()
                         .filter(abstractMethod -> abstractMethod instanceof Constructor)
                         .map(abstractMethod -> (Constructor)abstractMethod)
                         .collect(Collectors.toList());
    }

    public List<Method> getMethods()
    {
        return allMethods.stream()
                .filter(abstractMethod -> !(abstractMethod instanceof Constructor))
                .filter(abstractMethod -> !(abstractMethod instanceof Getter))
                .filter(abstractMethod -> !(abstractMethod instanceof Setter))
                .map(abstractMethod -> (Method)abstractMethod)
                .collect(Collectors.toList());
    }

    public List<AbstractMethod> getAllMethods()
    {
        return allMethods;
    }
}
