package org.tha3rav.rimaz.core.OOP;

import org.tha3rav.rimaz.apk.ApkClass;
import org.tha3rav.rimaz.apk.ApkInterface;
import org.tha3rav.rimaz.apk.ApkTypeEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import soot.SootClass;
import soot.SootMethod;
import soot.tagkit.EnclosingMethodTag;
import soot.tagkit.InnerClassTag;

import static org.tha3rav.rimaz.utils.OOP.OOPConstants.Numbers.ONE;

public class InnerTypesAssigner implements IInnerTypesAssigner
{
    private List<ApkTypeEntry> allTypeEntries;
    private List<ApkClass> innerAnonymousClasses;
    private List<ApkClass> innerInMethodAnonymousClasses;
    private List<ApkClass> innerInMethodNonAnonymousClasses;
    private List<ApkTypeEntry> innerNonAnonymousTypes;

    public InnerTypesAssigner(List<ApkTypeEntry> allTypeEntries)
    {
        this.allTypeEntries = allTypeEntries;
        innerAnonymousClasses = new ArrayList<>();
        innerNonAnonymousTypes = new ArrayList<>();
        innerInMethodNonAnonymousClasses = new ArrayList<>();
        innerInMethodAnonymousClasses = new ArrayList<>();
    }

    @Override
    public Tuple<List<ApkTypeEntry>,List<ApkTypeEntry>> AssignAndFilter()
    {
        List<ApkTypeEntry> outerTypes = allTypeEntries.stream()
                                                      .filter(outerType -> !outerType.getSootClass()
                                                                                     .isInnerClass())
                                                      .collect(Collectors.toList());

        List<ApkTypeEntry> allInnerTypes = allTypeEntries.stream()
                                                         .filter(apkClass -> apkClass.getSootClass()
                                                                                     .isInnerClass())
                                                         .collect(Collectors.toList());

        List<ApkTypeEntry> innerTypes;
        List<Field> fields;
        for (ApkTypeEntry outerType : outerTypes)
        {
            fields = outerType.getFields();
            innerTypes = getInnerTypesOfType(allInnerTypes, outerType);

            for (ApkTypeEntry innerType : innerTypes)
            {
                boolean typeIsField = typeIsField(fields, innerType);

                //If the type is a field in a type, then it is surely an anonymous class
                // Implementing an interface
                if (typeIsField)
                {
                    ApkClass innerClass = (ApkClass) innerType;
                    innerClass.setOuterType(Optional.of(outerType));
                    innerClass.setIsAnonymous(true);
                    outerType.getInnerTypes().add(innerClass);
                    innerAnonymousClasses.add(innerClass);
                }
                else
                {
                    boolean typeIsEnclosedByMethod = typeIsEnclosedByMethod(innerType);
                    //If the type is declared within a method, then it is a class that can go under
                    // two cases
                    if (typeIsEnclosedByMethod)
                    {
                        Method enclosingMethod = getEnclosingMethod(outerType, innerType);
                        ApkClass innerClass = (ApkClass) innerType;
                        innerClass.setOuterType(Optional.of(outerType));
                        innerClass.setEnclosingMethod(Optional.of(enclosingMethod));
                        outerType.getInnerTypes().add(innerClass);

                        boolean classHasName = classHasName(innerClass);
                        //It can be a non anonymous class
                        if (classHasName)
                        {
                            innerClass.setIsAnonymous(false);
                            innerInMethodNonAnonymousClasses.add(innerClass);
                        }
                        else
                        {
                            //It can be an anonymous class
                            innerClass.setIsAnonymous(true);
                            innerInMethodAnonymousClasses.add(innerClass);
                        }
                    }
                    else
                    {
                        //If the type isn't a field in a type, and not declared within a method
                        // then it is whether an interface
                        if (innerType instanceof ApkInterface)
                        {
                            ApkInterface innerInterface = (ApkInterface) innerType;
                            innerInterface.setOuterType(Optional.of(outerType));
                            outerType.getInnerTypes().add(innerInterface);
                            innerNonAnonymousTypes.add(innerInterface);
                        }
                        else
                        {
                            //Or a non anonymous class
                            if (innerType instanceof ApkClass)
                            {
                                ApkClass innerClass = (ApkClass) innerType;
                                innerClass.setOuterType(Optional.of(outerType));
                                outerType.getInnerTypes().add(innerClass);
                                innerNonAnonymousTypes.add(innerClass);
                            }
                        }
                    }
                }
            }
        }
        return new Tuple<>(Filter(),outerTypes);
    }

