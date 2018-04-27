package org.tha3rav.rimaz.core.ArchitecturalLayers;
import org.tha3rav.rimaz.core.ArchitecturalLayers.Model.Model;

import java.util.List;
import java.util.stream.Collectors;

public class NoArchitecturalStyle extends AbstractArchitecturalStyle
{
    public NoArchitecturalStyle(List<View> views,
                                List<MiddleArchitecturalLayer> middleArchitecturalLayer,
                                List<Model> models)
    {
        super(views,
              middleArchitecturalLayer,
              models);
    }

    public NoArchitecturalStyle(List<MiddleArchitecturalLayer> middleArchitecturalLayer, List<Model> models)
    {
        super(middleArchitecturalLayer, models);
    }

    public NoArchitecturalStyle(List<Controller> controllers)
    {
        super(controllers.stream()
                      .map(controller -> (MiddleArchitecturalLayer)controller)
                      .collect(Collectors.toList()));
    }

    public NoArchitecturalStyle()
    {
    }

    @Override
    public String toString()
    {
        return "NONE";
    }
}
