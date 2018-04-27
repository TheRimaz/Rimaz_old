package org.tha3rav.rimaz.apk;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import soot.SootClass;

public class ApkTypeEntryFilter implements IApkTypeEntryFilter
{
    List<ApkTypeEntry> allApkTypeEntries;

    public ApkTypeEntryFilter(List<ApkTypeEntry> apkTypeEntries)
    {
        allApkTypeEntries = new ArrayList<>();
        ApkTypeEntryFactory apkTypeEntryFactory = new ApkTypeEntryFactory();
        allApkTypeEntries.addAll(apkTypeEntries.stream()
                                            .map(apkTypeEntry -> apkTypeEntryFactory.getApkTypeEntry(apkTypeEntry))
                                            .collect(Collectors.toList()));
    }

    @Override
    public List<ApkTypeEntry> Filter()
    {
        List<ApkTypeEntry> filteredTypes = new ArrayList<>();
        List<ApkClass> apkClasses = getApkClasses();
        List<ApkInterface> apkInterface = getApkInterface();
        filteredTypes.addAll(apkClasses);
        filteredTypes.addAll(apkInterface);
        return filteredTypes;
    }

    @Override
    public List<ApkClass> getApkClasses()
    {
        return allApkTypeEntries.stream()
                                .filter(apkTypeEntry -> apkTypeEntry instanceof ApkClass)
                                .map(apkTypeEntry -> (ApkClass) apkTypeEntry)
                                .collect(Collectors.toList());
    }

    @Override
    public List<ApkInterface> getApkInterface()
    {
        return allApkTypeEntries.stream()
                                .filter(apkTypeEntry -> apkTypeEntry instanceof ApkInterface)
                                .map(apkTypeEntry -> (ApkInterface) apkTypeEntry)
                                .collect(Collectors.toList());
    }
}
