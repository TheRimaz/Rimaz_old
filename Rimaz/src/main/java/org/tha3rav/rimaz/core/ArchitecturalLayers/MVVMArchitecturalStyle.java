package org.tha3rav.rimaz.core.ArchitecturalLayers;

import org.tha3rav.rimaz.core.ArchitecturalLayers.Model.Model;

import java.util.List;
import java.util.stream.Collectors;

public class MVVMArchitecturalStyle extends AbstractArchitecturalStyle
{
    public MVVMArchitecturalStyle(List<View> views,
                                 List<ViewModel> viewModels,
                                 List<Model> models)
    {
        super(views,
              viewModels.stream()
                      .map(presenter -> (MiddleArchitecturalLayer)presenter)
                      .collect(Collectors.toList()),
              models);
    }

    public MVVMArchitecturalStyle(List<ViewModel> viewModels,
                                 List<Model> models)
    {
        super(viewModels.stream()
                      .map(presenter -> (MiddleArchitecturalLayer)presenter)
                      .collect(Collectors.toList()),
              models);
    }

    public MVVMArchitecturalStyle(List<ViewModel> viewModels)
    {
        super(viewModels.stream()
                      .map(presenter -> (MiddleArchitecturalLayer)presenter)
                      .collect(Collectors.toList()));
    }

    public MVVMArchitecturalStyle()
    {
    }

    public List<ViewModel> getViewModelss()
    {
        return middleArchitecturalLayer.stream()
                .map(middleArchitecturalLayer -> (ViewModel)middleArchitecturalLayer)
                .collect(Collectors.toList());
    }

    public void getPresenters(List<ViewModel> viewModels)
    {
        this.middleArchitecturalLayer = viewModels.stream()
                .map(middleArchitecturalLayer -> (MiddleArchitecturalLayer)middleArchitecturalLayer)
                .collect(Collectors.toList());
    }

    @Override
    public String toString()
    {
        return "MVVM";
    }
}
