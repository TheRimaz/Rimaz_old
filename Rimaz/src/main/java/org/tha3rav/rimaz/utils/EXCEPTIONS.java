package org.tha3rav.rimaz.utils;

import static org.tha3rav.rimaz.utils.APK.APKConstants.ANDROID_MANIFEST_FILE_NAME;

public abstract class EXCEPTIONS
{
    public static final class ExceptionMessages
    {
        public static final String NO_PACKAGE_ATTRIBUTE_IN_MANIFEST_ERROR_MESSAGE = String.format("Can't find Package attribute in {0} file", ANDROID_MANIFEST_FILE_NAME);
        public static final String UNFOUND_MANIFEST_FILE_ERROR_MESSAGE = String.format("Can't find manifest file");
        public static final String NOT_EVENT_HANDLER_CLASS_ERROR_MESSAGE = "Class %s is not a class that has event handlers.";
    }
}
