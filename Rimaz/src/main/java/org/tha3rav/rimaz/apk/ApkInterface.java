package org.tha3rav.rimaz.apk;

import org.tha3rav.rimaz.core.OOP.Field;
import org.tha3rav.rimaz.core.OOP.Method;
import org.tha3rav.rimaz.core.OOP.MethodsFilter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import soot.SootClass;
import soot.SootField;

public class ApkInterface extends ApkTypeEntry
{
    public ApkInterface(String fullName,
                        SootClass sootClass)
    {
        super(fullName, sootClass);
    }

    public ApkInterface(String fullName,
                        SootClass sootClass,
                        List<ApkTypeEntry> innerTypes)
    {
        super(fullName, sootClass, innerTypes);
    }

    public ApkInterface(String fullName,
                        SootClass sootClass,
                        List<ApkTypeEntry> innerTypes,
                        List<Field> fields)
    {
        super(fullName, sootClass, innerTypes, fields);
    }

    public ApkInterface(String fullName,
                        SootClass sootClass,
                        Optional<ApkTypeEntry> outerType)
    {
        super(fullName, sootClass, outerType);
    }

    public ApkInterface(String fullName,
                        SootClass sootClass,
                        List<ApkTypeEntry> innerTypes,
                        Optional<ApkTypeEntry> outerType)
    {
        super(fullName, sootClass, innerTypes, outerType);
    }

    public ApkInterface(String fullName,
                        SootClass sootClass,
                        List<ApkTypeEntry> innerTypes,
                        List<Field> fields,
                        Optional<ApkTypeEntry> outerType)
    {
        super(fullName,
              sootClass,
              innerTypes,
              fields,
              outerType);
    }

    public ApkInterface(ApkTypeEntry apkTypeEntry)
    {
        super(apkTypeEntry.getSootClass().getName(),
              apkTypeEntry.getSootClass(),
              apkTypeEntry.getInnerTypes(),
              apkTypeEntry.getFields(),
              apkTypeEntry.getOuterType());
    }

    private void FilterFields()
    {
        fields.addAll(sootClass.getFields()
                               .stream()
                               .map(sootField -> new Field(sootField))
                               .collect(Collectors.toList()));
    }

    private void FilterMethods()
    {
        methods.addAll(sootClass.getMethods()
                                .stream()
                                .map(sootMethod -> new Method(sootMethod))
                                .collect(Collectors.toList()));
    }
}
