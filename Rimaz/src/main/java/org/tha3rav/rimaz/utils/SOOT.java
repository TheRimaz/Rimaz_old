package org.tha3rav.rimaz.utils;

import org.tha3rav.rimaz.apk.ApkTypeEntry;
import org.tha3rav.rimaz.core.OOP.AbstractMethod;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import soot.Body;
import soot.MethodSource;
import soot.RefType;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JInvokeStmt;
import soot.jimple.internal.JimpleLocal;
import soot.tagkit.InnerClassTag;
import soot.tagkit.SignatureTag;


import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.PACKAGES.ANDROID_DATABINDING;
import static org.tha3rav.rimaz.utils.OOP.OOPConstants.Numbers.AT_LEAST_FOUR_DIGIT_REGEX;
import static org.tha3rav.rimaz.utils.OOP.OOPConstants.Numbers.ZERO;
import static org.tha3rav.rimaz.utils.OOP.OOPConstants.Symbols.AT_LEAST_ONE_LETTER_REGEX;
import static org.tha3rav.rimaz.utils.OOP.OOPConstants.Symbols.DOLAR;
import static org.tha3rav.rimaz.utils.OOP.OOPConstants.Symbols.DOT;
import static org.tha3rav.rimaz.utils.OOP.OOPConstants.Symbols.EMPTY_STRING;
import static org.tha3rav.rimaz.utils.OOP.OOPConstants.Symbols.SLASH;
import static org.tha3rav.rimaz.utils.SOOT.SootConstants.SORTED_LIBRARIES_BLACKLIST;

public abstract class SOOT
{
    public static final class SootConstants
    {
        public static final String SORTED_LIBRARIES_BLACKLIST = "/SortedLibrariesBlackList.txt";

        public static class TAGS
        {
            public static final String VIEW_JAVA_SOURCE_FILE_TAG = "View.java";
            public static final String ANDROID_VIEW_VIEW_TAG = "android/view/View";
        }
    }

