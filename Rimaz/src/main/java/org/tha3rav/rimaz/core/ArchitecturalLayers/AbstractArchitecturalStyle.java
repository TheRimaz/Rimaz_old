package org.tha3rav.rimaz.core.ArchitecturalLayers;

import org.tha3rav.rimaz.core.ArchitecturalLayers.Model.Model;

import java.util.List;

public abstract class AbstractArchitecturalStyle
{
    protected List<View>                     views;
    protected List<MiddleArchitecturalLayer> middleArchitecturalLayer;
    protected List<Model>                    models;

    public AbstractArchitecturalStyle(List<View> views,
                                      List<MiddleArchitecturalLayer> middleArchitecturalLayer,
                                      List<Model> models)
    {
        this.setViews(views);
        this.middleArchitecturalLayer = middleArchitecturalLayer;
        this.setModels(models);
    }

    public AbstractArchitecturalStyle(List<MiddleArchitecturalLayer> middleArchitecturalLayer,
                                      List<Model> models)
    {
        this.middleArchitecturalLayer = middleArchitecturalLayer;
        this.setModels(models);
    }

    public AbstractArchitecturalStyle(List<MiddleArchitecturalLayer> middleArchitecturalLayer)
    {
        this.middleArchitecturalLayer = middleArchitecturalLayer;
    }

    public AbstractArchitecturalStyle()
    {
    }

    public List<View> getViews()
    {
        return views;
    }

    public void setViews(List<View> views)
    {
        this.views = views;
    }

    public List<Model> getModels()
    {
        return models;
    }

    public void setModels(List<Model> models)
    {
        this.models = models;
    }
}
