package org.tha3rav.rimaz.core.ArchitecturalLayers;

import org.tha3rav.rimaz.apk.ApkClass;
import org.tha3rav.rimaz.apk.ApkTypeEntry;
import org.tha3rav.rimaz.core.AndroidSpecific.AndroidGeneratedClass;
import org.tha3rav.rimaz.core.AndroidSpecific.AndroidGeneratedTypesFilter;
import org.tha3rav.rimaz.core.AndroidSpecific.IAndroidGeneratedTypesFilter;
import org.tha3rav.rimaz.core.ArchitecturalLayers.Model.AutoValueModel;
import org.tha3rav.rimaz.core.ArchitecturalLayers.Model.IModelsFilter;
import org.tha3rav.rimaz.core.ArchitecturalLayers.Model.Model;
import org.tha3rav.rimaz.core.ArchitecturalLayers.Model.ModelsFilter;
import org.tha3rav.rimaz.core.ArchitecturalLayers.Model.ParcelableModel;
import org.tha3rav.rimaz.core.ArchitecturalLayers.Model.BeanModel;
import org.tha3rav.rimaz.core.Helper.HelperClass;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModelHelperClassFilter implements IModelHelperClassFilter
{
    private List<String>                 platformExcludedPackages;
    private List<String>                 platformIncludedPackages;
    private IModelsFilter                modelsFilter;
    private IAndroidGeneratedTypesFilter androidSpecificClassesFilter;
    private List<ApkClass>               allClasses;
    private List<Model>                  models;
    private List<HelperClass>            helperClasses;
    private List<AndroidGeneratedClass>  androidGeneratedClasses;

    public ModelHelperClassFilter(List<ApkClass> classes, List<String> platformExcludedPackages,List<String> platformIncludedPackages)
    {
        this.platformExcludedPackages = platformExcludedPackages;
        this.platformIncludedPackages = platformIncludedPackages;
        allClasses = classes;
        models = new ArrayList<>();
        helperClasses = new ArrayList<>();
        modelsFilter = new ModelsFilter(allClasses, platformExcludedPackages, platformIncludedPackages);
        androidSpecificClassesFilter = new AndroidGeneratedTypesFilter(allClasses.stream().map(apkClass -> (ApkTypeEntry) apkClass).collect(Collectors.toList()));
    }

    @Override
    public List<ApkClass> Filter()
    {
        List<ApkClass> filteredClasses = new ArrayList<>();
        models = modelsFilter.Filter();
        androidGeneratedClasses = androidSpecificClassesFilter.Filter();
        helperClasses = allClasses.stream()
                                  .filter(apkClass -> !models.contains(apkClass))
                                  .filter(apkClass -> !androidGeneratedClasses.contains(apkClass))
                                  .filter(apkClass -> !androidGeneratedClasses.stream()
                                                                             .anyMatch(androidSpecificClass -> androidSpecificClass.getInnerTypes().contains(apkClass)))
                                  .filter(apkClass -> isHelperClass(apkClass))
                                  .map(apkClass -> new HelperClass(apkClass))
                                  .collect(Collectors.toList());
        filteredClasses.addAll(models);
        filteredClasses.addAll(helperClasses);
        filteredClasses.addAll(androidGeneratedClasses);
        return filteredClasses;
    }

    @Override
    public boolean isModel(ApkClass apkClass)
    {
        boolean isModel;
        isModel = apkClass instanceof BeanModel ||
                  apkClass instanceof ParcelableModel ||
                  apkClass instanceof AutoValueModel;

        return isModel;
    }

    @Override
    public boolean isHelperClass(ApkClass apkClass)
    {
        boolean isHelperClass;
        isHelperClass = !isModel(apkClass) &&
                        !apkClass.isExtendingPlatformClass(platformExcludedPackages, platformIncludedPackages);

        return isHelperClass;
    }

    public List<Model> getModels()
    {
        return models;
    }

    public List<HelperClass> getHelperClasses()
    {
        return helperClasses;
    }

    public List<AndroidGeneratedClass> getAndroidGeneratedClasses()
    {
        return androidGeneratedClasses;
    }
}
