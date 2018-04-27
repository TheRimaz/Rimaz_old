package org.tha3rav.rimaz.core.AndroidSpecific;

import org.tha3rav.rimaz.apk.ApkTypeEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AndroidGeneratedTypesFilter implements IAndroidGeneratedTypesFilter
{
    private List<ApkTypeEntry> apkTypeEntries;

    public AndroidGeneratedTypesFilter(List<ApkTypeEntry> typeEntries)
    {
        this.apkTypeEntries = new ArrayList<>();
        AndroidGeneratedTypesFactory androidSpecificTypesFactory = new AndroidGeneratedTypesFactory();
        this.apkTypeEntries
            .addAll(typeEntries.stream()
                               .map(typeEntry -> androidSpecificTypesFactory.getAndroidSpecificClass(typeEntry))
                               .collect(Collectors.toList()));
    }

    @Override
    public List<AndroidGeneratedClass> Filter()
    {
        List<AndroidGeneratedClass> filteredClasses = new ArrayList<>();
        filteredClasses.addAll(getRClasses());
        filteredClasses.addAll(getBRClasses());
        filteredClasses.addAll(getRInnerClasses());
        filteredClasses.addAll(getBuildConfigClasses());
        filteredClasses.addAll(getDataBindingClasses());
        return filteredClasses;

    }

    @Override
    public List<RClass> getRClasses()
    {
        return apkTypeEntries.stream()
                         .filter(apkClass -> apkClass instanceof RClass)
                         .map(apkClass -> (RClass)apkClass)
                         .collect(Collectors.toList());
    }

    @Override
    public List<RInnerClass> getRInnerClasses()
    {
        return apkTypeEntries.stream()
                         .filter(apkClass -> apkClass instanceof RInnerClass)
                         .map(apkClass -> (RInnerClass)apkClass)
                         .collect(Collectors.toList());
    }

    @Override
    public List<BRClass> getBRClasses()
    {
        return apkTypeEntries.stream()
                         .filter(apkClass -> apkClass instanceof BRClass)
                         .map(apkClass -> (BRClass)apkClass)
                         .collect(Collectors.toList());
    }

    @Override
    public List<BuildConfigClass> getBuildConfigClasses()
    {
        return apkTypeEntries.stream()
                         .filter(apkClass -> apkClass instanceof BuildConfigClass)
                         .map(apkClass -> (BuildConfigClass)apkClass)
                         .collect(Collectors.toList());
    }

    @Override
    public List<DataBindingClass> getDataBindingClasses()
    {
        return apkTypeEntries.stream()
                         .filter(apkClass -> apkClass instanceof DataBindingClass)
                         .map(apkClass -> (DataBindingClass)apkClass)
                         .collect(Collectors.toList());
    }
}
