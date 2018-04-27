package org.tha3rav.rimaz.apk;

import org.tha3rav.rimaz.core.OOP.AbstractMethod;
import org.tha3rav.rimaz.core.OOP.Constructor;
import org.tha3rav.rimaz.core.OOP.Field;
import org.tha3rav.rimaz.core.OOP.Getter;
import org.tha3rav.rimaz.core.OOP.MethodsFilter;
import org.tha3rav.rimaz.core.OOP.Setter;
import org.tha3rav.rimaz.utils.SOOT;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import soot.SootClass;
import soot.SootField;
import soot.tagkit.EnclosingMethodTag;
import soot.tagkit.InnerClassTag;
import soot.tagkit.SignatureTag;
import soot.tagkit.Tag;

import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.OBJECTS.JAVA_LANG_OBJECT;


public class ApkClass extends ApkTypeEntry
{
    protected List<Getter>      getters;
    protected List<Setter>      setters;
    protected List<Constructor> constructors;
    protected Optional<AbstractMethod>  enclosingMethod;
    protected boolean           isAnonymous ;

    public ApkClass(String fullName, SootClass sootClass)
    {
        super(fullName, sootClass);
        getters = new ArrayList<>();
        setters = new ArrayList<>();
        constructors = new ArrayList<>();
        methods = new ArrayList<>();
        FilterMethods();
        FilterFields();
        isAnonymous = checkIfAnonymous();
    }

    public ApkClass(String fullName, SootClass sootClass, List<ApkTypeEntry> innerTypes)
    {
        super(fullName, sootClass, innerTypes);
        getters = new ArrayList<>();
        setters = new ArrayList<>();
        constructors = new ArrayList<>();
        methods = new ArrayList<>();
        FilterMethods();
        FilterFields();
        isAnonymous = checkIfAnonymous();
    }

    public ApkClass(String fullName, SootClass sootClass, List<ApkTypeEntry> innerTypes, List<Field> fields)
    {
        super(fullName, sootClass, innerTypes, fields);
        getters = new ArrayList<>();
        setters = new ArrayList<>();
        constructors = new ArrayList<>();
        methods = new ArrayList<>();
        FilterMethods();
        FilterFields();
        isAnonymous = checkIfAnonymous();
    }

    public ApkClass(String fullName,
                    SootClass sootClass,
                    Optional<ApkTypeEntry> outerType,
                    Optional<AbstractMethod> enclosingMethod)
    {
        super(fullName, sootClass, outerType);
        this.enclosingMethod = enclosingMethod;
        getters = new ArrayList<>();
        setters = new ArrayList<>();
        constructors = new ArrayList<>();
        methods = new ArrayList<>();
        FilterMethods();
        FilterFields();
        isAnonymous = checkIfAnonymous();
    }

    public ApkClass(String fullName,
                    SootClass sootClass,
                    List<ApkTypeEntry> innerTypes,
                    Optional<ApkTypeEntry> outerType,
                    Optional<AbstractMethod> enclosingMethod)
    {
        super(fullName, sootClass, innerTypes, outerType);
        this.enclosingMethod = enclosingMethod;
        getters = new ArrayList<>();
        setters = new ArrayList<>();
        constructors = new ArrayList<>();
        methods = new ArrayList<>();
        FilterMethods();
        FilterFields();
        isAnonymous = checkIfAnonymous();
    }

    public ApkClass(String fullName,
                    SootClass sootClass,
                    List<ApkTypeEntry> innerTypes,
                    List<Field> fields,
                    Optional<ApkTypeEntry> outerType,
                    Optional<AbstractMethod> enclosingMethod)
    {
        super(fullName, sootClass, innerTypes, fields, outerType);
        this.enclosingMethod = enclosingMethod;
        getters = new ArrayList<>();
        setters = new ArrayList<>();
        constructors = new ArrayList<>();
        methods = new ArrayList<>();
        FilterMethods();
        FilterFields();
        isAnonymous = checkIfAnonymous();
    }


    public ApkClass(ApkTypeEntry apkTypeEntry,
                    Optional<AbstractMethod> enclosingMethod)
    {
        super(apkTypeEntry.getSootClass().getName(),
              apkTypeEntry.getSootClass(),
              apkTypeEntry.getInnerTypes(),
              apkTypeEntry.getFields(),
              apkTypeEntry.getOuterType());
        this.enclosingMethod = enclosingMethod;
        getters = new ArrayList<>();
        setters = new ArrayList<>();
        constructors = new ArrayList<>();
        methods = new ArrayList<>();
        FilterMethods();
        FilterFields();
        isAnonymous = checkIfAnonymous();
    }

    public ApkClass(ApkTypeEntry apkTypeEntry)
    {
        super(apkTypeEntry.getSootClass().getName(),
              apkTypeEntry.getSootClass(),
              apkTypeEntry.getInnerTypes(),
              apkTypeEntry.getFields(),
              apkTypeEntry.getOuterType());
        this.enclosingMethod = Optional.empty();
        getters = new ArrayList<>();
        setters = new ArrayList<>();
        constructors = new ArrayList<>();
        methods = new ArrayList<>();
        FilterMethods();
        FilterFields();
        isAnonymous = checkIfAnonymous();
    }

    public boolean checkIfAnonymous()
    {
        boolean classIsAnonymous = false;

        List<Tag> tags                       = this.getSootClass().getTags();
        boolean   containsEnclosingMethodTag = tags.stream().anyMatch(tag -> tag instanceof EnclosingMethodTag);
        boolean   containsSignatureTag       = tags.stream().anyMatch(tag -> tag instanceof SignatureTag);
        boolean   containsInnerClassTag      = tags.stream().anyMatch(tag -> tag instanceof InnerClassTag);

        if(containsEnclosingMethodTag ||
           (containsSignatureTag &&
            containsInnerClassTag))
        {
            classIsAnonymous = true;
        }

        return classIsAnonymous;
    }

