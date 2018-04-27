import org.tha3rav.rimaz.apk.Apk;
import org.tha3rav.rimaz.apk.ApkClass;
import org.tha3rav.rimaz.apk.ApkResourceFile;
import org.tha3rav.rimaz.apk.ApkTypeEntry;
import org.tha3rav.rimaz.core.AndroidSpecific.DataBindingClass;
import org.tha3rav.rimaz.core.ArchitecturalLayers.AbstractArchitecturalStyle;
import org.tha3rav.rimaz.core.ArchitecturalLayers.ActiveClass;
import org.tha3rav.rimaz.core.ArchitecturalLayers.Controller;
import org.tha3rav.rimaz.core.ArchitecturalLayers.MVCArchitecturalStyle;
import org.tha3rav.rimaz.core.ArchitecturalLayers.MVPArchitecturalStyle;
import org.tha3rav.rimaz.core.ArchitecturalLayers.MVVMArchitecturalStyle;
import org.tha3rav.rimaz.core.ArchitecturalLayers.Model.Model;
import org.tha3rav.rimaz.core.ArchitecturalLayers.ModelHelperClassFilter;
import org.tha3rav.rimaz.core.ArchitecturalLayers.NoArchitecturalStyle;
import org.tha3rav.rimaz.core.ArchitecturalLayers.Presenter;
import org.tha3rav.rimaz.core.ArchitecturalLayers.View;
import org.tha3rav.rimaz.core.ArchitecturalLayers.ViewModel;
import org.tha3rav.rimaz.core.SootLoader;
import org.tha3rav.rimaz.exceptions.ErroneousManifestFileException;
import org.tha3rav.rimaz.exceptions.UnfoundManifestFileException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import soot.options.Options;

import static org.tha3rav.rimaz.utils.SOOT.SootHelpers.getBlackListLibraries;

public class Helpers
{
    public static List<ApkResourceFile> getAllResourceFiles(Apk apk)
    {
        return apk.getEntries()
                .stream()
                .filter(apkEntry -> apkEntry instanceof ApkResourceFile)
                .map(apkEntry -> (ApkResourceFile) apkEntry)
                .collect(Collectors.toList());
    }

    public static List<ApkTypeEntry> getAllTypeEntries(Apk apk)
    {
        return apk.getEntries()
                .parallelStream()
                .filter(apkEntry -> apkEntry instanceof ApkTypeEntry)
                .map(apkEntry -> (ApkTypeEntry) apkEntry)
                .collect(Collectors.toList());
    }

    public static Apk getAllEntries(String applicationApkFilePath, String platformsDirectory) throws IOException
    {
        Apk apk = null;
        String sootOutputDirectory = ExperimentationData.SOOT_LOADER_OUTPUT_DIRECTORY;
        List<String> sootClasses = getBlackListLibraries();

        SootLoader sootLoader = new SootLoader(platformsDirectory,
                                               sootOutputDirectory,
                                               Options.output_format_jimple,
                                               sootClasses);
        try
        {
            apk = new Apk(applicationApkFilePath, sootLoader);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }
        catch (UnfoundManifestFileException e)
        {
            e.printStackTrace();
        }
        catch (ErroneousManifestFileException e)
        {
            e.printStackTrace();
        }
        return apk;
    }

    public static List<ApkClass> getClassesOnly(List<ApkTypeEntry> allTypeEntries)
    {
        return allTypeEntries.parallelStream()
                .filter(apkTypeEntry -> apkTypeEntry instanceof ApkClass)
                .map(apkTypeEntry -> (ApkClass)apkTypeEntry)
                .collect(Collectors.toList());
    }

    public static List<DataBindingClass> getDataBindingClasses(ModelHelperClassFilter modelHelperClassFilter)
    {
        return modelHelperClassFilter.getAndroidGeneratedClasses()
                .stream()
                .filter(androidGeneratedClass -> androidGeneratedClass instanceof DataBindingClass)
                .map(androidGeneratedClass -> (DataBindingClass)androidGeneratedClass)
                .collect(Collectors.toList());
    }

    public static List<Model> getModels(ModelHelperClassFilter modelHelperClassFilter)
    {
        return modelHelperClassFilter.getModels()
                .stream()
                .filter(model -> !modelHelperClassFilter.getAndroidGeneratedClasses()
                        .stream()
                        .anyMatch(androidGeneratedClass -> androidGeneratedClass.equals(model)))
                .collect(Collectors.toList());
    }

