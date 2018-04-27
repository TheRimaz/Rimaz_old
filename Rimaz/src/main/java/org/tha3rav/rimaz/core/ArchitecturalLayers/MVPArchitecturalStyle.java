package org.tha3rav.rimaz.core.ArchitecturalLayers;

import org.tha3rav.rimaz.core.ArchitecturalLayers.Model.Model;

import java.util.List;
import java.util.stream.Collectors;

public class MVPArchitecturalStyle extends AbstractArchitecturalStyle
{
    public MVPArchitecturalStyle(List<View> views,
                                 List<Presenter> presenters,
                                 List<Model> models)
    {
        super(views,
              presenters.stream()
                      .map(presenter -> (MiddleArchitecturalLayer)presenter)
                      .collect(Collectors.toList()),
              models);
    }

    public MVPArchitecturalStyle(List<Presenter> presenters,
                                 List<Model> models)
    {
        super(presenters.stream()
                      .map(presenter -> (MiddleArchitecturalLayer)presenter)
                      .collect(Collectors.toList()),
              models);
    }

    public MVPArchitecturalStyle(List<Presenter> presenters)
    {
        super(presenters.stream()
                      .map(presenter -> (MiddleArchitecturalLayer)presenter)
                      .collect(Collectors.toList()));
    }

    public MVPArchitecturalStyle()
    {
    }

    public List<Presenter> getPresenters()
    {
        return middleArchitecturalLayer.stream()
                .map(middleArchitecturalLayer -> (Presenter)middleArchitecturalLayer)
                .collect(Collectors.toList());
    }

    public void getPresenters(List<Presenter> presenters)
    {
        this.middleArchitecturalLayer = presenters.stream()
                .map(middleArchitecturalLayer -> (MiddleArchitecturalLayer)middleArchitecturalLayer)
                .collect(Collectors.toList());
    }

    @Override
    public String toString()
    {
        return "MVP";
    }
}
