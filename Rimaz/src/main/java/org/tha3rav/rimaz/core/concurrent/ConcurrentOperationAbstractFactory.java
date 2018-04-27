package org.tha3rav.rimaz.core.concurrent;

import org.tha3rav.rimaz.core.Statements.AbstractStatement;

public abstract class ConcurrentOperationAbstractFactory
{

    public abstract AbstractConcurrentOperation getConcurrentOperation(AbstractStatement abstractStatement);

    //Doesn't work properly, soot doesn't identify implemented interfaces in some cases
    protected abstract boolean isRunnableOperation(AbstractStatement abstractStatement);
    protected abstract boolean isCallableOperation(AbstractStatement abstractStatement);
    protected abstract boolean isAsyncTaskOperation(AbstractStatement abstractStatement);
}
