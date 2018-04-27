package org.tha3rav.rimaz.core.AndroidSpecific;

import org.tha3rav.rimaz.apk.ApkClass;
import org.tha3rav.rimaz.apk.ApkTypeEntry;
import org.tha3rav.rimaz.utils.SOOT;

import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.CLASSES.BR;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.CLASSES.BUILDCONFIG;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.CLASSES.R;
import static org.tha3rav.rimaz.utils.ANDROID.AndroidConstants.OBJECTS.VIEW_DATA_BINDING;
import static org.tha3rav.rimaz.utils.OOP.OOPConstants.Numbers.ZERO;

public class AndroidGeneratedTypesFactory extends AndroidGeneratedTypesAbstractFactory
{
    @Override
    public AndroidGeneratedClass getAndroidSpecificClass(ApkTypeEntry apkTypeEntry)
    {
        AndroidGeneratedClass androidGeneratedClass;

        if (isRClass(apkTypeEntry))
        {
            androidGeneratedClass = new RClass((ApkClass)apkTypeEntry);
        }
        else
        {
            if (isRInnerClass(apkTypeEntry))
            {
                androidGeneratedClass = new RInnerClass((ApkClass)apkTypeEntry);
            }
            else
            {
                if (isBRClass(apkTypeEntry))
                {
                    androidGeneratedClass = new BRClass((ApkClass)apkTypeEntry);
                }
                else
                {
                    if (isBuildConfigClass(apkTypeEntry))
                    {
                        androidGeneratedClass = new BuildConfigClass((ApkClass)apkTypeEntry);
                    }
                    else
                    {
                        if (isDataBindingClass(apkTypeEntry))
                        {
                            androidGeneratedClass = new DataBindingClass((ApkClass)apkTypeEntry);
                        }
                        else
                        {
                            androidGeneratedClass = new AndroidGeneratedClass((ApkClass)apkTypeEntry);
                        }
                    }
                }
            }
        }
        return androidGeneratedClass;
    }

    @Override
    public boolean isRClass(ApkTypeEntry apkTypeEntry)
    {
        return apkTypeEntry instanceof ApkClass &&
               apkTypeEntry.getSootClass().getShortName().equals(R) &&
               apkTypeEntry.getSootClass().isFinal() &&
               apkTypeEntry.getSootClass().isPublic() &&
               apkTypeEntry.getFields().size() == ZERO;
    }

    @Override
    public boolean isBRClass(ApkTypeEntry apkTypeEntry)
    {
        return apkTypeEntry instanceof ApkClass &&
               apkTypeEntry.getSootClass().getShortName().equals(BR) &&
               apkTypeEntry.getSootClass().isPublic() &&
               apkTypeEntry.getFields().stream().allMatch(field -> field.getSootField().isPublic() &&
                                                               field.getSootField().isStatic() &&
                                                               field.getSootField().isFinal());
    }

    @Override
    public boolean isRInnerClass(ApkTypeEntry apkTypeEntry)
    {
        boolean isRInnerClass = false;
        if (apkTypeEntry.getSootClass().isInnerClass())
        {
            isRInnerClass = apkTypeEntry instanceof ApkClass &&
                            apkTypeEntry.getSootClass().getOuterClass().getShortName().equals(R) &&
                            apkTypeEntry.getSootClass().getOuterClass().isFinal() &&
                            apkTypeEntry.getSootClass().getOuterClass().isPublic() &&
                            apkTypeEntry.getSootClass().getOuterClass().getFields().size() == ZERO &&
                            apkTypeEntry.getSootClass().isFinal() &&
                            // Had to take this off, since the bytecode representation doesn't
                            // mention that it is a static class, despite that it is actually in
                            // java code.
                            // apkTypeEntry.getSootClass().isStatic() &&
                            apkTypeEntry.getSootClass().isPublic() &&
                            apkTypeEntry.getFields().stream().allMatch(field -> field.getSootField().isPublic() &&
                                                                            field.getSootField().isStatic() &&
                                                                            field.getSootField().isFinal());
        }
        return isRInnerClass;
    }

    @Override
    public boolean isBuildConfigClass(ApkTypeEntry apkTypeEntry)
    {
        return apkTypeEntry instanceof ApkClass &&
               apkTypeEntry.getSootClass().getShortName().equals(BUILDCONFIG) &&
               apkTypeEntry.getSootClass().isFinal() &&
               apkTypeEntry.getSootClass().isPublic() &&
               apkTypeEntry.getFields().stream().allMatch(field -> field.getSootField().isPublic() &&
                                                               field.getSootField().isStatic() &&
                                                               field.getSootField().isFinal());
    }

    @Override
    public boolean isDataBindingClass(ApkTypeEntry apkTypeEntry)
    {
        return apkTypeEntry instanceof ApkClass &&
               apkTypeEntry.getSootClass().getInterfaceCount() == ZERO &&
               apkTypeEntry.getSootClass().isPublic() &&
               SOOT.SootHelpers.getSupperTypes(apkTypeEntry.getSootClass()).stream()
                       .filter(sootClass -> sootClass.getName().equals(VIEW_DATA_BINDING))
                       .findFirst().isPresent() ;
    }
}
