import org.tha3rav.rimaz.apk.Apk;
import org.tha3rav.rimaz.apk.ApkClass;
import org.tha3rav.rimaz.apk.ApkResourceFile;
import org.tha3rav.rimaz.apk.ApkTypeEntry;
import org.tha3rav.rimaz.core.AndroidSpecific.DataBindingClass;
import org.tha3rav.rimaz.core.ArchitecturalLayers.AbstractArchitecturalStyle;
import org.tha3rav.rimaz.core.ArchitecturalLayers.ActiveClass;
import org.tha3rav.rimaz.core.ArchitecturalLayers.ActiveClassesFilter;
import org.tha3rav.rimaz.core.ArchitecturalLayers.ArchitecturalStyleIdentifier;
import org.tha3rav.rimaz.core.ArchitecturalLayers.Model.Model;
import org.tha3rav.rimaz.core.ArchitecturalLayers.ModelHelperClassFilter;
import org.tha3rav.rimaz.core.ArchitecturalLayers.UIElementClass;
import org.tha3rav.rimaz.core.Helper.HelperClass;
import org.tha3rav.rimaz.utils.ANDROID;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class AnalyzingTask implements Callable<AbstractArchitecturalStyle>
{

    private String applicationApkFilePath;
    private String platformsDirectory;

    public AnalyzingTask(String applicationApkFilePath, String platformsDirectory)
    {
        this.applicationApkFilePath = applicationApkFilePath;
        this.platformsDirectory = platformsDirectory;
    }

    @Override
    public AbstractArchitecturalStyle call()
    {
        System.out.println(String.format("Started Analyzing %s :",applicationApkFilePath));

        Apk apk = null;
        try
        {
            apk = Helpers.getAllEntries(applicationApkFilePath, platformsDirectory);
        }
        catch (IOException e)
        {
            System.out.println("Couldn't find or read libraries black list file");
        }

        if (apk == null)
        {
            return null;
        }

        //Get all type Entries, this contain Interfaces and Classes filtered apart
        List<ApkTypeEntry> allTypeEntries = Helpers.getAllTypeEntries(apk);

        List<ApkResourceFile> resourceFiles = Helpers.getAllResourceFiles(apk);

        //get class entries only
        List<ApkClass> classEntries = Helpers.getClassesOnly(allTypeEntries);

        ANDROID.AndroidUtils androidUtils             = new ANDROID.AndroidUtils();
        List<String>         platformExcludedPackages = androidUtils.getPlatformExcludedPackages();
        List<String>         platformIncludedPackages = androidUtils.getPlatformIncludedPackages();

        //Filter model, helper classes, and android specific classes
        ModelHelperClassFilter modelHelperClassFilter = new ModelHelperClassFilter(classEntries, platformExcludedPackages, platformIncludedPackages);
        modelHelperClassFilter.Filter();

        List<Model> models = Helpers.getModels(modelHelperClassFilter);

        List<DataBindingClass> dataBindingClasses = Helpers.getDataBindingClasses(modelHelperClassFilter);

        List<HelperClass> helperClasses = modelHelperClassFilter.getHelperClasses();

        ActiveClassesFilter activeClassesFilter = new ActiveClassesFilter(classEntries);
        List<ActiveClass>   activeClasses       =  activeClassesFilter.Filter().stream().filter(activeClass -> !(activeClass instanceof UIElementClass)).collect(Collectors.toList());

        List<ApkClass> filteredClasses = new ArrayList<>();

        filteredClasses.addAll(models);
        filteredClasses.addAll(helperClasses);
        filteredClasses.addAll(activeClasses);
        filteredClasses.addAll(dataBindingClasses);

        ArchitecturalStyleIdentifier architecturalStyleIdentifier = new ArchitecturalStyleIdentifier(models,
                                                                                                     filteredClasses,
                                                                                                     allTypeEntries,
                                                                                                     resourceFiles);
        AbstractArchitecturalStyle architecturalStyle = architecturalStyleIdentifier
                .identifyArchitecturalStyle();

        return architecturalStyle;
    }

}