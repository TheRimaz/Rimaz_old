package org.tha3rav.rimaz.core.AndroidSpecific;

import java.util.List;

public interface IAndroidGeneratedTypesFilter
{
    List<AndroidGeneratedClass> Filter();
    List<RClass> getRClasses();
    List<RInnerClass> getRInnerClasses();
    List<BRClass> getBRClasses();
    List<BuildConfigClass> getBuildConfigClasses();
    List<DataBindingClass> getDataBindingClasses();
}
