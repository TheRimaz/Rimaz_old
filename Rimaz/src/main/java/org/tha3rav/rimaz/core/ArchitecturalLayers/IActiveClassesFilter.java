package org.tha3rav.rimaz.core.ArchitecturalLayers;

import java.util.List;

public interface IActiveClassesFilter
{
    List<ActiveClass> Filter();
    List<ApplicationClass> getApplicationClasses();
    List<ActivityClass> getActivityClasses();
    List<FragmentClass> getFragmentClasses();
    List<UIElementClass> getUIElementClasses();
    List<DialogClass> getDialogClasses();
}
