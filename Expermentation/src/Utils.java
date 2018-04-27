public class Utils
{
    static public final class ErrorMessages
    {
        public static final String GC_ERROR                                    = "(!)Error: Stopped because of an OutOfMemoryError, GC Overhead limit exceeded";
        public static final String TIMEOUT_ERROR                               = "(!)Error: Application couldn't be analyzed in less than %d hours";
        public static final String DIRECTORY_AND_FILE_TOGETHER_PARAMETER_ERROR = "(!)Error: Cannot accept both %s and %s parameters at once !";
        public static final String INCORRECT_DIRECTORY_FILE_ERROR = "(!)Error:  Could't locate given source !";
        public static final String OUTPUT_ERROR = "(!)Error: Couldn't write to output file.";
        public static final String ENABLE_CREATE_OUTPUT_ERROR = "(!)Error: Couldn't create output file.";
        public static final String NO_JAR_PARAMETERS_ERROR = "(!)Error: No parameters were specified, you should specify parameters, enter Experimentation -h for help.";
        public static final String NO_SOURCE_PARAMETERS_ERROR = "(!)Error: No directory or apk file was specified, enter Experimentation -h for help.";
        public static final String NO_PLATFORM_DIRECTORY_ERROR = "(!)Error: No Android platforms were specified, enter Experimentation -h for help.";
        public static final String NO_TIMEOUT_PARAMETER_ERROR = "(!)Error: No timeOut was specified, enter Experimentation -h for help.";



        public static final String INCORRECT_TIMEOUT_PARAMETER = "(!)Error: Incorrect timeout parameter";
        public static final String INCORRECT_DIRECTORY_PARAMETER = "(!)Error: Incorrect directory parameter";
        public static final String INCORRECT_PLATFORMS_DIRECTORY_PARAMETER = "(!)Error: Incorrect Android platforms directory parameter";
        public static final String INCORRECT_FILE_PARAMETER = "(!)Error: Incorrect file parameter";


        public static final String PLATFORMS_PARAMETER_KEY   = "-p";
        public static final String TIMEOUT_PARAMETER_KEY   = "-t";
        public static final String DIRECTORY_PARAMETER_KEY = "-d";
        public static final String FILE_PARAMETER_KEY      = "-f";
        public static final String OUTPUT_PARAMETER_KEY      = "-o";

    }

    static public final class WarningMessages
    {
        public static final String OUTPUT_FILE_NOT_EXIST_WARNING = "(*)Warning: The specified output file doesn't exist !";
        public static final String CREATING_OUTPUT_FILE_WARNING = "(*)Warning: Creating output file \"%s\" ...";
    }

}