    public static void PrintAnalysisResult(AbstractArchitecturalStyle abstractArchitecturalStyle,
                                           String applicationPath)
    {
        System.out.print("Application : __");
        System.out.print(applicationPath);
        System.out.println("__  ");
        System.out.print("Has ");
        if (abstractArchitecturalStyle instanceof MVCArchitecturalStyle)
        {
            System.out.println("MVC Architectural style");

            System.out.println("");
            System.out.println("Models are :");
            System.out.println("");
            for (Model model: abstractArchitecturalStyle.getModels())
            {
                System.out.println(model.getName());
            }
            System.out.println("");
            System.out.println("Controllers are :");
            System.out.println("");
            for (Controller controller: ((MVCArchitecturalStyle) abstractArchitecturalStyle).getControllers())
            {
                System.out.println(controller.getName());
            }

        }
        else
        {
            if (abstractArchitecturalStyle instanceof MVPArchitecturalStyle)
            {
                System.out.println("MVP Architectural style");

                System.out.println("");
                System.out.println("Models are :");
                System.out.println("");
                for (Model model: abstractArchitecturalStyle.getModels())
                {
                    System.out.println(model.getName());
                }
                System.out.println("");
                System.out.println("Presenters are :");
                System.out.println("");
                for (Presenter presenter: ((MVPArchitecturalStyle) abstractArchitecturalStyle).getPresenters())
                {
                    System.out.println(presenter.getName());
                }
                System.out.println("");
                System.out.println("Views are :");
                System.out.println("");
                for (View view: abstractArchitecturalStyle.getViews().stream().filter(view -> view.get() instanceof ActiveClass).collect(Collectors.toList()))
                {
                    System.out.println(view.get().getName());
                }

            }
            else
            {
                if (abstractArchitecturalStyle instanceof MVVMArchitecturalStyle)
                {
                    System.out.println("MVVM Architectural style");

                    System.out.println("");
                    System.out.println("Models are :");
                    System.out.println("");
                    for (Model model: abstractArchitecturalStyle.getModels())
                    {
                        System.out.println(model.getName());
                    }
                    System.out.println("");
                    System.out.println("ViewModels are :");
                    System.out.println("");
                    for (ViewModel viewModel: ((MVVMArchitecturalStyle) abstractArchitecturalStyle).getViewModelss())
                    {
                        System.out.println(viewModel.getName());
                    }
                }
                else
                {
                    if (abstractArchitecturalStyle instanceof NoArchitecturalStyle)
                    {
                        System.out.println("No Architectural style");
                    }
                }
            }
        }
        System.out.println("");
        System.out.println("#####################################################################");
        System.out.println("");
    }

    public static HashMap<String, Object> ReadJarParams(String[] args)
    {
        HashMap<String,Object> parameters = new HashMap<>();
        List<String> argsAsList = Arrays.asList(args);


        String paramsDirectory = ReadPlatformDirectoryParameter(argsAsList);
        if (paramsDirectory != null)
        {
            parameters.put(Utils.ErrorMessages.PLATFORMS_PARAMETER_KEY, paramsDirectory);
        }

        int timeOut = ReadTimeOutParameter(argsAsList);
        if (timeOut != -1)
        {
            parameters.put(Utils.ErrorMessages.TIMEOUT_PARAMETER_KEY, timeOut);
        }

        String directory = ReadDirectoryParameter(argsAsList);
        if (directory != null)
        {
            parameters.put(Utils.ErrorMessages.DIRECTORY_PARAMETER_KEY, directory);
        }

        String file = ReadFileParameter(argsAsList);
        if (file != null)
        {
            parameters.put(Utils.ErrorMessages.FILE_PARAMETER_KEY, file);
        }

        String outputFile = ReadOutputParameter(argsAsList);
        if (outputFile != null)
        {
            parameters.put(Utils.ErrorMessages.OUTPUT_PARAMETER_KEY, outputFile);
        }
        return parameters;

    }

    public static String ReadPlatformDirectoryParameter(List<String> argsAsList)
    {

        String directory = null;
        if (argsAsList.contains(Utils.ErrorMessages.PLATFORMS_PARAMETER_KEY))
        {
            int positionOfDParameter = argsAsList.indexOf(Utils.ErrorMessages.PLATFORMS_PARAMETER_KEY);

            if (positionOfDParameter + 1 <= argsAsList.size() - 1)
            {
                if ((argsAsList.get(positionOfDParameter + 1).length() == 0)||
                    (argsAsList.get(positionOfDParameter + 1) == null))
                {
                    directory = null;
                }
                else
                {
                    directory = argsAsList.get(positionOfDParameter + 1);
                    Path directoryPath = null;
                    try
                    {
                        directoryPath = Paths.get(directory);
                        if (!Files.exists(directoryPath))
                        {
                            directory = null;
                        }
                    }
                    catch (InvalidPathException e)
                    {
                        directory = null;
                    }
                }
            }
        }
        return directory;
    }