    public static final class SootHelpers
    {
        public static List<String> getBlackListLibraries() throws IOException
        {
            List<String> librariesBlackList;
            InputStream inputStream = SOOT.class.getResourceAsStream(SORTED_LIBRARIES_BLACKLIST);
            try (BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream)))
            {
                librariesBlackList =  buffer.lines().collect(Collectors.toList());
            }
            return librariesBlackList;
        }

        public static List<String> getPlatformIncludedPackages()
        {
            return Arrays.asList(ANDROID_DATABINDING);
        }

        public static List<SootClass> getSupperTypes(SootClass sootClass)
        {
            List <SootClass> supperTypes = new ArrayList<>();
            if (!sootClass.isInterface())
            {
                if (!sootClass.hasSuperclass())
                {
                    supperTypes = new ArrayList<>();
                    return supperTypes;
                }
                supperTypes = getSupperTypes(sootClass.getSuperclass());
                supperTypes.add(sootClass.getSuperclass());
            }
            else
            {
                if (sootClass.getInterfaceCount() == 0)
                {
                    supperTypes = new ArrayList<>();
                    return supperTypes;
                }
                for (SootClass sootInterface : sootClass.getInterfaces())
                {
                    supperTypes.addAll(getSupperTypes(sootInterface));
                }
                supperTypes.addAll(sootClass.getInterfaces());
            }

            return supperTypes;
        }

        public static List<SootClass> getUpperHierarchy(SootClass sootClass)
        {
            List <SootClass> upperHierarchy = new ArrayList<>();
            upperHierarchy.add(sootClass);
            upperHierarchy.addAll(getSupperTypes(sootClass));
            return upperHierarchy;
        }

        public static List<SootClass> geInterfaceImplementationsHierarchically(SootClass sootClass)
        {
            List <SootClass> interfaces = getUpperHierarchy(sootClass).stream()
                    .filter(upperSootClass -> upperSootClass.getInterfaceCount() > 0)
                    .map(upperSootClass -> upperSootClass.getInterfaces())
                    .flatMap(sootClasses -> sootClasses.stream())
                    .map(sootInterface -> getUpperHierarchy(sootInterface))
                    .flatMap(sootInterfaces -> sootInterfaces.stream())
                    .collect(Collectors.toList());
            return interfaces;
        }

        public static List<SootClass> getAllUpperTypes(SootClass sootClass)
        {
            return Stream.concat(geInterfaceImplementationsHierarchically(sootClass).stream(),
                                 getUpperHierarchy(sootClass).stream())
                    .collect(Collectors.toList());
        }

        //Does not work for interfaces
        public static List<SootClass> getOuterClasses(SootClass sootClass)
        {
            List <SootClass> outerClasses;
            if (!sootClass.hasOuterClass())
            {
                outerClasses = new ArrayList<>();
                return outerClasses;
            }
            outerClasses = getOuterClasses(sootClass.getOuterClass());
            outerClasses.add(sootClass.getOuterClass());
            return outerClasses;
        }

        public static List<SootClass> getOuterHierarchy(SootClass sootClass)
        {
            List <SootClass> outerHierarchy = new ArrayList<>();
            outerHierarchy.add(sootClass);
            outerHierarchy.addAll(getOuterClasses(sootClass));
            return outerHierarchy;
        }

        public static boolean isSubClassOf(SootClass sootClass, String superClassName)
        {
            if(sootClass == null)
            {
                return false;
            }
            return getUpperHierarchy(sootClass)
                    .stream()
                    .anyMatch(sootC -> sootC.getName().equals(superClassName));

        }

        //This is more correct than soot's isInnerClass
        public static boolean isExpectedParameterTypeInner(SootClass sootInterface)
        {
            boolean isExpectedParameterTypeInner = false;

            if (sootInterface.isInterface())
            {
                isExpectedParameterTypeInner = sootInterface.getTags().stream().anyMatch(tag -> tag instanceof InnerClassTag);
            }
            else
            {
                if (sootInterface.isAbstract())
                {
                    isExpectedParameterTypeInner = sootInterface.isInnerClass();
                }
            }
            return isExpectedParameterTypeInner;
        }

        public static String getExpectedParameterOuterTypeName(SootClass innerType)
        {
            String expectedParameterOuterTypeName = null;

            if (innerType.isInterface())
            {
                String innerTypeName = innerType.getName();
                String[] splittedinnerTypeName = innerTypeName.split(DOLAR);
                expectedParameterOuterTypeName = splittedinnerTypeName[ZERO];
            }
            else
            {
                expectedParameterOuterTypeName = innerType.getOuterClass().getName();
            }
            return expectedParameterOuterTypeName;
        }

        public static Body getMethodBody(AbstractMethod abstractMethod)
        {
            Body       methodBody   = null;
            SootMethod sootMethod = abstractMethod.getSootMethod();
            MethodSource methodSource = sootMethod.method().getSource();
            if(!sootMethod.isPhantom())
            {
                if(methodSource != null)
                {
                    try
                    {
                        methodBody = methodSource.getBody(sootMethod, EMPTY_STRING);
                    }
                    catch (NullPointerException e)
                    {
                        System.out.println(String.format("Couldn't get body of method : %s",sootMethod.getSignature()));
                    }

                }
            }
            return methodBody;
        }

        public static boolean isRefType(Type type)
        {
            return type instanceof RefType;
        }

        //Used for motherfucking java's fake generics !
        public static boolean fieldSignatureContainsType(SootField sootField, String typeName)
        {
            return sootField.getTags()
                    .stream()
                    .filter(tag -> tag instanceof SignatureTag)
                    .map(tag -> ((SignatureTag)tag).getSignature().toLowerCase().replace(SLASH,DOT))
                    .anyMatch(signature -> signature.contains(typeName.toLowerCase()));
        }

        //Used for motherfucking java's fake generics ! Again !!
        public static boolean typeHasGenericTypeParameter(ApkTypeEntry genericType, ApkTypeEntry genericTypeParameter)
        {
            return genericType.getSootClass().getTags()
                    .stream()
                    .filter(tag -> tag instanceof SignatureTag)
                    .map(tag -> ((SignatureTag)tag).getSignature().toLowerCase().replace(SLASH,DOT))
                    .anyMatch(signature -> signature.contains(genericTypeParameter.getSootClass().getShortName().toLowerCase()));
        }

        //Used for motherfucking java's fake generics ! Again !!!!!
        public static boolean typeHasGenericTypeParameter(SootClass genericType, ApkTypeEntry genericTypeParameter)
        {
            return genericType.getTags()
                    .stream()
                    .filter(tag -> tag instanceof SignatureTag)
                    .map(tag -> ((SignatureTag)tag).getSignature().toLowerCase().replace(SLASH,DOT))
                    .anyMatch(signature -> signature.contains(genericTypeParameter.getSootClass().getShortName().toLowerCase()));
        }

        public static boolean isReferenceTypeAssignment(Unit statement)
        {
            boolean isReferenceTypeAssignment = false;

            if (isAssignmentStatement(statement))
            {
                if (((JAssignStmt)statement).getLeftOpBox().getValue() instanceof JimpleLocal)
                {
                    if (((JAssignStmt)statement).getLeftOpBox().getValue().getType() instanceof RefType)
                    {
                        isReferenceTypeAssignment = true;
                    }
                }
            }

            return isReferenceTypeAssignment;
        }

        public static boolean isAssignmentStatement(Unit statement)
        {
            return statement instanceof JAssignStmt;
        }

        public static boolean isInvocationStatement(Unit statement)
        {
            return statement instanceof JInvokeStmt;
        }

        public static boolean isIndesirableGeneratedClass(SootClass sootClass)
        {
            boolean hasAutoGeneratedFields = sootClass.getFields().stream()
                    .anyMatch(sootField -> isMemberNameAutoGenerated(sootField.getName()));

            boolean hasAutoGeneratedMethods = sootClass.getMethods().stream()
                             .anyMatch(sootMethod -> isMemberNameAutoGenerated(sootMethod.getName()));

            return hasAutoGeneratedFields;
        }

        private static boolean isMemberNameAutoGenerated(String memberName)
        {
            Pattern pattern = Pattern.compile(AT_LEAST_FOUR_DIGIT_REGEX);
            Matcher matcher = pattern.matcher(memberName);

            boolean memberNameContainsTooMuchDigits = matcher.find();
            boolean memberNameContainsAtLeastOneLetter = memberName.matches(AT_LEAST_ONE_LETTER_REGEX);

            return memberNameContainsTooMuchDigits ||
                   !memberNameContainsAtLeastOneLetter;
        }
    }
}





