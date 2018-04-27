package org.tha3rav.rimaz.core.OOP;

import org.tha3rav.rimaz.apk.ApkTypeEntry;

import java.util.List;
import java.util.Optional;

import soot.SootField;
import soot.SootMethod;

public abstract class MethodAbstractFactory
{
    public abstract AbstractMethod getMethod(SootMethod sootMethod, List<SootField> fields);
    public abstract AbstractMethod getMethod(ApkTypeEntry containingType, SootMethod sootMethod, List<SootField> fields);

    public abstract Optional<Field> isSetter(SootMethod method, List<SootField> fields);
    public abstract Optional<Field> isGetter(SootMethod method, List<SootField> fields);
}
