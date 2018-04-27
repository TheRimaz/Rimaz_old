package org.tha3rav.rimaz.apk;

public class ApkTypeEntryFactory extends ApkTypeEntryAbstractFactory
{
    @Override
    public ApkTypeEntry getApkTypeEntry(ApkTypeEntry apkTypeEntry)
    {
        ApkTypeEntry apkType;

        if (isApkClass(apkTypeEntry))
        {
            apkType = new ApkClass(apkTypeEntry);
        }
        else
        {
            if (isApkInterface(apkTypeEntry))
            {
                apkType = new ApkInterface(apkTypeEntry);
            }
            else
            {
                apkType = new ApkTypeEntry(apkTypeEntry.getSootClass().getName(),
                                           apkTypeEntry.getSootClass(),
                                           apkTypeEntry.getInnerTypes(),
                                           apkTypeEntry.getFields(),
                                           apkTypeEntry.getOuterType());
            }
        }
        return apkType;
    }

    @Override
    public boolean isApkClass(ApkTypeEntry apkTypeEntry)
    {
        return !apkTypeEntry.getSootClass().isInterface();
    }

    @Override
    public boolean isApkInterface(ApkTypeEntry apkTypeEntry)
    {
        return apkTypeEntry.getSootClass().isInterface();
    }
}