    public Optional<String> checkForEnclosingMethod()
    {
        Optional<String> enclosingMethodName = Optional.empty();

        List<Tag> tags = this.getSootClass().getTags();
        Optional<Tag> enclosingMethodTag = tags.stream()
                .filter(tag -> tag instanceof EnclosingMethodTag)
                .findAny();

        if(enclosingMethodTag.isPresent())
        {
            enclosingMethodName = Optional.of(((EnclosingMethodTag)enclosingMethodTag.get()).getEnclosingMethod());
        }

        return enclosingMethodName;
    }


    private void FilterFields()
    {
        for (SootField sootField: sootClass.getFields())
        {
            Optional<Getter> potentialGetter = getters.stream()
                                                      .filter(getter -> getter.getField().getSootField().equals(sootField))
                                                      .findAny();

            Optional<Setter> potentialSetter = setters.stream()
                                                      .filter(setter -> setter.getField().getSootField().equals(sootField))
                                                      .findAny();

            Field field = new Field(sootField, potentialGetter, potentialSetter);
/*

            //----------------------------------Critical :-------------------------------------
            //
            // ReAssigning the new reference of the constructed Field to the corresponding
            // getter and setter in order to maintain references.
            //
            potentialGetter.ifPresent(getter -> getter.setField(field));
            potentialSetter.ifPresent(setter -> setter.setField(field));
            //
            //----------------------------------End Critical.---------------------------------
*/

            fields.add(field);
        }
    }

    private void FilterMethods()
    {
        List<SootField> privateNonStaticSootFields = getPrivateSootFields(false);
        List<SootField> protectedNonStaticSootFields = getPrivateSootFields(false);
        MethodsFilter methodsFilter = new MethodsFilter(this, this.sootClass.getMethods(),
                                                        Stream.concat(privateNonStaticSootFields.stream(),
                                                                      protectedNonStaticSootFields.stream())
                                                              .collect(Collectors.toList()));
        allMethods = methodsFilter.getAllMethods();
        getters = methodsFilter.getGetters();
        setters = methodsFilter.getSetters();
        constructors = methodsFilter.getConstructors();
        methods = methodsFilter.getMethods();

    }

    public boolean isExtendingPlatformClass(List<String> platformExcludedPackages, List<String> platformIncludedPackages)
    {
        boolean isExtendingPlatformClass = false;
        List<SootClass> supperClasses = SOOT.SootHelpers.getSupperTypes(sootClass);

        for (String platformExcludedPackage: platformExcludedPackages)
        {
            for (SootClass sClass: supperClasses)
            {
                if ((sClass.getPackageName().contains(platformExcludedPackage)) && (!sClass.getName().equals(JAVA_LANG_OBJECT)))
                {
                    for (String platformIncludedPackage: platformIncludedPackages)
                    {
                        if (!sClass.getPackageName().contains(platformIncludedPackage))
                        {
                            isExtendingPlatformClass = true;
                            break;
                        }
                    }
                }
            }
        }
        return isExtendingPlatformClass;
    }

    public List<SootField> getPrivateSootFields()
    {
        return this.sootClass
                   .getFields()
                   .stream()
                   .filter(sootField -> sootField.isPrivate())
                   .collect(Collectors.toList());
    }

    public boolean isSubClassOf(String classFullName)
    {
        return SOOT.SootHelpers.getUpperHierarchy(this.sootClass).stream().anyMatch(sootC -> sootC.getName().equals(classFullName));
    }

    public boolean isSubClassOf(SootClass sootClass)
    {
        return SOOT.SootHelpers.getUpperHierarchy(this.sootClass).stream().anyMatch(sootC -> sootC.equals(sootClass));
    }

    public List<SootField> getPrivateSootFields(boolean isStatic)
    {
        return this.sootClass
                   .getFields()
                   .stream()
                   .filter(sootField -> sootField.isPrivate() && sootField.isStatic() == isStatic)
                   .collect(Collectors.toList());
    }

    public List<SootField> getProtectedSootFields()
    {
        return this.sootClass
                .getFields()
                .stream()
                .filter(sootField -> sootField.isProtected())
                .collect(Collectors.toList());
    }

    public List<SootField> getProtectedSootFields(boolean isStatic)
    {
        return this.sootClass
                .getFields()
                .stream()
                .filter(sootField -> sootField.isProtected() && sootField.isStatic() == isStatic)
                .collect(Collectors.toList());
    }

    public List<Constructor> getConstructors()
    {
        return constructors;
    }

    public void setConstructors(List<Constructor> constructors)
    {
        this.constructors = constructors;
    }

    public List<Getter> getGetters()
    {
        return getters;
    }

    public void setGetters(List<Getter> getters)
    {
        this.getters = getters;
    }

    public List<Setter> getSetters()
    {
        return setters;
    }

    public void setSetters(List<Setter> setters)
    {
        this.setters = setters;
    }

    public Optional<AbstractMethod> getEnclosingMethod()
    {
        return enclosingMethod;
    }

    public void setEnclosingMethod(Optional<AbstractMethod> enclosingMethod)
    {
        this.enclosingMethod = enclosingMethod;
    }

    public boolean isAnonymous()
    {
        return isAnonymous;
    }

    public void setIsAnonymous(boolean anonymous)
    {
        isAnonymous = anonymous;
    }

    @Override
    public boolean equals(Object obj)
    {
        boolean isEqual = false;
        if (obj instanceof ApkClass)
        {
            isEqual = this.sootClass.equals(((ApkClass) obj).sootClass);
        }
        return isEqual;
    }

}