    private Method getEnclosingMethod(ApkTypeEntry outerType, ApkTypeEntry innerType)
    {
        String enclosingMethodName = getEnclosingMethodName(innerType);

        SootMethod sootEnclosingMethod = outerType.getSootClass()
                                                  .getMethods()
                                                  .stream()
                                                  .filter(sootMethod -> sootMethod.getName()
                                                                                  .equals(enclosingMethodName))
                                                  .findFirst()
                                                  .get();

        return new Method(sootEnclosingMethod);
    }

    private String getEnclosingMethodName(ApkTypeEntry innerType)
    {
        return innerType.getSootClass()
                        .getTags()
                        .stream()
                        .filter(tag -> tag instanceof EnclosingMethodTag)
                        .map(tag -> (EnclosingMethodTag)tag)
                        .findFirst()
                        .get()
                        .getEnclosingMethod();
    }

    private boolean typeIsEnclosedByMethod(ApkTypeEntry innerType)
    {
        return innerType.getSootClass()
                        .getTags()
                        .stream()
                        .anyMatch(tag -> tag instanceof EnclosingMethodTag);
    }

    private boolean typeIsField(List<Field> fields, ApkTypeEntry innerType)
    {
        boolean isField = false;
        List<SootClass> innerTypeInterfaces = innerType.getSootClass().getInterfaces().stream().collect(Collectors.toList());
        if (innerTypeInterfaces.size() == ONE)
        {
            isField = fields.stream()
                            .anyMatch(f -> f.getSootField()
                                            .getType()
                                            .equals(innerType.getSootClass()
                                                             .getInterfaces()
                                                             .getFirst()
                                                             .getType()));
        }
        return isField;
    }

    private boolean classHasName(ApkClass innerClass)
    {
        boolean hasName = false;
        Optional<InnerClassTag> innerClassTag = innerClass.getSootClass()
                                                          .getTags()
                                                          .stream()
                                                          .filter(tag -> tag instanceof InnerClassTag)
                                                          .map(tag -> (InnerClassTag)tag)
                                                          .findFirst();

        if(innerClassTag.isPresent())
        {
            if (innerClassTag.get().getShortName() != null)
            {
                hasName = true;
            }
        }

        return hasName;
    }

    private List<ApkTypeEntry> getInnerTypesOfType(List<ApkTypeEntry> allInnerTypes,
                                                   ApkTypeEntry outerType)
    {
        return allInnerTypes.stream()
                                  .filter(innerType -> innerType.getSootClass()
                                                                .getOuterClass()
                                                                .equals(outerType.getSootClass()))
                                  .collect(Collectors.toList());
    }

    private List<ApkTypeEntry> Filter()
    {
        List<ApkTypeEntry> innerTypes = new ArrayList<>();
        innerTypes.addAll(innerAnonymousClasses);
        innerTypes.addAll(innerNonAnonymousTypes);
        innerTypes.addAll(innerInMethodAnonymousClasses);
        innerTypes.addAll(innerInMethodNonAnonymousClasses);
        return innerTypes;
    }

    @Override
    public List<ApkClass> getInnerAnonymousClasses()
    {
        return innerAnonymousClasses;
    }

    @Override
    public List<ApkClass> getInnerInMethodAnonymousClasses()
    {
        return innerInMethodAnonymousClasses;
    }

    @Override
    public List<ApkClass> getInnerInMethodNonAnonymousClasses()
    {
        return innerInMethodNonAnonymousClasses;
    }

    @Override
    public List<ApkTypeEntry> getInnerNonAnonymousTypes()
    {
        return innerNonAnonymousTypes;
    }

}
