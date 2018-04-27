package org.tha3rav.rimaz.core.ArchitecturalLayers;

import org.tha3rav.rimaz.core.ArchitecturalLayers.Model.Model;

import java.util.List;
import java.util.stream.Collectors;

public class MVCArchitecturalStyle extends AbstractArchitecturalStyle
{
    public MVCArchitecturalStyle(List<View> views,
                                 List<Controller> controllers,
                                 List<Model> models)
    {
        super(views,
              controllers.stream()
                         .map(controller -> (MiddleArchitecturalLayer)controller)
                         .collect(Collectors.toList()),
              models);
    }

    public MVCArchitecturalStyle(List<Controller> controllers,
                                 List<Model> models)
    {
        super(controllers.stream()
                      .map(controller -> (MiddleArchitecturalLayer)controller)
                      .collect(Collectors.toList()),
              models);
    }

    public MVCArchitecturalStyle(List<Controller> controllers)
    {
        super(controllers.stream()
                      .map(controller -> (MiddleArchitecturalLayer)controller)
                      .collect(Collectors.toList()));
    }

    public MVCArchitecturalStyle()
    {
    }

    public List<Controller> getControllers()
    {
        return middleArchitecturalLayer.stream()
                .map(middleArchitecturalLayer -> (Controller)middleArchitecturalLayer)
                .collect(Collectors.toList());
    }

    public void setControllers(List<Controller> controllers)
    {
        this.middleArchitecturalLayer = controllers.stream()
                .map(middleArchitecturalLayer -> (MiddleArchitecturalLayer)middleArchitecturalLayer)
                .collect(Collectors.toList());
    }

    @Override
    public String toString()
    {
        return "MVC";
    }
}
