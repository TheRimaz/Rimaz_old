package org.tha3rav.rimaz.core.ArchitecturalLayers;

import org.tha3rav.rimaz.apk.ApkClass;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ActiveClassesFilter implements IActiveClassesFilter
{
    private List<ActiveClass> allActiveClasses;

    public ActiveClassesFilter(List<ApkClass> apkClasses)
    {
        ActiveClassFactory activeClassFactory = new ActiveClassFactory();
        allActiveClasses = new ArrayList<>();
        allActiveClasses.addAll(apkClasses.stream()
                                          .map(apkClass -> activeClassFactory.getActiveClass(apkClass))
                                          .collect(Collectors.toList()));
    }

    @Override
    public List<ActiveClass> Filter()
    {
        List<ActiveClass> activeClasses = new ArrayList<>();
        activeClasses.addAll(getApplicationClasses());
        activeClasses.addAll(getActivityClasses());
        activeClasses.addAll(getFragmentClasses());
        activeClasses.addAll(getUIElementClasses());
        activeClasses.addAll(getDialogClasses());
        return activeClasses;
    }

    @Override
    public List<ApplicationClass> getApplicationClasses()
    {
        return allActiveClasses.stream()
                               .filter(activeClass -> activeClass instanceof ApplicationClass)
                               .map(activeClass -> (ApplicationClass)activeClass)
                               .collect(Collectors.toList());
    }

    @Override
    public List<ActivityClass> getActivityClasses()
    {
        return allActiveClasses.stream()
                               .filter(activeClass -> activeClass instanceof ActivityClass)
                               .map(activeClass -> (ActivityClass)activeClass)
                               .collect(Collectors.toList());
    }

    @Override
    public List<FragmentClass> getFragmentClasses()
    {
        return allActiveClasses.stream()
                               .filter(activeClass -> activeClass instanceof FragmentClass)
                               .map(activeClass -> (FragmentClass)activeClass)
                               .collect(Collectors.toList());
    }

    @Override
    public List<UIElementClass> getUIElementClasses()
    {
        return allActiveClasses.stream()
                .filter(activeClass -> activeClass instanceof UIElementClass)
                .map(activeClass -> (UIElementClass)activeClass)
                .collect(Collectors.toList());
    }

    @Override
    public List<DialogClass> getDialogClasses()
    {
        return allActiveClasses.stream()
                .filter(activeClass -> activeClass instanceof DialogClass)
                .map(activeClass -> (DialogClass)activeClass)
                .collect(Collectors.toList());
    }
}
