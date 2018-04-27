package org.tha3rav.rimaz.core.OOP;

import org.tha3rav.rimaz.apk.ApkClass;
import org.tha3rav.rimaz.apk.ApkTypeEntry;

import java.util.List;

public interface IInnerTypesAssigner
{
    Tuple<List<ApkTypeEntry>,List<ApkTypeEntry>> AssignAndFilter();
    List<ApkClass> getInnerAnonymousClasses();
    List<ApkClass> getInnerInMethodAnonymousClasses();
    List<ApkClass> getInnerInMethodNonAnonymousClasses();
    List<ApkTypeEntry> getInnerNonAnonymousTypes();
}
