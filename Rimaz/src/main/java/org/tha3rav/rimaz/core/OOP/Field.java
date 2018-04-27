package org.tha3rav.rimaz.core.OOP;

import java.util.Optional;
import soot.SootField;

public class Field
{
    private SootField sootField;
    private boolean hasSetter;
    private boolean hasGetter;
    private Optional<Setter> setter;
    private Optional<Getter> getter;

    public Field(SootField sootField)
    {
        this.sootField = sootField;
        getter = Optional.empty();
        setter = Optional.empty();
    }

    public Field(SootField sootField, Optional<Getter> getter, Optional<Setter> setter)
    {
        this.sootField = sootField;
        this.getter = getter;
        this.setter = setter;
        getter.ifPresent(g -> hasGetter = true);
        setter.ifPresent(s -> hasSetter = true);
    }

    public Field(SootField sootField, Optional<Getter> getter)
    {
        this.sootField = sootField;
        this.getter = getter;
        getter.ifPresent(g -> hasGetter = true);
    }

    public boolean hasSetter()
    {
        return hasSetter;
    }

    public void setHasSetter(boolean hasSetter)
    {
        this.hasSetter = hasSetter;
    }

    public boolean hasGetter()
    {
        return hasGetter;
    }

    public void setHasGetter(boolean hasGetter)
    {
        this.hasGetter = hasGetter;
    }

    public Optional<Setter> getSetter()
    {
        return setter;
    }

    public void setSetter(Optional<Setter> setter)
    {
        this.setter = setter;
    }

    public Optional<Getter> getGetter()
    {
        return getter;
    }

    public void setGetter(Optional<Getter> getter)
    {
        this.getter = getter;
    }

    public SootField getSootField()
    {
        return sootField;
    }

    public void setSootField(SootField sootField)
    {
        this.sootField = sootField;
    }
}
