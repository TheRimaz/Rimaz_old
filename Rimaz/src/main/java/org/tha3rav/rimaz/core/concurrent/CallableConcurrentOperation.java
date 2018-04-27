package org.tha3rav.rimaz.core.concurrent;

import org.tha3rav.rimaz.apk.ApkTypeEntry;
import org.tha3rav.rimaz.core.OOP.Method;

import java.util.List;

public class CallableConcurrentOperation extends AbstractConcurrentOperation
{
    public CallableConcurrentOperation(ApkTypeEntry invokingObject,
                                       List<Method> executionMethods)
    {
        super(invokingObject,
              executionMethods);
    }
}
