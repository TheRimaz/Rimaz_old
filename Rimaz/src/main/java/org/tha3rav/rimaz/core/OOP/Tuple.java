package org.tha3rav.rimaz.core.OOP;

public class Tuple<T1,T2>
{
    private T1 firstParameter;
    private T2 secondParameter;

    public Tuple(T1 parameter1, T2 parameter2)
    {
        this.firstParameter = parameter1;
        this.secondParameter = parameter2;
    }

    public Tuple()
    {
    }

    public T1 getFirstParameter()
    {
        return firstParameter;
    }

    public void setFirstParameter(T1 firstParameter)
    {
        this.firstParameter = firstParameter;
    }

    public T2 getSecondParameter()
    {
        return secondParameter;
    }

    public void setSecondParameter(T2 secondParameter)
    {
        this.secondParameter = secondParameter;
    }
}
