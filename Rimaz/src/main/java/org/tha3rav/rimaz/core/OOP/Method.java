package org.tha3rav.rimaz.core.OOP;

import org.tha3rav.rimaz.apk.ApkTypeEntry;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import soot.SootClass;
import soot.SootMethod;

import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.getUpperHierarchy;

public class Method extends AbstractMethod
{

    public Method(SootMethod sootMethod)
    {
        super(sootMethod);
    }

    public Method(SootMethod sootMethod,
                  ApkTypeEntry containingType)
    {
        super(sootMethod,
              containingType);
    }

    public Tuple<Boolean,SootClass> overridesOrImplementsMethodInUpperType()
    {
        AtomicReference<Tuple<Boolean, SootClass>> overridesOrImplementsMethod =
                new AtomicReference<>(new Tuple<>(false, null));

        SootClass declaringType = this.getSootMethod().getDeclaringClass();
        List<SootClass> upperHierarchy = getUpperHierarchy(declaringType);
        List<SootClass> implementedInterfaces = upperHierarchy.stream()
                .map(sootClass -> sootClass.getInterfaces())
                .flatMap(anInterface -> anInterface.stream())
                .collect(Collectors.toList());

        implementedInterfaces.stream()
                .filter(anInterface -> anInterface.getMethods()
                       .stream()
                       .anyMatch(stMethod -> stMethod.getSubSignature().equals(this.getSootMethod().getSubSignature())))
                .findFirst()
                .ifPresent(anInterface -> overridesOrImplementsMethod.set(new Tuple<>(true, anInterface)));

        return overridesOrImplementsMethod.get();
    }
}
