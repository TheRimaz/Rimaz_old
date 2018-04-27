package org.tha3rav.rimaz.core.OOP;

import java.util.List;

public interface IMethodsFilter
{
    List<Getter> getGetters();

    List<Setter> getSetters();

    List<Constructor> getConstructors();
}
