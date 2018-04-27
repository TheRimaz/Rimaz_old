package org.tha3rav.rimaz.apk;

import java.util.List;

public interface IApkTypeEntryFilter
{
    List<ApkTypeEntry> Filter();
    List<ApkClass> getApkClasses();
    List<ApkInterface> getApkInterface();
}
