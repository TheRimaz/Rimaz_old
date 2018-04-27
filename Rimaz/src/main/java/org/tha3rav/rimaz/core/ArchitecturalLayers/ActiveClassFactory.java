package org.tha3rav.rimaz.core.ArchitecturalLayers;

import org.tha3rav.rimaz.apk.ApkClass;
import org.tha3rav.rimaz.utils.SOOT;

import soot.SootClass;

import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.OBJECTS.ANDROID_APP_ACTIVITY;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.OBJECTS.ANDROID_APP_APPLICATION;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.OBJECTS.ANDROID_APP_DIALOG;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.OBJECTS.ANDROID_APP_FRAGMENT;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.OBJECTS.ANDROID_VIEW_VIEW;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidHelpers.isOrExtendingInnerClassOfUIElement;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidHelpers.isUIElement;
import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.getAllUpperTypes;

public class ActiveClassFactory extends ActiveClassAbstractFactory
{

    @Override
    public ActiveClass getActiveClass(ApkClass apkClass)
    {
        ActiveClass activeClass = null;
        if (isApplicationClass(apkClass))
        {
            activeClass = new ApplicationClass(apkClass);
        }
        else
        {
            if (isActivityClass(apkClass))
            {
                activeClass = new ActivityClass(apkClass);
            }
            else
            {
                if(isFragmentClass(apkClass))
                {
                    activeClass = new FragmentClass(apkClass);
                }
                else
                {
                    if(isUIElementClass(apkClass))
                    {
                        activeClass = new UIElementClass(apkClass);
                    }
                    else
                    {
                        if (isDialogClass(apkClass))
                        {
                            activeClass = new DialogClass(apkClass);
                        }
                        else
                        {
                            activeClass = new ActiveClass(apkClass);
                        }
                    }
                }
            }
        }
        return activeClass;
    }

    @Override
    public boolean isApplicationClass(ApkClass apkClass)
    {
        return getAllUpperTypes(apkClass.getSootClass()).stream()
                .anyMatch(sootClass -> sootClass.getName().endsWith(ANDROID_APP_APPLICATION));
    }

    @Override
    public boolean isActivityClass(ApkClass apkClass)
    {
        return getAllUpperTypes(apkClass.getSootClass()).stream()
                    .anyMatch(sootClass -> sootClass.getName().endsWith(ANDROID_APP_ACTIVITY));
    }

    @Override
    public boolean isFragmentClass(ApkClass apkClass)
    {
        return getAllUpperTypes(apkClass.getSootClass()).stream()
                .anyMatch(sootClass -> sootClass.getName().endsWith(ANDROID_APP_FRAGMENT));
    }

    @Override
    public boolean isUIElementClass(ApkClass apkClass)
    {
        return isUIElement(apkClass.getSootClass()) ||
               isOrExtendingInnerClassOfUIElement(apkClass.getSootClass());
    }

    @Override
    public boolean isDialogClass(ApkClass apkClass)
    {
        return getAllUpperTypes(apkClass.getSootClass()).stream()
                .anyMatch(sootClass -> sootClass.getName().endsWith(ANDROID_APP_DIALOG));
    }
}
