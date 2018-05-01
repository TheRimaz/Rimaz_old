
# Rimaz

## Introduction
This is an experimentation version of the **Rimaz** Tool.
Rimaz is an Arabic word that means "code" like in "source code" , written in Arabic : **رِماز** or **الرِّماز**

Rimaz is a tool that provides :
 - The detection of Android application main components, such as *Custom UIElements* (Views), *Activities*, *Fragments*, *Dialogs*, *Application* classes etc. ;
- The detection of Android application Input events, including :   
	- **Listener based** input events;   
	- Life cycle and non life cycle **Event handlers**;
- The detection of *Model entities* that might be used in an Android application;
- The detection of the dominant presentation layer architectural style that is used in an Android application including (**MVC**, **MVP**, **MVVM**);

Rimaz uses [SOOT](https://github.com/Sable/soot) framework on parse Android applications bytecode, for this reason, Rimaz needs some components related to SOOT to perform its analyses, such as [Android platforms](https://github.com/Sable/soot/wiki/Instrumenting-Android-Apps-with-Soot)

## Using Rimaz

The "Expermentation" package uses the Rimaz tool, it is the "Expermentation" application that you should use to apply analyses on 
an Android application.

You can get the Expermentation jar directly from this [link](https://github.com/TheRimaz/Rimaz/blob/master/Expermentation/out/artifacts/Expermentation_jar/Experimentation/Expermentation.jar)

Expermentation jar must be run with the following parameters :

 - **-p** followed by a path to the directory containing [Android platform files](https://github.com/Sable/android-platforms) that [SOOT](https://github.com/Sable/soot) uses to perform the
   analyses.
 - **-t** followed by an positive integer for setting a timeout (in minutes)
 - **-d** followed by a path to the directory containing the applications to be analyzed.    
 - **-o** followed by a path to a csv file that represents
   the analyses output file.

We highly recommend increasing the jvm heap size to successfully analyze big applications.

Android platform files should be downloaded locally to use Rimaz.

For example, to run the Expermentation jar for a 60 minutes timeout for each application that is contained in a specific directory you can use the following command :
 

    java -jar path_to_Expermentation.jar -p path_to_AndroidPlatforms -t 60
    -d path_to_appsDirectory -o path_to_output.csv

Rimaz filters out the used libraries in an application by using an extensive blacklist of widely used libraries, this list contains :
 - More than 1300 library package names that where constructed by Li et al. in their study [An Investigation into the Use of Common Libraries in Android Apps](https://arxiv.org/pdf/1511.06554.pdf) 
 - Another number of more than 1000 library package names that we collected manually from community web sites such as [Android Arsenal](https://android-arsenal.com/)  and [Ultimate Android Reference](https://github.com/aritraroy/UltimateAndroidReference)  

This blacklist is [integrated](https://github.com/TheRimaz/Rimaz/blob/master/Rimaz/src/main/Resources/SortedLibrariesBlackList.txt) (temporarily) within the Rimaz tool.

#### Notes
As some applications may be too complex, their analysis may take a considerable long time.

The current version of Rimaz is developed with parallelism in mind, for the sake of making experimentation in multi-processor computers, using Rimaz in computers with a low number of processors would probably slow down the execution time.

## DataSet components

### Manual analyses dataset

To asses our tool, we tested it on a set of opensource applications and we validated the results manually. You can find the list of the application in this [list](https://github.com/TheRimaz/Rimaz/blob/master/ManuallyValidatedApplicationDataSet.csv).

### Automatic analyses dataset

Our analyses were conducted on a set of Android applications downloaded from Google Play Store.

This [list](https://github.com/TheRimaz/Rimaz/blob/master/TheApplicationDataSet.csv) contains more than 5000 applications that we downloaded randomly from Google Play Store along with the metadata used for the analyses.
