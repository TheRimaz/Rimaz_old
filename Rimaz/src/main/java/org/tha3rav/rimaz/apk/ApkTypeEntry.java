package org.tha3rav.rimaz.apk;

import org.tha3rav.rimaz.core.OOP.AbstractMethod;
import org.tha3rav.rimaz.core.OOP.Field;
import org.tha3rav.rimaz.core.OOP.Method;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import soot.ClassMember;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.tagkit.AbstractHost;
import soot.util.Numberable;

public class ApkTypeEntry extends ApkEntry
{
    protected SootClass            sootClass;
    protected List<Field>          fields;
    protected List<Method>         methods;
    protected List<AbstractMethod> allMethods;
    protected List<ApkTypeEntry>   innerTypes;
    protected Optional<ApkTypeEntry>  outerType;


    public ApkTypeEntry(String fullName, SootClass sootClass)
    {
        super(fullName);
        this.sootClass = sootClass;
        innerTypes = new ArrayList<>();
        fields = new ArrayList<>();
        allMethods = new ArrayList<>();
        outerType = Optional.empty();
    }

    public ApkTypeEntry(String fullName, SootClass sootClass, List<ApkTypeEntry> innerTypes)
    {
        super(fullName);
        this.sootClass = sootClass;
        this.innerTypes = innerTypes;
        fields = new ArrayList<>();
        allMethods = new ArrayList<>();
        outerType = Optional.empty();
    }

    public ApkTypeEntry(String fullName, SootClass sootClass, List<ApkTypeEntry> innerTypes, List<Field> fields)
    {
        super(fullName);
        this.sootClass = sootClass;
        this.innerTypes = innerTypes;
        this.fields = fields;
        allMethods = new ArrayList<>();
        outerType = Optional.empty();
    }

    public ApkTypeEntry(String fullName,
                        SootClass sootClass,
                        Optional<ApkTypeEntry> outerType)
    {
        super(fullName);
        this.sootClass = sootClass;
        this.outerType = outerType;
        innerTypes = new ArrayList<>();
        allMethods = new ArrayList<>();
        fields = new ArrayList<>();
    }

    public ApkTypeEntry(String fullName,
                        SootClass sootClass,
                        List<ApkTypeEntry> innerTypes,
                        Optional<ApkTypeEntry> outerType)
    {
        super(fullName);
        this.sootClass = sootClass;
        this.innerTypes = innerTypes;
        this.outerType = outerType;
        fields = new ArrayList<>();
        allMethods = new ArrayList<>();
    }

    public ApkTypeEntry(String fullName,
                        SootClass sootClass,
                        List<ApkTypeEntry> innerTypes,
                        List<Field> fields,
                        Optional<ApkTypeEntry> outerType)
    {
        super(fullName);
        this.sootClass = sootClass;
        this.innerTypes = innerTypes;
        this.fields = fields;
        this.outerType = outerType;
        allMethods = new ArrayList<>();
    }


    public <T extends AbstractHost & ClassMember & Numberable> List<T> getMembers()
    {
        List<T>          members = new ArrayList<>();
        List<SootField>  fields  = this.sootClass.getFields().stream().collect(Collectors.toList());
        List<SootMethod> methods = this.sootClass.getMethods().stream().collect(Collectors.toList());

        members.addAll((Collection<? extends T>) fields);
        members.addAll((Collection<? extends T>) methods);

        return  members;
    }

    public List<SootField> getPublicSootFields()
    {
        return this.sootClass
                .getFields()
                .stream()
                .filter(sootField -> sootField.isPublic())
                .collect(Collectors.toList());
    }

    public List<SootField> getPublicSootFields(boolean isStatic)
    {
        return this.sootClass
                .getFields()
                .stream()
                .filter(sootField -> sootField.isPublic() && sootField.isStatic() == isStatic)
                .collect(Collectors.toList());
    }

    public SootClass getSootClass()
    {
        return sootClass;
    }

    public List<Field> getFields()
    {
        return fields;
    }

    public void setFields(List<Field> fields)
    {
        this.fields = fields;
    }

    public List<AbstractMethod> getAllMethods()
    {
        return allMethods;
    }

    public void setAllMethods(List<AbstractMethod> allMethods)
    {
        this.allMethods = allMethods;
    }

    public List<Method> getMethods()
    {
        return methods;
    }

    public void setMethods(List<Method> methods)
    {
        this.methods = methods;
    }

    public List<ApkTypeEntry> getInnerTypes()
    {
        return innerTypes;
    }

    public void setInnerTypes(List<ApkTypeEntry> innerTypes)
    {
        this.innerTypes = innerTypes;
    }

    public Optional<ApkTypeEntry> getOuterType()
    {
        return outerType;
    }

    public void setOuterType(Optional<ApkTypeEntry> outerType)
    {
        this.outerType = outerType;
    }

    @Override
    public boolean equals(Object obj)
    {
        boolean isEqual = false;
        if (obj instanceof ApkTypeEntry)
        {
            isEqual = this.sootClass.equals(((ApkTypeEntry) obj).sootClass);
        }
        return isEqual;
    }
}