    public static int ReadTimeOutParameter(List<String> argsAsList)
    {

        int timeOut = -1;
        if (argsAsList.contains(Utils.ErrorMessages.TIMEOUT_PARAMETER_KEY))
        {
            int positionOfTParameter = argsAsList.indexOf(Utils.ErrorMessages.TIMEOUT_PARAMETER_KEY);

            if (positionOfTParameter + 1 <= argsAsList.size() - 1)
            {
                try
                {
                    timeOut = Integer.parseUnsignedInt(argsAsList.get(positionOfTParameter + 1));
                    if (timeOut <= 0)
                    {
                        timeOut = -1;
                    }
                }
                catch (NumberFormatException e)
                {
                    timeOut = -1;
                }
            }
        }
        return timeOut;
    }

    public static String ReadDirectoryParameter(List<String> argsAsList)
    {

        String directory = null;
        if (argsAsList.contains(Utils.ErrorMessages.DIRECTORY_PARAMETER_KEY))
        {
            int positionOfDParameter = argsAsList.indexOf(Utils.ErrorMessages.DIRECTORY_PARAMETER_KEY);

            if (positionOfDParameter + 1 <= argsAsList.size() - 1)
            {
                if ((argsAsList.get(positionOfDParameter + 1).length() == 0)||
                    (argsAsList.get(positionOfDParameter + 1) == null))
                {
                    directory = null;
                }
                else
                {
                    directory = argsAsList.get(positionOfDParameter + 1);
                    Path directoryPath = null;
                    try
                    {
                        directoryPath = Paths.get(directory);
                        if (!Files.exists(directoryPath))
                        {
                            directory = null;
                        }
                    }
                    catch (InvalidPathException e)
                    {
                        directory = null;
                    }
                }
            }
        }
        return directory;
    }

    public static String ReadFileParameter(List<String> argsAsList)
    {

        String file = null;
        if (argsAsList.contains(Utils.ErrorMessages.FILE_PARAMETER_KEY))
        {
            int positionOfFParameter = argsAsList.indexOf(Utils.ErrorMessages.FILE_PARAMETER_KEY);

            if (positionOfFParameter + 1 <= argsAsList.size() - 1)
            {
                if ((argsAsList.get(positionOfFParameter + 1).length() == 0)||
                    (argsAsList.get(positionOfFParameter + 1) == null))
                {
                    file = null;
                }
                else
                {
                    file = argsAsList.get(positionOfFParameter + 1);
                    Path filePath = null;
                    try
                    {
                        filePath = Paths.get(file);
                        if (!Files.exists(filePath))
                        {
                            file = null;
                        }
                    }
                    catch (InvalidPathException e)
                    {
                        file = null;
                    }
                }
            }
        }
        return file;
    }

    public static String ReadOutputParameter(List<String> argsAsList)
    {
        String file = null;
        if (argsAsList.contains(Utils.ErrorMessages.OUTPUT_PARAMETER_KEY))
        {
            int positionOfOParameter = argsAsList.indexOf(Utils.ErrorMessages.OUTPUT_PARAMETER_KEY);

            if (positionOfOParameter + 1 <= argsAsList.size() - 1)
            {
                if ((argsAsList.get(positionOfOParameter + 1).length() == 0)||
                    (argsAsList.get(positionOfOParameter + 1) == null))
                {
                    file = null;
                }
                else
                {
                    file = argsAsList.get(positionOfOParameter + 1);
                    Path filePath = null;
                    try
                    {
                        filePath = Paths.get(file);
                        if (!Files.exists(filePath))
                        {
                            System.out.println(Utils.WarningMessages.OUTPUT_FILE_NOT_EXIST_WARNING);
                            System.out.println(String.format(Utils.WarningMessages.CREATING_OUTPUT_FILE_WARNING, file));
                            File yourFile = new File(file);
                            yourFile.createNewFile();
                        }
                    }
                    catch (InvalidPathException e)
                    {
                        file = null;
                    }
                    catch (IOException e)
                    {
                        System.out.println(Utils.ErrorMessages.ENABLE_CREATE_OUTPUT_ERROR);
                        file = null;
                    }
                }
            }
        }
        return file;
    }

    public static List<String> getListOfApplications(String directory, String file) throws IOException
    {
        List<String> applications = new ArrayList<>();
        if(directory != null)
        {
            Path source = Paths.get(directory);
            applications = Files.walk(source)
                    .filter(Files::isRegularFile)
                    .map(path -> path.toString())
                    .collect(Collectors.toList());
        }
        if (file != null)
        {
            applications = Arrays.asList(file);
        }
        return applications;
    }

    public static void PrintOutputResult(String result, String outputFile)
    {
        if (outputFile != null)
        {
            Path file = Paths.get(outputFile);
            try
            {
                Files.write(file, (result+System.lineSeparator()).getBytes(),
                            StandardOpenOption.APPEND);
            }
            catch (IOException e)
            {
                System.out.println(Utils.ErrorMessages.OUTPUT_ERROR);
            }
        }
    }
}