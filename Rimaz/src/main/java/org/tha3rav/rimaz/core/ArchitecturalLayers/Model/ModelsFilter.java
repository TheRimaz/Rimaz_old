package org.tha3rav.rimaz.core.ArchitecturalLayers.Model;

import org.tha3rav.rimaz.apk.ApkClass;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModelsFilter implements IModelsFilter
{
    private List<String> platformExcludedPackages;
    private List<String> platformIncludedPackages;
    private List<Model> allModels;

    public ModelsFilter(List<ApkClass> apkClasses, List<String> platformExcludedPackages, List<String> platformIncludedPackages)
    {
        this.platformExcludedPackages = platformExcludedPackages;
        this.platformIncludedPackages = platformIncludedPackages;
        ModelFactory modelFactory = new ModelFactory(platformExcludedPackages, platformIncludedPackages);
        allModels = new ArrayList<>();
        allModels.addAll(apkClasses.stream()
                                   .map(apkClass -> modelFactory.getModel(apkClass))
                                   .collect(Collectors.toList()));
    }

    @Override
    public List<Model> Filter()
    {
        List<Model> models = new ArrayList<>();

        models.addAll(getRawModels());
        models.addAll(getParcelableModels());
        models.addAll(getAutoValueModels());
        models.addAll(getFlatModels());
        models.addAll(getGSONSerializedModels());
        models.addAll(getSQLiteOpenHelperModels());
        models.addAll(getJavaIOSerializableModels());
        models.addAll(getLiteSuitsORMModels());

        return models;
    }

    @Override
    public List<BeanModel> getRawModels()
    {
        return allModels.stream()
                .filter(model -> model instanceof BeanModel)
                .map(model -> (BeanModel)model)
                .collect(Collectors.toList());

    }

    @Override
    public List<ParcelableModel> getParcelableModels()
    {
        return allModels.stream()
                        .filter(model -> model instanceof ParcelableModel)
                        .map(model -> (ParcelableModel)model)
                        .collect(Collectors.toList());
    }

    @Override
    public List<AutoValueModel> getAutoValueModels()
    {
        return allModels.stream()
                        .filter(model -> model instanceof AutoValueModel)
                        .map(model -> (AutoValueModel)model)
                        .collect(Collectors.toList());
    }

    @Override
    public List<FlatModel> getFlatModels()
    {
        return allModels.stream()
                .filter(model -> model instanceof FlatModel)
                .map(model -> (FlatModel)model)
                .collect(Collectors.toList());
    }

    @Override
    public List<GSONSerializedModel> getGSONSerializedModels()
    {
        return allModels.stream()
                .filter(model -> model instanceof GSONSerializedModel)
                .map(model -> (GSONSerializedModel)model)
                .collect(Collectors.toList());
    }

    @Override
    public List<SQLiteOpenHelperModel> getSQLiteOpenHelperModels()
    {
        return allModels.stream()
                .filter(model -> model instanceof SQLiteOpenHelperModel)
                .map(model -> (SQLiteOpenHelperModel)model)
                .collect(Collectors.toList());
    }

    @Override
    public List<JavaIOSerializableModel> getJavaIOSerializableModels()
    {
        return allModels.stream()
                .filter(model -> model instanceof JavaIOSerializableModel)
                .map(model -> (JavaIOSerializableModel)model)
                .collect(Collectors.toList());
    }

    @Override
    public List<LiteSuitsORMModel> getLiteSuitsORMModels()
    {
        return allModels.stream()
                .filter(model -> model instanceof LiteSuitsORMModel)
                .map(model -> (LiteSuitsORMModel)model)
                .collect(Collectors.toList());
    }
}
