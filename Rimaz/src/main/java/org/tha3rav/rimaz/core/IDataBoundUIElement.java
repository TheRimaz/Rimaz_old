package org.tha3rav.rimaz.core;

import org.tha3rav.rimaz.apk.LayoutFileAttribute;

import java.util.Optional;

public interface IDataBoundUIElement
{
    boolean isDataBoundUIElement();
    Optional<LayoutFileAttribute> getDataBindingAttribute();
}
