import org.tha3rav.rimaz.core.ArchitecturalLayers.AbstractArchitecturalStyle;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Main
{
    static private int timeOut = -1;
    static private String file = null;
    static private String directory = null;
    static private String outputFile = null;
    private static String platformsDirectory = null;

    public static void main(String[] args)
    {
        String result = null;
        if(args == null || args.length == 0)
        {
            System.out.println(Utils.ErrorMessages.NO_JAR_PARAMETERS_ERROR);
            return;
        }
        HashMap<String, Object> parameters = Helpers.ReadJarParams(args);


        List<String> applicationsPaths = getApplicationPaths(parameters);

        if (applicationsPaths == null)
        {
            return;
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();
        AbstractArchitecturalStyle  abstractArchitecturalStyle = null;
        Future<AbstractArchitecturalStyle> future = null;

        File file = null;
        String apkFileName = null;
        Date startingTime ;
        Date finishingTime ;
        Date duration;


        for (String applicationPath: applicationsPaths)
        {
            startingTime = new Date();
            file = new File(applicationPath);
            apkFileName = file.getName();
            executor = Executors.newSingleThreadExecutor();
            future = executor.submit(new AnalyzingTask(applicationPath, platformsDirectory));
            try
            {
                abstractArchitecturalStyle = future.get(timeOut, TimeUnit.MINUTES);
                finishingTime = new Date();

                duration = new Date(finishingTime.getTime() -  startingTime.getTime());
                DateFormat formatter     = new SimpleDateFormat("HH:mm:ss");
                String durationFormatted = formatter.format(duration);

                Helpers.PrintAnalysisResult(abstractArchitecturalStyle, applicationPath);

                result = String.format("%s,%s,%s",apkFileName,abstractArchitecturalStyle.toString(),durationFormatted);
                Helpers.PrintOutputResult(result, outputFile);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
                finishingTime = new Date();

                duration = new Date(finishingTime.getTime() -  startingTime.getTime());
                DateFormat formatter     = new SimpleDateFormat("HH:mm:ss");
                String durationFormatted = formatter.format(duration);

                result = String.format("%s,%s,%s",apkFileName,"Error",durationFormatted);
                Helpers.PrintOutputResult(result, outputFile);
            }
            catch (ExecutionException e)
            {
                if (e.getCause() instanceof OutOfMemoryError)
                {
                    System.out.println(Utils.ErrorMessages.GC_ERROR);
                    finishingTime = new Date();

                    duration = new Date(finishingTime.getTime() -  startingTime.getTime());
                    DateFormat formatter     = new SimpleDateFormat("HH:mm:ss");
                    String durationFormatted = formatter.format(duration);

                    result = String.format("%s,%s,%s",apkFileName,"GC_ERROR",durationFormatted);
                    Helpers.PrintOutputResult(result, outputFile);
                }
                else
                {
                    e.printStackTrace();
                    finishingTime = new Date();

                    duration = new Date(finishingTime.getTime() -  startingTime.getTime());
                    DateFormat formatter     = new SimpleDateFormat("HH:mm:ss");
                    String durationFormatted = formatter.format(duration);

                    result = String.format("%s,%s,%s",apkFileName,"ERROR",durationFormatted);
                    Helpers.PrintOutputResult(result, outputFile);
                }
            }
            catch (TimeoutException e)
            {
                System.out.println(String.format(Utils.ErrorMessages.TIMEOUT_ERROR,timeOut));
                finishingTime = new Date();

                duration = new Date(finishingTime.getTime() -  startingTime.getTime());
                DateFormat formatter     = new SimpleDateFormat("HH:mm:ss");
                String durationFormatted = formatter.format(duration);

                result = String.format("%s,%s,%s",apkFileName,"TIMEOUT_ERROR",durationFormatted);
                Helpers.PrintOutputResult(result, outputFile);
            }
            catch (OutOfMemoryError outOfMemoryError)
            {
                System.out.println(Utils.ErrorMessages.GC_ERROR);
                finishingTime = new Date();

                duration = new Date(finishingTime.getTime() -  startingTime.getTime());
                DateFormat formatter     = new SimpleDateFormat("HH:mm:ss");
                String durationFormatted = formatter.format(duration);

                result = String.format("%s,%s,%s",apkFileName,"GC_ERROR",durationFormatted);
                Helpers.PrintOutputResult(result, outputFile);
            }
        }



        System.out.println("Finished.");
        System.exit(0);
    }




    private static List<String> getApplicationPaths(HashMap<String, Object> parameters)
    {
        List<String> applicationsPaths = null;

        if (parameters.containsKey(Utils.ErrorMessages.DIRECTORY_PARAMETER_KEY)&&
            parameters.containsKey(Utils.ErrorMessages.FILE_PARAMETER_KEY))
        {
            System.out.println(String.format(Utils.ErrorMessages.DIRECTORY_AND_FILE_TOGETHER_PARAMETER_ERROR,
                                             Utils.ErrorMessages.DIRECTORY_PARAMETER_KEY,
                                             Utils.ErrorMessages.FILE_PARAMETER_KEY));
            return null;
        }
        else
        {
            if (!parameters.containsKey(Utils.ErrorMessages.DIRECTORY_PARAMETER_KEY)&&
                !parameters.containsKey(Utils.ErrorMessages.FILE_PARAMETER_KEY))
            {
                System.out.println(Utils.ErrorMessages.NO_SOURCE_PARAMETERS_ERROR);
                return null;
            }
            if(!parameters.containsKey(Utils.ErrorMessages.PLATFORMS_PARAMETER_KEY))
            {
                System.out.println(Utils.ErrorMessages.NO_PLATFORM_DIRECTORY_ERROR);
                return null;
            }
            if(!parameters.containsKey(Utils.ErrorMessages.TIMEOUT_PARAMETER_KEY))
            {
                System.out.println(Utils.ErrorMessages.NO_TIMEOUT_PARAMETER_ERROR);
                return null;
            }
        }

        if (parameters.containsKey(Utils.ErrorMessages.PLATFORMS_PARAMETER_KEY))
        {
            if (parameters.get(Utils.ErrorMessages.PLATFORMS_PARAMETER_KEY) == null)
            {
                System.out.println(Utils.ErrorMessages.INCORRECT_PLATFORMS_DIRECTORY_PARAMETER);
                return null;
            }
            platformsDirectory = parameters.get(Utils.ErrorMessages.PLATFORMS_PARAMETER_KEY).toString();
        }

        if (parameters.containsKey(Utils.ErrorMessages.TIMEOUT_PARAMETER_KEY))
        {
            timeOut = (int)parameters.get(Utils.ErrorMessages.TIMEOUT_PARAMETER_KEY);
            if (timeOut == -1)
            {
                System.out.println(Utils.ErrorMessages.INCORRECT_TIMEOUT_PARAMETER);
                return null;
            }
        }

        if (parameters.containsKey(Utils.ErrorMessages.FILE_PARAMETER_KEY))
        {
            if (parameters.get(Utils.ErrorMessages.FILE_PARAMETER_KEY) == null)
            {
                System.out.println(Utils.ErrorMessages.INCORRECT_FILE_PARAMETER);
                return null;
            }
            file = parameters.get(Utils.ErrorMessages.FILE_PARAMETER_KEY).toString();
        }

        if (parameters.containsKey(Utils.ErrorMessages.DIRECTORY_PARAMETER_KEY))
        {
            if (parameters.get(Utils.ErrorMessages.DIRECTORY_PARAMETER_KEY) == null)
            {
                System.out.println(Utils.ErrorMessages.INCORRECT_DIRECTORY_PARAMETER);
                return null;
            }
            directory = parameters.get(Utils.ErrorMessages.DIRECTORY_PARAMETER_KEY).toString();
        }

        try
        {
            applicationsPaths = Helpers.getListOfApplications(directory, file);
        }
        catch (IOException e)
        {
            System.out.println(Utils.ErrorMessages.INCORRECT_DIRECTORY_FILE_ERROR);
            return null;
        }

        if (parameters.containsKey(Utils.ErrorMessages.OUTPUT_PARAMETER_KEY))
        {
            if (parameters.get(Utils.ErrorMessages.OUTPUT_PARAMETER_KEY) != null)
            {
                outputFile = parameters.get(Utils.ErrorMessages.OUTPUT_PARAMETER_KEY).toString();
            }
        }

        return applicationsPaths;
    }
}
